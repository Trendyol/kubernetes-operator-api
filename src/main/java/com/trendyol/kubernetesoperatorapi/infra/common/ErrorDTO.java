package com.trendyol.kubernetesoperatorapi.infra.common;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {

    private String exception;
    private long timestamp = System.currentTimeMillis();
    private List<ErrorDetailDTO> errorDetail = new ArrayList<>();
}
