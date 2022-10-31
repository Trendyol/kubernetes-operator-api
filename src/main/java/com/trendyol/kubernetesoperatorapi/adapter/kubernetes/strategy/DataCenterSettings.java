package com.trendyol.kubernetesoperatorapi.adapter.kubernetes.strategy;

import io.kubernetes.client.openapi.ApiClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataCenterSettings {

    private ApiClient apiClient;
    private String imageNamePrefix;
}
