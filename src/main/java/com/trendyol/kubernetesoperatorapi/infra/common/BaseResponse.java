package com.trendyol.kubernetesoperatorapi.infra.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BaseResponse implements Serializable {

    private String exception;
    private List<ErrorDetailDTO> errorDetailModel;
    private long timeStamp;

    public BaseResponse(String exception, String... exceptionMessage) {
        if (StringUtils.isNotBlank(exception)) {
            this.exception = exception;
        }

        Stream.of(exceptionMessage).forEach(msg -> {
            var errorDetail = new ErrorDetailDTO();
            errorDetail.setMessage(msg);
            this.errorDetailModel.add(errorDetail);
        });
    }
}
