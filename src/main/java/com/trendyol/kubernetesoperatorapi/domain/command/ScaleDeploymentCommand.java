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
public class ScaleDeploymentCommand {

    private String runId;
    private DataCenter dataCenter;
    private int workerCount;
}
