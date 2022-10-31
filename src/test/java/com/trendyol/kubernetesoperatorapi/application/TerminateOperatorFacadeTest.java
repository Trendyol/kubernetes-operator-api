package com.trendyol.kubernetesoperatorapi.application;

import com.trendyol.kubernetesoperatorapi.application.port.OperatorApiPort;
import com.trendyol.kubernetesoperatorapi.base.OrderTest;
import com.trendyol.kubernetesoperatorapi.domain.command.TerminateDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.enumtype.DataCenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static com.trendyol.kubernetesoperatorapi.infra.constant.Constant.*;

class TerminateOperatorFacadeTest extends OrderTest {

    private TerminateOperatorFacade terminateOperatorFacade;

    @Mock
    private OperatorApiPort operatorApiPort;

    @BeforeEach
    void setUp() {
        terminateOperatorFacade = new TerminateOperatorFacade(operatorApiPort);
    }

    @Test
    void should_terminate_deployment() {
        //given
        TerminateDeploymentCommand command = TerminateDeploymentCommand.builder()
                .runId("runid")
                .dataCenter(DataCenter.AWS)
                .build();

        //when
        terminateOperatorFacade.terminateDeployment(command);

        //then
        inOrder.verify(operatorApiPort).deleteDeployment(command.getDataCenter().getValue(), MASTER_DEPLOYMENT_NAME_PREFIX + command.getRunId());
        inOrder.verify(operatorApiPort).deleteDeployment(command.getDataCenter().getValue(), WORKER_DEPLOYMENT_NAME_PREFIX + command.getRunId());
        inOrder.verify(operatorApiPort).deleteService(command.getDataCenter().getValue(), MASTER_SERVICE_NAME_PREFIX + command.getRunId());
        inOrder.verify(operatorApiPort).deleteService(command.getDataCenter().getValue(), MASTER_SERVICE_UI_NAME_PREFIX + command.getRunId());
        inOrder.verifyNoMoreInteractions();
    }
}