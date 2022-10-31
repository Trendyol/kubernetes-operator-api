package com.trendyol.kubernetesoperatorapi.adapter.kubernetes;

import com.google.gson.JsonSyntaxException;
import com.trendyol.kubernetesoperatorapi.adapter.kubernetes.component.KubernetesDeploymentComponent;
import com.trendyol.kubernetesoperatorapi.adapter.kubernetes.strategy.DataCenterSettings;
import com.trendyol.kubernetesoperatorapi.adapter.kubernetes.strategy.KubernetesClient;
import com.trendyol.kubernetesoperatorapi.application.port.OperatorApiPort;
import com.trendyol.kubernetesoperatorapi.domain.command.CreateDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.command.ScaleDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.enumtype.RollBackLevel;
import com.trendyol.kubernetesoperatorapi.domain.exception.BusinessException;
import com.trendyol.kubernetesoperatorapi.domain.exception.RollBackBusinessException;
import com.trendyol.kubernetesoperatorapi.infra.annotation.RetryOperation;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ServiceList;
import io.kubernetes.client.openapi.models.V1Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.trendyol.kubernetesoperatorapi.infra.constant.Constant.*;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "mock.kubernetes.api.enabled", havingValue = "false", matchIfMissing = true)
public class KubernetesApiAdapter implements OperatorApiPort {

    private final KubernetesClient kubernetesClient;
    private final KubernetesDeploymentComponent kubernetesDeploymentComponent;

    @Override
    public void createDeployment(CreateDeploymentCommand command) {
        log.info("Started create deployment by runId: {}", command.getRunId());

        DataCenterSettings dataCenterSettings = kubernetesClient.getClient(command.getDataCenter().name());
        ApiClient apiClient = dataCenterSettings.getApiClient();
        String imageNamePrefix = dataCenterSettings.getImageNamePrefix();
        Map<String, String> variables = prepareVariablesMap(command, imageNamePrefix);

        String deploymentNameForMaster = kubernetesDeploymentComponent.deployMaster(apiClient, variables);
        if (!kubernetesDeploymentComponent.rollOutStatus(command.getRunId(), apiClient, deploymentNameForMaster, APP_NAME_MASTER, 1)) {
            log.error("Encountered an exception while roll out created name spaced deployment for master");
            throw new RollBackBusinessException("Encountered an exception while roll out created name spaced deployment for master", RollBackLevel.BEFORE_WORKER_DEPLOYMENT);
        }

        String deploymentNameForWorker = kubernetesDeploymentComponent.deployWorker(apiClient, variables, command.getWorkerCount());
        if (!kubernetesDeploymentComponent.rollOutStatus(command.getRunId(), apiClient, deploymentNameForWorker, APP_NAME_WORKER, command.getWorkerCount())) {
            log.error("Encountered an exception while roll out created name spaced deployment for worker");
            throw new RollBackBusinessException("Encountered an exception while roll out created name spaced deployment for worker", RollBackLevel.BEFORE_SERVICE);
        }

        kubernetesDeploymentComponent.createService(apiClient, command.getRunId());
        kubernetesDeploymentComponent.createServiceUi(apiClient, command.getRunId());
        kubernetesDeploymentComponent.printPodStatus(apiClient, command.getRunId());
        V1ServiceList v1ServiceList = kubernetesDeploymentComponent.retrieveNamespacedServiceList(apiClient, command.getRunId());
        kubernetesDeploymentComponent.printServiceStatus(v1ServiceList);

        log.info("Finished create deployment by runId: {}", command.getRunId());
    }

    private Map<String, String> prepareVariablesMap(CreateDeploymentCommand command, String imageNamePrefix) {
        Map<String, String> variables = new HashMap<>();
        variables.put("RUN_ID", command.getRunId());
        variables.put("IMAGE", imageNamePrefix + command.getImageName());
        variables.put("DATA_CENTER", command.getDataCenter().name());
        return variables;
    }

    @Override
    @RetryOperation(retryCount = 5, waitSeconds = 1)
    public void scaleDeployment(ScaleDeploymentCommand command) {
        log.info("Started scale deployment by runId: {} , workerCount: {}", command.getRunId(), command.getWorkerCount());
        DataCenterSettings settings = kubernetesClient.getClient(command.getDataCenter().name());
        ApiClient apiClient = settings.getApiClient();
        Configuration.setDefaultApiClient(apiClient);

        AppsV1Api appsV1Api = new AppsV1Api(apiClient);
        String deploymentName = WORKER_DEPLOYMENT_NAME_PREFIX + command.getRunId();
        String jsonPatchStr = String.format("[{\"op\":\"replace\",\"path\":\"/spec/replicas\",\"value\":%d}]", command.getWorkerCount());

        try {
            appsV1Api.patchNamespacedDeploymentScale(deploymentName, NAMESPACE, new V1Patch(jsonPatchStr), null, null, null, null, null);
            if (!kubernetesDeploymentComponent.rollOutStatus(command.getRunId(), apiClient, deploymentName, APP_NAME_WORKER, command.getWorkerCount())) {
                log.error("Encountered an exception while roll out scaled name spaced deployment for worker");
                throw new RollBackBusinessException("Encountered an exception while roll out scaled name spaced deployment for worker", RollBackLevel.BEFORE_SERVICE);
            }
        } catch (ApiException e) {
            log.error("Encountered an exception while consuming scale deployment by deploymentName: {} , exceptionMessage: {}", deploymentName, e.getResponseBody());
            throw new BusinessException("Encountered an exception while consuming scale deployment");
        }

        log.info("Finished scale deployment by runId: {} , workerCount: {}", command.getRunId(), command.getWorkerCount());
    }

    @Override
    @RetryOperation(retryCount = 5, waitSeconds = 1)
    public void deleteDeployment(String dataCenterName, String deploymentName) {
        log.info("Started delete deployment by deploymentName: {}", deploymentName);

        DataCenterSettings settings = kubernetesClient.getClient(dataCenterName);
        ApiClient apiClient = settings.getApiClient();
        Configuration.setDefaultApiClient(apiClient);
        AppsV1Api appsV1Api = new AppsV1Api(apiClient);

        V1Status status;
        try {
            status = appsV1Api.deleteNamespacedDeployment(deploymentName, NAMESPACE, null, null, null, null, null, null);
        } catch (ApiException e) {
            log.error("Encountered an exception while consuming delete deployment by deploymentName: {} , exceptionMessage : {}", deploymentName, e.getResponseBody());
            throw new BusinessException("Encountered an exception while consuming delete deployment");
        }

        if (SUCCESS.equals(status.getStatus())) {
            log.info("Finished delete deployment by deploymentName: {}", deploymentName);
        } else {
            log.error("An error occurred while deleting deployment for deploymentName: {} ", deploymentName);
        }
    }

    @Override
    @RetryOperation(retryCount = 5, waitSeconds = 1)
    public void deleteService(String dataCenterName, String serviceName) {
        log.info("Started delete service by serviceName: {}", serviceName);

        DataCenterSettings settings = kubernetesClient.getClient(dataCenterName);
        ApiClient apiClient = settings.getApiClient();
        Configuration.setDefaultApiClient(apiClient);

        CoreV1Api coreV1Api = new CoreV1Api(apiClient);
        try {
            coreV1Api.deleteNamespacedService(serviceName, NAMESPACE, null, null, null, null, null, null);
        } catch (JsonSyntaxException ignored) {
            /*
                Ignored due to issue of k8 client.
                https://github.com/kubernetes-client/java/issues/2307.If the issue is resolved this try-catch should be removed
            */
        } catch (ApiException e) {
            log.error("Encountered an exception while consuming delete component by serviceName: {} , exceptionMessage: {}", serviceName, e.getResponseBody());
            throw new BusinessException("Encountered an exception while consuming delete component");
        }

        log.info("Finished delete service by serviceName: {}", serviceName);
    }
}
