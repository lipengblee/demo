package com.star.lp.module.trade.framework.store.util;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 门店管理工具类
 */
public class StoreManagementUtils {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd HH:mm");

    /**
     * 格式化时间显示
     */
    public static String formatTimeDisplay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }

        LocalDateTime now = LocalDateTime.now();
        long minutesDiff = LocalDateTimeUtil.between(dateTime, now).toMinutes();

        if (minutesDiff < 1) {
            return "刚刚";
        } else if (minutesDiff < 60) {
            return minutesDiff + "分钟前";
        } else if (minutesDiff < 1440) { // 24小时内
            return (minutesDiff / 60) + "小时前";
        } else {
            return dateTime.format(DATETIME_FORMATTER);
        }
    }

    /**
     * 计算预计完成时间
     */
    public static LocalDateTime calculateEstimatedCompleteTime(Integer totalPages, Integer currentPage,
                                                               LocalDateTime startTime) {
        if (totalPages == null || currentPage == null || startTime == null || currentPage == 0) {
            return null;
        }

        // 假设每页打印需要30秒
        long secondsPerPage = 30;
        long remainingPages = totalPages - currentPage;
        long remainingSeconds = remainingPages * secondsPerPage;

        return LocalDateTime.now().plusSeconds(remainingSeconds);
    }

    /**
     * 生成设备唯一标识
     */
    public static String generateDeviceIdentifier(String name, String address) {
        return StrUtil.format("{}_{}", name.replaceAll("\\s+", "_"),
                address.replaceAll("[^a-zA-Z0-9]", "_"));
    }

    /**
     * 验证设备连接地址格式
     */
    public static boolean isValidDeviceAddress(String address, String connectionType) {
        if (StrUtil.isBlank(address)) {
            return false;
        }

        switch (connectionType.toLowerCase()) {
            case "tcp":
                // 验证IP地址格式
                return address.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
            case "usb":
                // USB设备路径验证
                return address.startsWith("/dev/") || address.startsWith("COM");
            case "bluetooth":
                // 蓝牙地址验证
                return address.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
            default:
                return true;
        }
    }
}