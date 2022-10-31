package com.trendyol.kubernetesoperatorapi.infra.interceptor;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.trendyol.kubernetesoperatorapi.infra.constant.AuditorConstants.X_AGENTNAME;
import static com.trendyol.kubernetesoperatorapi.infra.constant.AuditorConstants.X_AGENTNAME_KEBAB_CASE;

@RequiredArgsConstructor
public class AgentNameInterceptor extends HandlerInterceptorAdapter {

    private final String applicationName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String agentName = request.getHeader(X_AGENTNAME);

        if (StringUtils.isBlank(agentName)) {
            agentName = request.getHeader(X_AGENTNAME_KEBAB_CASE);
        }

        if (StringUtils.isBlank(agentName)) {
            agentName = applicationName;
        }

        MDC.put(X_AGENTNAME, agentName);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.remove(X_AGENTNAME);
    }
}
