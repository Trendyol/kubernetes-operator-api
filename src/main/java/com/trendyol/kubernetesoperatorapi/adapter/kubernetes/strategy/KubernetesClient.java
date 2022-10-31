package com.trendyol.kubernetesoperatorapi.adapter.kubernetes.strategy;

import com.trendyol.kubernetesoperatorapi.domain.exception.BusinessException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class KubernetesClient {

    private final Map<String, KubernetesClientStrategy> kubernetesClientStrategyHashMap = new HashMap<>();

    public KubernetesClient(ListableBeanFactory beanFactory) {
        beanFactory.getBeansOfType(KubernetesClientStrategy.class)
                .values()
                .forEach(service -> kubernetesClientStrategyHashMap.put(service.getDataCenter(), service));
    }

    public DataCenterSettings getClient(String dataCenter) {
        return Optional.ofNullable(kubernetesClientStrategyHashMap.get(dataCenter))
                .orElseThrow(() -> new BusinessException("Kubernetes client strategy is not found !"))
                .getClient();
    }
}
