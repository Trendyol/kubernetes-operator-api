package com.trendyol.kubernetesoperatorapi.domain.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RollBackLevel {

    //Queue is important !

    ALL_FLOW(5),
    BEFORE_SERVICE_UI(4),
    BEFORE_SERVICE(3),
    BEFORE_WORKER_DEPLOYMENT(2),
    BEFORE_MASTER_DEPLOYMENT(1);

    private final int value;
}
