package com.star.lp.module.trade.framework.store.listener;

import com.star.lp.module.trade.framework.store.event.PrintTaskCompletedEvent;
import com.star.lp.module.trade.framework.store.event.PrintTaskFailedEvent;
import com.star.lp.module.trade.framework.store.event.PrintTaskStartedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 打印任务事件监听器
 */
@Component
@Slf4j
public class PrintTaskEventListener {

    @EventListener
    public void handleTaskStarted(PrintTaskStartedEvent event) {
        log.info("[PrintTaskEventListener] 打印任务开始: 队列ID={}, 订单ID={}, 设备ID={}",
                event.getQueueId(), event.getOrderId(), event.getDeviceId());

        // TODO: 发送开始通知，更新统计数据等
    }

    @EventListener
    public void handleTaskCompleted(PrintTaskCompletedEvent event) {
        log.info("[PrintTaskEventListener] 打印任务完成: 队列ID={}, 订单ID={}, 设备ID={}",
                event.getQueueId(), event.getOrderId(), event.getDeviceId());

        // TODO: 发送完成通知，更新统计数据等
    }

    @EventListener
    public void handleTaskFailed(PrintTaskFailedEvent event) {
        log.error("[PrintTaskEventListener] 打印任务失败: 队列ID={}, 订单ID={}, 设备ID={}, 错误={}",
                event.getQueueId(), event.getOrderId(), event.getDeviceId(), event.getErrorMessage());

        // TODO: 发送失败通知，记录错误日志等
    }
}