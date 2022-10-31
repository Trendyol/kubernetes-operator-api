package com.trendyol.kubernetesoperatorapi.adapter.kubernetes.strategy;

public interface KubernetesClientStrategy {

    String getDataCenter();

    DataCenterSettings getClient();
}
