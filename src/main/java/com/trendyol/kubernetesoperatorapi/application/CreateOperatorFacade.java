package com.trendyol.kubernetesoperatorapi.application;

import com.trendyol.kubernetesoperatorapi.application.port.OperatorApiPort;
import com.trendyol.kubernetesoperatorapi.domain.command.CreateDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.exception.BusinessException;
import com.trendyol.kubernetesoperatorapi.domain.exception.RollBackBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.trendyol.kubernetesoperatorapi.infra.constant.Constant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOperatorFacade {

    private final OperatorApiPort operatorApiPort;

    public void createDeployment(CreateDeploymentCommand command) {
        try {
            operatorApiPort.createDeployment(command);
        } catch (RollBackBusinessException rollBackBusinessException) {
            rollBackDeployment(rollBackBusinessException, command);
        }
    }

    private void rollBackDeployment(RollBackBusinessException rollBackBusinessException, CreateDeploymentCommand command) {
        switch (rollBackBusinessException.getLevel()) {
            case 5:
                log.error("Rollback master ui service for runId: {}", command.getRunId());
                operatorApiPort.deleteService(command.getDataCenter().name(), MASTER_SERVICE_UI_NAME_PREFIX + command.getRunId());
                // ! Don't use break operator
            case 4:
                log.error("Rollback service for runId: {}", command.getRunId());
                operatorApiPort.deleteService(command.getDataCenter().name(), MASTER_SERVICE_NAME_PREFIX + command.getRunId());
                // ! Don't use break operator
            case 3:
                log.error("Rollback worker deployment for runId: {}", command.getRunId());
                operatorApiPort.deleteDeployment(command.getDataCenter().name(), WORKER_DEPLOYMENT_NAME_PREFIX + command.getRunId());
                // ! Don't use break operator
            case 2:
                log.error("Rollback master deployment for runId: {}", command.getRunId());
                operatorApiPort.deleteDeployment(command.getDataCenter().name(), MASTER_DEPLOYMENT_NAME_PREFIX + command.getRunId());
                // ! Don't use break operator
            case 1:
                log.error("Encountered an exception while creating deployment for runId: {}", command.getRunId());
                throw new BusinessException("Encountered an exception while creating deployment");
        }
    }
}
