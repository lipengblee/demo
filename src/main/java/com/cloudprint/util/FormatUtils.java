package com.cloudprint.util;

import com.cloudprint.model.TradeOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DecimalFormat;

/**
 * 格式化工具类，提供各种格式化方法
 */
public class FormatUtils {
    
    /**
     * 格式化文件大小，转换为可读格式
     * @param fileSize 文件大小（字节）
     * @return 格式化后的文件大小字符串
     */
    public static String formatFileSize(Long fileSize) {
        if (fileSize == null || fileSize < 0) {
            return "0 B";
        }
        
        final String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = fileSize;
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(size) + " " + units[unitIndex];
    }
    
    /**
     * 获取订单状态文本
     * @param order 订单对象
     * @return 状态文本
     */
    public static String getStatusText(TradeOrder order) {
        if (order == null) {
            return "未知状态";
        }
        
        Integer status = order.getStatus();
        Integer deliveryType = order.getDeliveryType();
        Boolean commentStatus = order.getCommentStatus();
        
        if (status == 0) {
            return "待付款";
        }
        
        if (status == 10) {
            if (deliveryType == 1) {
                return "待发货";
            } else if (deliveryType == 2) {
                return "待核销";
            }
        }
        
        if (status == 20) {
            return "待收货";
        }
        
        if (status == 30) {
            if (commentStatus == null || !commentStatus) {
                return "待评价";
            } else {
                return "已完成";
            }
        }
        
        return "已关闭";
    }
    
    /**
     * 获取打印状态中文文本
     * @param printStatus 英文打印状态
     * @return 中文打印状态
     */
    public static String getPrintStatusText(String printStatus) {
        if (printStatus == null) {
            return "未知状态";
        }
        
        switch (printStatus.toLowerCase()) {
            case "pending":
                return "待处理";
            case "assigned":
                return "已指派";
            case "waiting":
                return "等待中";
            case "printing":
                return "打印中";
            case "completed":
                return "打印已完成";
            case "failed":
                return "失败";
            case "paused":
                return "暂停";
            case "cancelled":
                return "已取消";
            default:
                return printStatus; // 如果没有匹配到，返回原状态
        }
    }
    
    /**
     * 解析商品属性JSON，提取valueName并以标签方式显示
     * @param propertiesJson 商品属性JSON字符串
     * @return 格式化后的属性标签字符串
     */
    public static String parseProperties(String propertiesJson) {
        if (propertiesJson == null || propertiesJson.isEmpty()) {
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            // 解析JSON数组
            JsonNode rootNode = objectMapper.readTree(propertiesJson);
            if (rootNode.isArray()) {
                // 遍历数组中的每个属性对象
                for (JsonNode propertyNode : rootNode) {
                    // 提取valueName
                    JsonNode valueNameNode = propertyNode.get("valueName");
                    if (valueNameNode != null && valueNameNode.isTextual()) {
                        String valueName = valueNameNode.asText();
                        if (result.length() > 0) {
                            result.append(" | ");
                        }
                        result.append(valueName);
                    }
                }
            }
        } catch (JsonProcessingException e) {
            System.err.println("解析商品属性JSON失败: " + e.getMessage());
            return propertiesJson; // 解析失败时返回原字符串
        }
        
        return result.toString();
    }
}