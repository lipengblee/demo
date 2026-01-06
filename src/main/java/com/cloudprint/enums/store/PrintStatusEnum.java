package com.star.lp.module.trade.enums.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 打印状态枚举
 */
@Getter
@AllArgsConstructor
public enum PrintStatusEnum {

    PENDING("pending", "待处理"),
    ASSIGNED("assigned", "已指派"),
    WAITING("waiting", "等待中"),
    PRINTING("printing", "打印中"),
    COMPLETED("completed", "打印已完成"),
    FAILED("failed", "失败"),
    PAUSED("paused", "暂停"),
    CANCELLED("cancelled", "已取消");

    private final String status;
    private final String description;
}
