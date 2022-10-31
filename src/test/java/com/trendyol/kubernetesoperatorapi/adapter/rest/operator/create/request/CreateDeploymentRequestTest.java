package com.trendyol.kubernetesoperatorapi.adapter.rest.operator.create.request;

import com.trendyol.kubernetesoperatorapi.base.BaseTest;
import com.trendyol.kubernetesoperatorapi.domain.command.CreateDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.enumtype.DataCenter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CreateDeploymentRequestTest extends BaseTest {

    @Test
    void should_convert() {
        //given
        CreateDeploymentRequest request = CreateDeploymentRequest.builder()
                .runId("runid")
                .dataCenter(DataCenter.AWS)
                .workerCount(3)
                .build();

        //when
        CreateDeploymentCommand command = request.toModel();

        //then
        assertThat(command.getRunId()).isEqualTo("runid");
        assertThat(command.getDataCenter()).isEqualTo(DataCenter.AWS);
        assertThat(command.getWorkerCount()).isEqualByComparingTo(3);
        assertThat(command.getImageName()).isNull();
    }
}