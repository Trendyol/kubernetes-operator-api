package com.trendyol.kubernetesoperatorapi.infra.config.swagger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.service.Contact;

@Getter
@Setter
@ConfigurationProperties(prefix = "swagger")
class SwaggerProperties {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.application.description}")
    private String description;

    private String version;

    private Host host;
    private Contact contact;
    private License license;
}
