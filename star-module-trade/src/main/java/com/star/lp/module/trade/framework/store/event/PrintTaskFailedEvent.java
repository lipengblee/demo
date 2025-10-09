package com.star.lp.module.trade.framework.store.event;

import lombok.Data;

/**
 * 打印任务失败事件
 */
@Data
public class PrintTaskFailedEvent extends PrintTaskEvent {

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 重试次数
     */
    private Integer retryCount;
}
