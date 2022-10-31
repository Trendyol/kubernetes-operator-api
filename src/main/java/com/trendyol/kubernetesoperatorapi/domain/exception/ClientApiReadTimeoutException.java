package com.trendyol.kubernetesoperatorapi.domain.exception;

import com.trendyol.kubernetesoperatorapi.infra.common.BaseResponse;

public class ClientApiReadTimeoutException extends ClientApiBaseException {

    private static final long serialVersionUID = 3235684905554169944L;

    public ClientApiReadTimeoutException(BaseResponse baseResponse, String exception) {
        super(baseResponse, exception);
    }
}
