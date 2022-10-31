package com.trendyol.kubernetesoperatorapi.application;

import com.trendyol.kubernetesoperatorapi.application.port.OperatorApiPort;
import com.trendyol.kubernetesoperatorapi.domain.command.ScaleDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScaleOperatorFacade {

    private final OperatorApiPort operatorApiPort;

    public void scaleDeployment(ScaleDeploymentCommand command) {
        try {
            operatorApiPort.scaleDeployment(command);
        } catch (Exception e) {
            log.error("Encountered an exception while scaling deployment for runId: {}", command.getRunId());
            throw new BusinessException("Encountered an exception while scaling deployment");
        }
    }
}
