package com.trendyol.kubernetesoperatorapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.retry.annotation.EnableRetry;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableRetry
@EnableHystrix
@EnableSwagger2
@SpringBootApplication
public class KubernetesOperatorApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(KubernetesOperatorApiApplication.class, args);
    }
}
