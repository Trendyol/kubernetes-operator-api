package com.trendyol.kubernetesoperatorapi.adapter.rest.operator.create.request;

import com.trendyol.kubernetesoperatorapi.domain.command.CreateDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.enumtype.DataCenter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeploymentRequest {

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]*$")
    private String runId;

    @NotNull
    private DataCenter dataCenter;

    @NotNull
    @Positive
    private int workerCount;

    public CreateDeploymentCommand toModel() {
        return CreateDeploymentCommand.builder()
                .runId(runId)
                .dataCenter(dataCenter)
                .workerCount(workerCount)
                .build();
    }
}
