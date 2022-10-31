package com.trendyol.kubernetesoperatorapi.infra.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuditorConstants {

    public static final String X_CORRELATION_ID = "x-correlationid";
    public static final String X_CORRELATION_ID_KEBAB_CASE = "x-correlation-id";
    public static final String X_CORRELATION_ID_CAMEL_CASE = "x-correlationId";
    public static final String X_AGENTNAME = "x-agentname";
    public static final String X_AGENTNAME_KEBAB_CASE = "x-agentname";
    public static final String X_EXECUTOR_USER = "x-executor-user";
}
