package com.trendyol.kubernetesoperatorapi.domain.command;

import com.trendyol.kubernetesoperatorapi.domain.enumtype.DataCenter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerminateDeploymentCommand {

    private String runId;
    private DataCenter dataCenter;

    public static TerminateDeploymentCommand of(String runId, DataCenter dataCenter) {
        return TerminateDeploymentCommand.builder()
                .runId(runId)
                .dataCenter(dataCenter)
                .build();
    }
}
