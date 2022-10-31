package com.trendyol.kubernetesoperatorapi.adapter.rest.operator.create;

import com.trendyol.kubernetesoperatorapi.adapter.rest.operator.create.request.CreateDeploymentRequest;
import com.trendyol.kubernetesoperatorapi.application.CreateOperatorFacade;
import com.trendyol.kubernetesoperatorapi.base.AbstractMvc;
import com.trendyol.kubernetesoperatorapi.domain.command.CreateDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.enumtype.DataCenter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreateOperatorController.class)
class CreateOperatorControllerMockMvcTest extends AbstractMvc {

    @MockBean
    private CreateOperatorFacade createOperatorFacade;

    @Test
    void should_create_deployment() throws Exception {
        //given
        CreateDeploymentRequest request = CreateDeploymentRequest.builder()
                .runId("runid")
                .dataCenter(DataCenter.AWS)
                .workerCount(3)
                .build();

        CreateDeploymentCommand command = request.toModel();

        //when
        mockMvc.perform(
                        post("/v1/deployments")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());

        //then
        verify(createOperatorFacade).createDeployment(command);
    }
}