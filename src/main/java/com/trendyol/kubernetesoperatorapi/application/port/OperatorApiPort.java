package com.trendyol.kubernetesoperatorapi.application.port;

import com.trendyol.kubernetesoperatorapi.domain.command.CreateDeploymentCommand;
import com.trendyol.kubernetesoperatorapi.domain.command.ScaleDeploymentCommand;

public interface OperatorApiPort {

    void createDeployment(CreateDeploymentCommand command);

    void scaleDeployment(ScaleDeploymentCommand command);

    void deleteDeployment(String dataCenterName, String deploymentName);

    void deleteService(String dataCenterName, String serviceName);
}
