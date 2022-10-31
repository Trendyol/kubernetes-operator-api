package com.trendyol.kubernetesoperatorapi.domain.exception;

import com.trendyol.kubernetesoperatorapi.infra.common.BaseResponse;

public class ClientApiBusinessException extends ClientApiBaseException {

    private static final long serialVersionUID = 918472304740612225L;

    public ClientApiBusinessException(BaseResponse baseResponse, String exception) {
        super(baseResponse, exception);
    }
}
