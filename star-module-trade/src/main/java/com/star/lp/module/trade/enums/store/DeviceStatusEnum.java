package com.star.lp.module.trade.enums.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备状态枚举
 */
@Getter
@AllArgsConstructor
public enum DeviceStatusEnum {

    ONLINE("online", "在线"),
    OFFLINE("offline", "离线"),
    BUSY("busy", "忙碌"),
    PAUSED("paused", "暂停"),
    ERROR("error", "故障");

    private final String status;
    private final String description;
}

