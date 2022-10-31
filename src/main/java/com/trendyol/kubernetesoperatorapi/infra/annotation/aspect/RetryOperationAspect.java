package com.trendyol.kubernetesoperatorapi.infra.annotation.aspect;

import com.trendyol.kubernetesoperatorapi.infra.annotation.RetryOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class RetryOperationAspect {

    @Around(value = "@annotation(com.trendyol.kubernetesoperatorapi.infra.annotation.RetryOperation)")
    public Object retryOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        Object response = null;
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RetryOperation annotation = method.getAnnotation(RetryOperation.class);
        int retryCount = annotation.retryCount();
        int waitSeconds = annotation.waitSeconds();
        boolean successful = false;

        do {
            try {
                response = joinPoint.proceed();
                successful = true;
            } catch (Exception e) {
                log.error("Operation failed, retries remaining: {}", retryCount);
                retryCount--;
                if (retryCount <= 0) {
                    throw e;
                }
                if (waitSeconds > 0) {
                    log.warn("Waiting for {} second(s) before next retry", waitSeconds);
                    Thread.sleep(waitSeconds * 1000L);
                }
            }
        } while (!successful);

        return response;
    }
}