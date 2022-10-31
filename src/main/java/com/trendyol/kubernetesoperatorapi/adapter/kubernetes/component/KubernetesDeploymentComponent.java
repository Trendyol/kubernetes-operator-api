package com.trendyol.kubernetesoperatorapi.adapter.kubernetes.component;

import com.trendyol.kubernetesoperatorapi.domain.enumtype.RollBackLevel;
import com.trendyol.kubernetesoperatorapi.domain.exception.RollBackBusinessException;
import com.trendyol.kubernetesoperatorapi.infra.annotation.RetryOperation;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.trendyol.kubernetesoperatorapi.infra.constant.Constant.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesDeploymentComponent {

    private final KubernetesYamlConverterComponent kubernetesYamlConverterComponent;

    @RetryOperation(retryCount = 3, waitSeconds = 1)
    public String deployMaster(ApiClient apiClient, Map<String, String> variables) {
        AppsV1Api appsV1Api = new AppsV1Api(apiClient);
        String runId = variables.get("RUN_ID");
        String deploymentName = MASTER_DEPLOYMENT_NAME_PREFIX + runId;

        V1Deployment deployment = (V1Deployment) kubernetesYamlConverterComponent.convertKubernetesYml(variables, MASTER_DEPLOYMENT_YAML_PATH, V1Deployment.class);

        try {
            appsV1Api.createNamespacedDeployment(NAMESPACE, deployment, null, null, null, null);
        } catch (ApiException e) {
            log.error("Encountered an exception while consuming create name spaced deployment by deploymentName: {} , exceptionMessage: {}", deploymentName, e.getResponseBody());
            throw new RollBackBusinessException("Encountered an exception while consuming create name spaced deployment for master", RollBackLevel.BEFORE_MASTER_DEPLOYMENT);
        }
        return deploymentName;
    }

    @RetryOperation(retryCount = 3, waitSeconds = 1)
    public String deployWorker(ApiClient apiClient, Map<String, String> variables, int workerCount) {
        AppsV1Api appsV1Api = new AppsV1Api(apiClient);
        String runId = variables.get("RUN_ID");
        String deploymentName = WORKER_DEPLOYMENT_NAME_PREFIX + runId;

        V1Deployment deployment = (V1Deployment) kubernetesYamlConverterComponent.convertKubernetesYml(variables, WORKER_DEPLOYMENT_YAML_PATH, V1Deployment.class);
        deployment.getSpec().replicas(workerCount);

        try {
            appsV1Api.createNamespacedDeployment(NAMESPACE, deployment, null, null, null, null);
        } catch (ApiException e) {
            log.error("Encountered an exception while consuming create name spaced deployment by deploymentName: {} , exceptionMessage: {}", deploymentName, e.getMessage(), e);
            throw new RollBackBusinessException("Encountered an exception while consuming create name spaced deployment for worker", RollBackLevel.BEFORE_WORKER_DEPLOYMENT);
        }
        return deploymentName;
    }

    @RetryOperation(retryCount = 3, waitSeconds = 1)
    public boolean rollOutStatus(String runId, ApiClient apiClient, String deploymentName, String appName, int replicaCount) {
        AppsV1Api appsV1Api = new AppsV1Api(apiClient);
        CoreV1Api coreV1Api = new CoreV1Api(apiClient);

        long start = System.currentTimeMillis();
        boolean deploymentReady = false;
        while (!deploymentReady) {
            if (System.currentTimeMillis() - start > TIMEOUT_VALUE_FOR_ROLLOUT) {
                break;
            }
            try {
                V1DeploymentStatus status = appsV1Api.readNamespacedDeployment(deploymentName, NAMESPACE, null).getStatus();

                int readyReplicas = Objects.nonNull(status) ? Optional.ofNullable(status.getReadyReplicas()).orElse(0) : 0;

                String labelSelector = String.format("id=%s,app=%s", runId, appName);
                V1PodList list = coreV1Api.listNamespacedPod(NAMESPACE, null, null, null, "status.phase=Running", labelSelector, Integer.MAX_VALUE, null, null, null, Boolean.FALSE);

                log.info("Waiting for deployment {} to finish. {}/{} replicas are available. {}/{} pods are running", deploymentName, readyReplicas, replicaCount, list.getItems().size(), replicaCount);
                deploymentReady = readyReplicas >= replicaCount && list.getItems().size() >= replicaCount;

                if (!deploymentReady) {
                    Thread.sleep(INTERVAL_VALUE_FOR_ROLLOUT);
                }
            } catch (Exception ignored) {
                //This exception ignored, this method will try again without waiting.
            }
        }

        if (deploymentReady) {
            log.info("Created deployment successfully: {}", deploymentName);
            return true;
        } else {
            log.error("An error occurred while creating deployment: {}", deploymentName);
            return false;
        }
    }

    @RetryOperation(retryCount = 5, waitSeconds = 1)
    public void createService(ApiClient apiClient, String runId) {
        Map<String, String> variables = Map.of("RUN_ID", runId);

        CoreV1Api coreV1Api = new CoreV1Api(apiClient);
        V1Service masterService = (V1Service) kubernetesYamlConverterComponent.convertKubernetesYml(variables, MASTER_SERVICE_YAML_PATH, V1Service.class);

        try {
            coreV1Api.createNamespacedService(NAMESPACE, masterService, null, null, null, null);
        } catch (ApiException e) {
            log.error("Encountered an exception while consuming create name spaced component by serviceName: {} , exceptionMessage: {}", MASTER_SERVICE, e.getResponseBody());
            throw new RollBackBusinessException("Encountered an exception while consuming create name spaced component for " + MASTER_SERVICE, RollBackLevel.BEFORE_SERVICE);
        }
        log.info("component/{}-{} created", MASTER_SERVICE, runId);
    }

    @RetryOperation(retryCount = 5, waitSeconds = 1)
    public void createServiceUi(ApiClient apiClient, String runId) {
        Map<String, String> variables = Map.of("RUN_ID", runId);

        CoreV1Api coreV1Api = new CoreV1Api(apiClient);
        V1Service masterService = (V1Service) kubernetesYamlConverterComponent.convertKubernetesYml(variables, MASTER_SERVICE_UI_YAML_PATH, V1Service.class);

        try {
            coreV1Api.createNamespacedService(NAMESPACE, masterService, null, null, null, null);
        } catch (ApiException e) {
            log.error("Encountered an exception while consuming create name spaced component by serviceName: {} , exceptionMessage: {}", MASTER_SERVICE_UI, e.getResponseBody());
            throw new RollBackBusinessException("Encountered an exception while consuming create name spaced component for " + MASTER_SERVICE_UI, RollBackLevel.BEFORE_SERVICE_UI);
        }
        log.info("component/{}-{} created", MASTER_SERVICE_UI, runId);
    }

    public void printPodStatus(ApiClient apiClient, String runId) {
        CoreV1Api coreV1Api = new CoreV1Api(apiClient);
        try {
            V1PodList list = coreV1Api.listNamespacedPod(NAMESPACE, null, null, null, null, "id=" + runId, Integer.MAX_VALUE, null, null, null, Boolean.FALSE);
            var podInfos = new ArrayList<Object[]>();
            podInfos.add(new String[]{"NAME", "STATUS", "AGE"});
            long nowAsSecond = System.currentTimeMillis() / 1000;
            for (V1Pod pod : list.getItems()) {
                final long age = nowAsSecond - pod.getStatus().getStartTime().toEpochSecond();
                podInfos.add(new Object[]{pod.getMetadata().getName(), pod.getStatus().getPhase(), age + "s"});
            }

            for (Object[] row : podInfos) {
                System.out.format("%-55s%-15s%-15s%n", row);
            }
        } catch (ApiException ignored) {
            //This exception ignored.
        }
    }

    @RetryOperation(retryCount = 5, waitSeconds = 1)
    public V1ServiceList retrieveNamespacedServiceList(ApiClient apiClient, String runId) {
        CoreV1Api coreV1Api = new CoreV1Api(apiClient);
        V1ServiceList serviceList;
        try {
            serviceList = coreV1Api.listNamespacedService(NAMESPACE, null, null, null, null, "id=" + runId, Integer.MAX_VALUE, null, null, null, Boolean.FALSE);
        } catch (ApiException e) {
            log.error("Encountered an exception while listing name spaced service by exceptionMessage: {}", e.getResponseBody());
            throw new RollBackBusinessException("Encountered an exception while listing name spaced service by runId: {}" + runId, RollBackLevel.ALL_FLOW);
        }

        return serviceList;
    }

    public void printServiceStatus(V1ServiceList list) {
        var serviceInfos = new ArrayList<Object[]>();
        serviceInfos.add(new String[]{"NAME", "TYPE", "CLUSTER-IP", "PORT(S)", "AGE"});
        long nowAsSecond = System.currentTimeMillis() / 1000;
        for (V1Service service : list.getItems()) {

            var ports = service.getSpec().getPorts();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ports.size(); i++) {
                sb.append(ports.get(i).getPort());
                sb.append(Objects.nonNull(ports.get(i).getNodePort()) ? ":" + ports.get(i).getNodePort() : "");
                sb.append("/");
                sb.append(ports.get(i).getProtocol());

                if (i + 1 < ports.size()) {
                    sb.append(",");
                }
            }

            long age = nowAsSecond - service.getMetadata().getCreationTimestamp().toEpochSecond();
            serviceInfos.add(new Object[]{service.getMetadata().getName(), service.getSpec().getType(), service.getSpec().getClusterIP(), sb.toString(), age + "s"});
        }

        for (Object[] row : serviceInfos) {
            System.out.format("%-40s%-15s%-15s%-25s%-15s%n", row);
        }
    }
}
