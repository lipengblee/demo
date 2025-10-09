package com.star.lp.module.trade.framework.store.event;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 打印任务事件基类
 */
@Data
public abstract class PrintTaskEvent {

    /**
     * 队列任务ID
     */
    private Long queueId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 事件时间
     */
    private LocalDateTime eventTime;

    public PrintTaskEvent() {
        this.eventTime = LocalDateTime.now();
    }
}

