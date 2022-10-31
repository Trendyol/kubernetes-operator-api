package com.trendyol.kubernetesoperatorapi.infra.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static com.trendyol.kubernetesoperatorapi.infra.constant.AuditorConstants.*;

public class CorrelationIdInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String correlationId = request.getHeader(X_CORRELATION_ID);

        if (StringUtils.isBlank(correlationId)) {
            correlationId = request.getHeader(X_CORRELATION_ID_CAMEL_CASE);
        }

        if (StringUtils.isBlank(correlationId)) {
            correlationId = request.getHeader(X_CORRELATION_ID_KEBAB_CASE);
        }

        if (StringUtils.isBlank(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }

        MDC.put(X_CORRELATION_ID, correlationId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.remove(X_CORRELATION_ID);
    }
}

