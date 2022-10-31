package com.trendyol.kubernetesoperatorapi.adapter.rest.operator.terminate;

import com.trendyol.kubernetesoperatorapi.application.TerminateOperatorFacade;
import com.trendyol.kubernetesoperatorapi.domain.command.TerminateDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.enumtype.DataCenter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TerminateOperatorController {

    private final TerminateOperatorFacade terminateOperatorFacade;

    @DeleteMapping("/v1/deployments/{runId}/data-center/{dataCenter}")
    public void terminateDeployment(@PathVariable String runId, @PathVariable DataCenter dataCenter) {
        TerminateDeploymentCommand command = TerminateDeploymentCommand.of(runId, dataCenter);
        (new Thread(() -> terminateOperatorFacade.terminateDeployment(command))).start();
    }
}
