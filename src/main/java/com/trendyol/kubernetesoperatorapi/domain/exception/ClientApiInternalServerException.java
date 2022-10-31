package com.trendyol.kubernetesoperatorapi.domain.exception;

import com.trendyol.kubernetesoperatorapi.infra.common.BaseResponse;

public class ClientApiInternalServerException extends ClientApiBaseException {

    private static final long serialVersionUID = 8873864100950989673L;

    public ClientApiInternalServerException(BaseResponse baseResponse, String exception) {
        super(baseResponse, exception);
    }
}
