package com.star.lp.module.trade.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 设备统计响应
 */
@Data
@Schema(description = "设备统计响应")
public class DeviceStatsRespVO {

    @Schema(description = "在线设备数", example = "3")
    private Integer online;

    @Schema(description = "忙碌设备数", example = "2")
    private Integer busy;

    @Schema(description = "离线设备数", example = "1")
    private Integer offline;

    @Schema(description = "总设备数", example = "6")
    private Integer total;
}
