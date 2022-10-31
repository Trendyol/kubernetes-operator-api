package com.trendyol.kubernetesoperatorapi.adapter.rest.operator.scale;

import com.trendyol.kubernetesoperatorapi.adapter.rest.operator.scale.request.ScaleDeploymentRequest;
import com.trendyol.kubernetesoperatorapi.application.ScaleOperatorFacade;
import com.trendyol.kubernetesoperatorapi.base.AbstractMvc;
import com.trendyol.kubernetesoperatorapi.domain.command.ScaleDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.enumtype.DataCenter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScaleOperatorController.class)
class ScaleOperatorControllerMockMvcTest extends AbstractMvc {

    @MockBean
    private ScaleOperatorFacade scaleOperatorFacade;

    @Test
    void should_scale_deployment() throws Exception {
        //given
        ScaleDeploymentRequest request = ScaleDeploymentRequest.builder()
                .runId("runid")
                .dataCenter(DataCenter.AWS)
                .workerCount(3)
                .build();

        ScaleDeploymentCommand command = request.toModel();

        //when
        mockMvc.perform(
                        put("/v1/deployments")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        verify(scaleOperatorFacade).scaleDeployment(command);
    }
}