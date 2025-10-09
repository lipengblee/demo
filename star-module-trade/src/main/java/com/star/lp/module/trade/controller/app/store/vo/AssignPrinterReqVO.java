package com.star.lp.module.trade.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 指派打印机请求
 */
@Data
@Schema(description = "指派打印机请求")
public class AssignPrinterReqVO {

    @Schema(description = "订单ID", required = true, example = "1")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Schema(description = "设备ID", required = true, example = "1")
    @NotNull(message = "设备ID不能为空")
    private Long deviceId;

    @Schema(description = "优先级", example = "1")
    private Integer priority; // 1-高, 2-中, 3-低

    @Schema(description = "备注")
    private String remark;
}
