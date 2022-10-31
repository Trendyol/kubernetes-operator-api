package com.trendyol.kubernetesoperatorapi.application;

import com.trendyol.kubernetesoperatorapi.application.port.OperatorApiPort;
import com.trendyol.kubernetesoperatorapi.base.OrderTest;
import com.trendyol.kubernetesoperatorapi.domain.command.ScaleDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.enumtype.DataCenter;
import com.trendyol.kubernetesoperatorapi.domain.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.doThrow;

class ScaleOperatorFacadeTest extends OrderTest {

    private ScaleOperatorFacade scaleOperatorFacade;

    @Mock
    private OperatorApiPort operatorApiPort;

    @Captor
    private ArgumentCaptor<ScaleDeploymentCommand> scaleDeploymentCommandArgumentCaptor;

    @BeforeEach
    void setUp() {
        scaleOperatorFacade = new ScaleOperatorFacade(operatorApiPort);
    }

    @Test
    void should_scale_deployment() {
        //given
        ScaleDeploymentCommand command = ScaleDeploymentCommand.builder()
                .runId("runid")
                .dataCenter(DataCenter.AWS)
                .workerCount(3)
                .build();

        //when
        scaleOperatorFacade.scaleDeployment(command);

        //then
        inOrder.verify(operatorApiPort).scaleDeployment(scaleDeploymentCommandArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        ScaleDeploymentCommand value = scaleDeploymentCommandArgumentCaptor.getValue();
        assertThat(value.getRunId()).isEqualTo("runid");
        assertThat(value.getDataCenter()).isEqualTo(DataCenter.AWS);
        assertThat(value.getWorkerCount()).isEqualByComparingTo(3);
    }

    @Test
    void throw_business_exception_for_scale_deployment() {
        //given
        ScaleDeploymentCommand command = ScaleDeploymentCommand.builder().runId("runid").workerCount(3).build();

        doThrow(new BusinessException("Scale deployment exception"))
                .doNothing()
                .when(operatorApiPort).scaleDeployment(command);

        //when
        Throwable throwable = catchThrowable(() -> scaleOperatorFacade.scaleDeployment(command));

        //then
        assertThat(throwable)
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Encountered an exception while scaling deployment");

        inOrder.verify(operatorApiPort).scaleDeployment(command);
        inOrder.verifyNoMoreInteractions();
    }
}
