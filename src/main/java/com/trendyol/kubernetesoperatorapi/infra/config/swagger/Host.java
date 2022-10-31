package com.trendyol.kubernetesoperatorapi.infra.config.swagger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Host {

    private String url;
    private String protocol = "http";
}
