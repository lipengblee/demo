package com.star.lp.module.trade.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 打印队列项响应
 */
@Data
@Schema(description = "打印队列项响应")
public class PrintQueueItemRespVO {

    @Schema(description = "队列ID")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "队列索引")
    private Integer queueIndex;

    @Schema(description = "任务状态")
    private String status; // waiting, printing, completed, failed, paused, cancelled

    @Schema(description = "文档标题")
    private String documentTitle;

    @Schema(description = "页数")
    private Integer pages;

    @Schema(description = "份数")
    private Integer copies;

    @Schema(description = "客户姓名")
    private String customerName;

    @Schema(description = "打印进度")
    private Integer progress;

    @Schema(description = "错误信息")
    private String errorMessage;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "完成时间")
    private LocalDateTime completeTime;
}
