package com.trendyol.kubernetesoperatorapi.infra.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryOperation {

    int retryCount();

    int waitSeconds();
}
