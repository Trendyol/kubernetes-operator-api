package com.trendyol.kubernetesoperatorapi.adapter.rest.operator.create;

import com.trendyol.kubernetesoperatorapi.adapter.rest.operator.create.request.CreateDeploymentRequest;
import com.trendyol.kubernetesoperatorapi.application.CreateOperatorFacade;
import com.trendyol.kubernetesoperatorapi.domain.command.CreateDeploymentCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CreateOperatorController {

    private final CreateOperatorFacade createOperatorFacade;

    @PostMapping("/v1/deployments")
    @ResponseStatus(HttpStatus.CREATED)
    public void createDeployment(@RequestBody @Valid CreateDeploymentRequest request) {
        CreateDeploymentCommand command = request.toModel();
        (new Thread(() -> createOperatorFacade.createDeployment(command))).start();
    }
}
