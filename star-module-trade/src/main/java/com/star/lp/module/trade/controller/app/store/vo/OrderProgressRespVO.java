package com.star.lp.module.trade.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单进度响应
 */
@Data
@Schema(description = "订单进度响应")
public class OrderProgressRespVO {

    @Schema(description = "打印进度", example = "65")
    private Integer progress;

    @Schema(description = "打印状态")
    private String status;

    @Schema(description = "当前页", example = "5")
    private Integer currentPage;

    @Schema(description = "总页数", example = "10")
    private Integer totalPages;

    @Schema(description = "预计完成时间")
    private LocalDateTime estimatedCompleteTime;
}
