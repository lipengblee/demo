package com.star.lp.module.trade.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 订单统计响应
 */
@Data
@Schema(description = "订单统计响应")
public class OrderStatsRespVO {

    @Schema(description = "待处理订单数", example = "5")
    private Integer pending;

    @Schema(description = "打印中订单数", example = "3")
    private Integer printing;

    @Schema(description = "已完成订单数", example = "12")
    private Integer completed;

    @Schema(description = "总订单数", example = "20")
    private Integer total;

    @Schema(description = "今日订单数", example = "8")
    private Integer todayCount;

    @Schema(description = "今日营业额", example = "12800")
    private Integer todayAmount;
}
