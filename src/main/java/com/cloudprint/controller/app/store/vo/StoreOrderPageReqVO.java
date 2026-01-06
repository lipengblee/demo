package com.star.lp.module.trade.controller.app.store.vo;

import com.star.lp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门店订单分页请求
 */
@Data
@Schema(description = "门店订单分页请求")
public class StoreOrderPageReqVO extends PageParam {

    @Schema(description = "订单状态")
    private String status;

    @Schema(description = "打印状态")
    private String printStatus;

    @Schema(description = "关键词")
    private String keyword;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;
}
