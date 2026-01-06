package com.star.lp.module.trade.framework.store.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 门店管理配置
 */
@ConfigurationProperties(prefix = "star.trade.store")
@Validated
@Data
public class StoreManagementProperties {

    /**
     * 是否启用门店管理功能
     */
    @NotNull
    private Boolean enabled = true;

    /**
     * 订单打印超时时间(分钟)
     */
    @NotNull
    private Integer printTimeoutMinutes = 30;

    /**
     * 设备离线判断时间(分钟)
     */
    @NotNull
    private Integer deviceOfflineMinutes = 10;

    /**
     * 队列最大重试次数
     */
    @NotNull
    private Integer maxRetryCount = 3;

    /**
     * 是否启用设备状态监控
     */
    @NotNull
    private Boolean enableDeviceMonitor = true;

    /**
     * 设备监控间隔时间(秒)
     */
    @NotNull
    private Integer monitorIntervalSeconds = 60;
}
