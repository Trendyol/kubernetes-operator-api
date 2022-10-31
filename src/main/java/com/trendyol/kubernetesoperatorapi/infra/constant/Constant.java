package com.trendyol.kubernetesoperatorapi.infra.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constant {

    public static final String MASTER_SERVICE_UI_NAME_PREFIX = "master-service-ui-";
    public static final String MASTER_SERVICE_NAME_PREFIX = "master-service-";

    public static final String MASTER_DEPLOYMENT_NAME_PREFIX = "master-deployment-";
    public static final String WORKER_DEPLOYMENT_NAME_PREFIX = "worker-deployment-";

    public static final String DEFAULT_IMAGE_NAME_PREFIX = "default-image-prefix";

    public static final String APP_NAME_MASTER = "app-name-master";
    public static final String APP_NAME_WORKER = "app-name-worker";

    public static final String NAMESPACE = "namespace";

    public static final String MASTER_DEPLOYMENT_YAML_PATH = "/master-deployment.yaml";
    public static final String WORKER_DEPLOYMENT_YAML_PATH = "/worker-deployment.yaml";
    public static final String MASTER_SERVICE_YAML_PATH = "/master-service.yaml";
    public static final String MASTER_SERVICE_UI_YAML_PATH = "/master-service-ui.yaml";

    public static final String MASTER_SERVICE = "/master-service";
    public static final String MASTER_SERVICE_UI = "/master-service-ui";

    public static final String SUCCESS = "Success";

    public static final int INTERVAL_VALUE_FOR_ROLLOUT = 1000;
    public static final int TIMEOUT_VALUE_FOR_ROLLOUT = 60000 * 3;
}
