package com.trendyol.kubernetesoperatorapi.adapter.kubernetes;

import com.trendyol.kubernetesoperatorapi.application.port.OperatorApiPort;
import com.trendyol.kubernetesoperatorapi.domain.command.CreateDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.command.ScaleDeploymentCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(value = "mock.kubernetes.api.enabled", havingValue = "true")
public class KubernetesApiMockAdapter implements OperatorApiPort {

    @Override
    public void createDeployment(CreateDeploymentCommand command) {
        log.info("Started mock create deployment by runId: {}", command.getRunId());
        log.info("Finished mock create deployment by runId: {}", command.getRunId());
    }

    @Override
    public void scaleDeployment(ScaleDeploymentCommand command) {
        log.info("Started mock scale deployment by runId: {}", command.getRunId());
        log.info("Finished mock scale deployment by runId: {}", command.getRunId());
    }

    @Override
    public void deleteDeployment(String dataCenterName, String deploymentName) {
        log.info("Started mock delete deployment");
        log.info("Finished mock delete deployment");
    }

    @Override
    public void deleteService(String dataCenterName, String serviceName) {
        log.info("Started mock delete service");
        log.info("Finished mock delete service");
    }
}
