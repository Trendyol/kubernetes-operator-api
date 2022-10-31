package com.trendyol.kubernetesoperatorapi.domain.command;

import com.trendyol.kubernetesoperatorapi.base.BaseTest;
import com.trendyol.kubernetesoperatorapi.domain.enumtype.DataCenter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TerminateDeploymentCommandTest extends BaseTest {

    @Test
    void should_convert() {
        //given
        String runId = "runid";
        DataCenter dataCenter = DataCenter.AWS;

        //when
        TerminateDeploymentCommand command = TerminateDeploymentCommand.of(runId, dataCenter);

        //then
        assertThat(command.getRunId()).isEqualTo("runid");
        assertThat(command.getDataCenter()).isEqualTo(DataCenter.AWS);
    }
}