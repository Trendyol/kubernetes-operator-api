package com.trendyol.kubernetesoperatorapi.domain.exception;

import com.trendyol.kubernetesoperatorapi.infra.common.BaseResponse;
import lombok.Getter;

@Getter
public class ClientApiBaseException extends RuntimeException {

    private static final long serialVersionUID = 918472304740612225L;

    private final BaseResponse baseResponse;
    private final String exception;

    public ClientApiBaseException(BaseResponse baseResponse, String exception) {
        super(baseResponse.toString());
        this.baseResponse = baseResponse;
        this.exception = exception;
    }
}
