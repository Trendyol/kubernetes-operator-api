package com.trendyol.kubernetesoperatorapi.application;

import com.trendyol.kubernetesoperatorapi.application.port.OperatorApiPort;
import com.trendyol.kubernetesoperatorapi.base.OrderTest;
import com.trendyol.kubernetesoperatorapi.domain.command.CreateDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.enumtype.DataCenter;
import com.trendyol.kubernetesoperatorapi.domain.enumtype.RollBackLevel;
import com.trendyol.kubernetesoperatorapi.domain.exception.BusinessException;
import com.trendyol.kubernetesoperatorapi.domain.exception.RollBackBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static com.trendyol.kubernetesoperatorapi.infra.constant.Constant.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.doThrow;

class CreateOperatorFacadeTest extends OrderTest {

    private CreateOperatorFacade createOperatorFacade;

    @Mock
    private OperatorApiPort operatorApiPort;

    @Captor
    private ArgumentCaptor<CreateDeploymentCommand> createDeploymentCommandArgumentCaptor;

    @BeforeEach
    void setUp() {
        createOperatorFacade = new CreateOperatorFacade(operatorApiPort);
    }

    @Test
    void should_create_deployment() {
        //given
        CreateDeploymentCommand command = CreateDeploymentCommand.builder()
                .runId("runid")
                .dataCenter(DataCenter.AWS)
                .workerCount(3)
                .imageName("imagename")
                .build();

        //when
        createOperatorFacade.createDeployment(command);

        //then
        inOrder.verify(operatorApiPort).createDeployment(createDeploymentCommandArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        CreateDeploymentCommand value = createDeploymentCommandArgumentCaptor.getValue();
        assertThat(value.getRunId()).isEqualTo("runid");
        assertThat(value.getDataCenter()).isEqualTo(DataCenter.AWS);
        assertThat(value.getWorkerCount()).isEqualByComparingTo(3);
        assertThat(value.getImageName()).isEqualTo("imagename");
    }

    @Test
    void throw_rollback_business_exception_for_all_flow() {
        //given
        CreateDeploymentCommand command = CreateDeploymentCommand.builder()
                .runId("runid")
                .dataCenter(DataCenter.AWS)
                .workerCount(3)
                .imageName("imagename")
                .build();

        doThrow(new RollBackBusinessException("All flow exception", RollBackLevel.ALL_FLOW))
                .doNothing()
                .when(operatorApiPort).createDeployment(command);

        //when
        Throwable throwable = catchThrowable(() -> createOperatorFacade.createDeployment(command));

        //then
        assertThat(throwable)
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Encountered an exception while creating deployment");

        inOrder.verify(operatorApiPort).createDeployment(command);
        inOrder.verify(operatorApiPort).deleteService(command.getDataCenter().name(), MASTER_SERVICE_UI_NAME_PREFIX + command.getRunId());
        inOrder.verify(operatorApiPort).deleteService(command.getDataCenter().name(), MASTER_SERVICE_NAME_PREFIX + command.getRunId());
        inOrder.verify(operatorApiPort).deleteDeployment(command.getDataCenter().name(), WORKER_DEPLOYMENT_NAME_PREFIX + command.getRunId());
        inOrder.verify(operatorApiPort).deleteDeployment(command.getDataCenter().name(), MASTER_DEPLOYMENT_NAME_PREFIX + command.getRunId());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void throw_rollback_business_exception_for_before_service_ui() {
        //given
        CreateDeploymentCommand command = CreateDeploymentCommand.builder()
                .runId("runid")
                .dataCenter(DataCenter.AWS)
                .workerCount(3)
                .imageName("imagename")
                .build();

        doThrow(new RollBackBusinessException("Before service ui exception", RollBackLevel.BEFORE_SERVICE_UI))
                .doNothing()
                .when(operatorApiPort).createDeployment(command);

        //when
        Throwable throwable = catchThrowable(() -> createOperatorFacade.createDeployment(command));

        //then
        assertThat(throwable)
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Encountered an exception while creating deployment");

        inOrder.verify(operatorApiPort).createDeployment(command);
        inOrder.verify(operatorApiPort).deleteService(command.getDataCenter().name(), MASTER_SERVICE_NAME_PREFIX + command.getRunId());
        inOrder.verify(operatorApiPort).deleteDeployment(command.getDataCenter().name(), WORKER_DEPLOYMENT_NAME_PREFIX + command.getRunId());
        inOrder.verify(operatorApiPort).deleteDeployment(command.getDataCenter().name(), MASTER_DEPLOYMENT_NAME_PREFIX + command.getRunId());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void throw_rollback_business_exception_for_before_service() {
        //given
        CreateDeploymentCommand command = CreateDeploymentCommand.builder()
                .runId("runid")
                .dataCenter(DataCenter.AWS)
                .workerCount(3)
                .imageName("imagename")
                .build();

        doThrow(new RollBackBusinessException("Before service exception", RollBackLevel.BEFORE_SERVICE))
                .doNothing()
                .when(operatorApiPort).createDeployment(command);

        //when
        Throwable throwable = catchThrowable(() -> createOperatorFacade.createDeployment(command));

        //then
        assertThat(throwable)
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Encountered an exception while creating deployment");

        inOrder.verify(operatorApiPort).createDeployment(command);
        inOrder.verify(operatorApiPort).deleteDeployment(command.getDataCenter().name(), WORKER_DEPLOYMENT_NAME_PREFIX + command.getRunId());
        inOrder.verify(operatorApiPort).deleteDeployment(command.getDataCenter().name(), MASTER_DEPLOYMENT_NAME_PREFIX + command.getRunId());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void throw_rollback_business_exception_for_before_worker_deployment() {
        //given
        CreateDeploymentCommand command = CreateDeploymentCommand.builder()
                .runId("runid")
                .dataCenter(DataCenter.AWS)
                .workerCount(3)
                .imageName("imagename")
                .build();

        doThrow(new RollBackBusinessException("Before worker deployment exception", RollBackLevel.BEFORE_WORKER_DEPLOYMENT))
                .doNothing()
                .when(operatorApiPort).createDeployment(command);

        //when
        Throwable throwable = catchThrowable(() -> createOperatorFacade.createDeployment(command));

        //then
        assertThat(throwable)
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Encountered an exception while creating deployment");

        inOrder.verify(operatorApiPort).createDeployment(command);
        inOrder.verify(operatorApiPort).deleteDeployment(command.getDataCenter().name(), MASTER_DEPLOYMENT_NAME_PREFIX + command.getRunId());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void throw_rollback_business_exception_for_before_master_deployment() {
        //given
        CreateDeploymentCommand command = CreateDeploymentCommand.builder()
                .runId("runid")
                .dataCenter(DataCenter.AWS)
                .workerCount(3)
                .imageName("imagename")
                .build();

        doThrow(new RollBackBusinessException("Before master deployment exception", RollBackLevel.BEFORE_MASTER_DEPLOYMENT))
                .doNothing()
                .when(operatorApiPort).createDeployment(command);

        //when
        Throwable throwable = catchThrowable(() -> createOperatorFacade.createDeployment(command));

        //then
        assertThat(throwable)
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Encountered an exception while creating deployment");

        inOrder.verify(operatorApiPort).createDeployment(command);
        inOrder.verifyNoMoreInteractions();
    }
}