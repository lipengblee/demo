package com.star.lp.module.trade.enums.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 纸张状态枚举
 */
@Getter
@AllArgsConstructor
public enum PaperStatusEnum {

    SUFFICIENT("sufficient", "充足"),
    LOW("low", "不足"),
    EMPTY("empty", "缺纸");

    private final String status;
    private final String description;
}
