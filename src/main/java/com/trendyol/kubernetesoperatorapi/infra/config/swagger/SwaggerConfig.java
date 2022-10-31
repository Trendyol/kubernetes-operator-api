package com.trendyol.kubernetesoperatorapi.infra.config.swagger;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.Optional;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfig {

    private static final String EMPTY_VALUE = "-";

    private final SwaggerProperties properties;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        var license = properties.getLicense();
        return new ApiInfo(
                properties.getAppName(),
                properties.getDescription(),
                properties.getVersion(),
                EMPTY_VALUE,
                properties.getContact(),
                Optional.ofNullable(license).map(License::getName).orElse(EMPTY_VALUE),
                Optional.ofNullable(license).map(License::getUrl).orElse(EMPTY_VALUE),
                Collections.emptyList());
    }
}