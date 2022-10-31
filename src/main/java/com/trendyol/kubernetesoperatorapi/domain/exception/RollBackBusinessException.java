package com.trendyol.kubernetesoperatorapi.domain.exception;

import com.trendyol.kubernetesoperatorapi.domain.enumtype.RollBackLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RollBackBusinessException extends BaseTrendyolException {

    private static final long serialVersionUID = 1L;

    private int level;

    public RollBackBusinessException(String exception) {
        super(exception);
    }

    public RollBackBusinessException(String exception, RollBackLevel rollBackLevel) {
        super(exception);
        this.level = rollBackLevel.getValue();
    }
}
