package com.trendyol.kubernetesoperatorapi.infra.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.trendyol.kubernetesoperatorapi.infra.constant.AuditorConstants.X_EXECUTOR_USER;

public class ExecutorUserInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String executorUser = Optional.ofNullable(request.getHeader(X_EXECUTOR_USER))
                .map(Object::toString)
                .orElse(Optional
                        .ofNullable(request.getAttribute(X_EXECUTOR_USER))
                        .map(Object::toString).orElse(StringUtils.EMPTY));
        MDC.put(X_EXECUTOR_USER, executorUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.remove(X_EXECUTOR_USER);
    }
}

