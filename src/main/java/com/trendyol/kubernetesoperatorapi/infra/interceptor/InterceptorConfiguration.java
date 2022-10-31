package com.trendyol.kubernetesoperatorapi.infra.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    private static final int MAX_AGE = 3600;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${cors.webInternalIp}")
    private String webInternalIp;

    @Value("${cors.webExternalIp}")
    private String webExternalIp;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CorrelationIdInterceptor());
        registry.addInterceptor(new ExecutorUserInterceptor());
        registry.addInterceptor(new AgentNameInterceptor(applicationName));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(webInternalIp, webExternalIp)
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedMethods(GET.name(), POST.name(), DELETE.name(), PUT.name(), OPTIONS.name(), PATCH.name())
                .maxAge(MAX_AGE);
    }
}

