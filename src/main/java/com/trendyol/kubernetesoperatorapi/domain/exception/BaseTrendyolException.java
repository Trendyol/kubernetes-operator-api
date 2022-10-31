package com.trendyol.kubernetesoperatorapi.domain.exception;

import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

@Getter
public abstract class BaseTrendyolException extends RuntimeException {

    private final String key;
    private final String[] args;

    protected BaseTrendyolException(String key) {
        this.key = key;
        this.args = ArrayUtils.EMPTY_STRING_ARRAY;
    }

    protected BaseTrendyolException(String key, String... args) {
        this.key = key;
        this.args = args;
    }

    @Override
    public String getMessage() {
        return "Business exception occurred " + key + " " + StringUtils.join(args);
    }
}