package com.trendyol.kubernetesoperatorapi.adapter.rest.operator.scale.request;

import com.trendyol.kubernetesoperatorapi.domain.command.ScaleDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.enumtype.DataCenter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScaleDeploymentRequest {

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]*$")
    private String runId;

    @NotNull
    private DataCenter dataCenter;

    @NotNull
    @PositiveOrZero
    private int workerCount;

    public ScaleDeploymentCommand toModel() {
        return ScaleDeploymentCommand.builder()
                .runId(runId)
                .dataCenter(dataCenter)
                .workerCount(workerCount)
                .build();
    }
}
