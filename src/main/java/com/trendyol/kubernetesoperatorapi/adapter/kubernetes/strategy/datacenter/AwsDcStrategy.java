package com.trendyol.kubernetesoperatorapi.adapter.kubernetes.strategy.datacenter;

import com.trendyol.kubernetesoperatorapi.adapter.kubernetes.strategy.DataCenterSettings;
import com.trendyol.kubernetesoperatorapi.adapter.kubernetes.strategy.KubernetesClientStrategy;
import com.trendyol.kubernetesoperatorapi.domain.enumtype.DataCenter;
import com.trendyol.kubernetesoperatorapi.domain.exception.BusinessException;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;

import static com.trendyol.kubernetesoperatorapi.infra.constant.Constant.DEFAULT_IMAGE_NAME_PREFIX;

@Slf4j
@Service
public class AwsDcStrategy implements KubernetesClientStrategy {

    private volatile DataCenterSettings dataCenterSettings = null;

    @Override
    public String getDataCenter() {
        return DataCenter.AWS.getValue();
    }

    @Override
    public DataCenterSettings getClient() {
        String kubeConfigPath = "/etc/kube/aws/AWS";

        if (dataCenterSettings == null) {
            synchronized (AwsDcStrategy.class) {
                if (dataCenterSettings == null) {
                    try {
                        ApiClient apiClient = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
                        dataCenterSettings = DataCenterSettings.builder()
                                .apiClient(apiClient)
                                .imageNamePrefix(DEFAULT_IMAGE_NAME_PREFIX)
                                .build();
                    } catch (IOException e) {
                        log.error("Encountered an exception while consuming get client: {} , exception message: {}", e.getMessage(), e);
                        throw new BusinessException("Encountered an exception while consuming get client");
                    }
                }
            }
        }
        return dataCenterSettings;
    }
}
