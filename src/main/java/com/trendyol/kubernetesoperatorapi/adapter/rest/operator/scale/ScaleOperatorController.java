package com.trendyol.kubernetesoperatorapi.adapter.rest.operator.scale;

import com.trendyol.kubernetesoperatorapi.adapter.rest.operator.scale.request.ScaleDeploymentRequest;
import com.trendyol.kubernetesoperatorapi.application.ScaleOperatorFacade;
import com.trendyol.kubernetesoperatorapi.domain.command.ScaleDeploymentCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ScaleOperatorController {

    private final ScaleOperatorFacade scaleOperatorFacade;

    @PutMapping("/v1/deployments")
    public void scaleDeployment(@RequestBody @Valid ScaleDeploymentRequest request) {
        ScaleDeploymentCommand command = request.toModel();
        (new Thread(() -> scaleOperatorFacade.scaleDeployment(command))).start();
    }
}
