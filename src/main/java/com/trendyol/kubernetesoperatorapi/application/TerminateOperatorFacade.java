package com.trendyol.kubernetesoperatorapi.application;

import com.trendyol.kubernetesoperatorapi.application.port.OperatorApiPort;
import com.trendyol.kubernetesoperatorapi.domain.command.TerminateDeploymentCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.trendyol.kubernetesoperatorapi.infra.constant.Constant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TerminateOperatorFacade {

    private final OperatorApiPort operatorApiPort;

    public void terminateDeployment(TerminateDeploymentCommand command) {
        deleteMasterDeployment(command);
        deleteWorkerDeployment(command);
        deleteMasterService(command);
        deleteMasterUiService(command);
    }

    private void deleteMasterDeployment(TerminateDeploymentCommand command) {
        try {
            operatorApiPort.deleteDeployment(command.getDataCenter().getValue(), MASTER_DEPLOYMENT_NAME_PREFIX + command.getRunId());
        } catch (Exception e) {
            log.error("Encountered an exception while deleting master deployment for runId: {}", command.getRunId());
            // ! Don't use throw
        }
    }

    private void deleteWorkerDeployment(TerminateDeploymentCommand command) {
        try {
            operatorApiPort.deleteDeployment(command.getDataCenter().getValue(), WORKER_DEPLOYMENT_NAME_PREFIX + command.getRunId());
        } catch (Exception e) {
            log.error("Encountered an exception while deleting worker deployment for runId: {}", command.getRunId());
            // ! Don't use throw
        }
    }

    private void deleteMasterService(TerminateDeploymentCommand command) {
        try {
            operatorApiPort.deleteService(command.getDataCenter().getValue(), MASTER_SERVICE_NAME_PREFIX + command.getRunId());
        } catch (Exception e) {
            log.error("Encountered an exception while deleting master component for runId: {}", command.getRunId());
            // ! Don't use throw
        }
    }

    private void deleteMasterUiService(TerminateDeploymentCommand command) {
        try {
            operatorApiPort.deleteService(command.getDataCenter().getValue(), MASTER_SERVICE_UI_NAME_PREFIX + command.getRunId());
        } catch (Exception e) {
            log.error("Encountered an exception while deleting master ui component for runId: {}", command.getRunId());
            // ! Don't use throw
        }
    }
}
