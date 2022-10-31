package com.trendyol.kubernetesoperatorapi.domain.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DataCenter {

    GOOGLE("GOOGLE"),
    AWS("AWS"),
    DUMMY("DUMMY");

    private final String value;
}
