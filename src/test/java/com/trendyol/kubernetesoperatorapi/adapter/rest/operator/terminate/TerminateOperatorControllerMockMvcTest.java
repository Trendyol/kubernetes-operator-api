package com.trendyol.kubernetesoperatorapi.adapter.rest.operator.terminate;

import com.trendyol.kubernetesoperatorapi.application.TerminateOperatorFacade;
import com.trendyol.kubernetesoperatorapi.base.AbstractMvc;
import com.trendyol.kubernetesoperatorapi.domain.command.TerminateDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.enumtype.DataCenter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TerminateOperatorController.class)
class TerminateOperatorControllerMockMvcTest extends AbstractMvc {

    @MockBean
    private TerminateOperatorFacade terminateOperatorFacade;

    @Test
    void should_terminate_deployment() throws Exception {
        //given
        String runId = "runid";
        DataCenter dataCenter = DataCenter.AWS;

        TerminateDeploymentCommand command = TerminateDeploymentCommand.of(runId, dataCenter);
        //when
        mockMvc.perform(
                        delete("/v1/deployments/" + runId + "/data-center/" + dataCenter)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        verify(terminateOperatorFacade).terminateDeployment(command);
    }
}