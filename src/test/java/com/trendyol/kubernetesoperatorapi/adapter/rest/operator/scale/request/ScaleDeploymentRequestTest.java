package com.trendyol.kubernetesoperatorapi.adapter.rest.operator.scale.request;

import com.trendyol.kubernetesoperatorapi.base.BaseTest;
import com.trendyol.kubernetesoperatorapi.domain.command.ScaleDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.enumtype.DataCenter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ScaleDeploymentRequestTest extends BaseTest {

    @Test
    void should_convert() {
        //given
        ScaleDeploymentRequest request = ScaleDeploymentRequest.builder()
                .runId("runid")
                .dataCenter(DataCenter.AWS)
                .workerCount(3)
                .build();

        //when
        ScaleDeploymentCommand command = request.toModel();

        //then
        assertThat(command.getRunId()).isEqualTo("runid");
        assertThat(command.getDataCenter()).isEqualTo(DataCenter.AWS);
        assertThat(command.getWorkerCount()).isEqualByComparingTo(3);
    }
}