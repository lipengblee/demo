package com.star.lp.module.trade.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 门店订单响应
 */
@Data
@Schema(description = "门店订单响应")
public class StoreOrderRespVO {

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单号")
    private String no;

    @Schema(description = "订单状态")
    private String status;

    @Schema(description = "打印状态")
    private String printStatus;

    @Schema(description = "收件人信息")
    private String receiverName;

    @Schema(description = "收件人电话")
    private String receiverMobile;

    @Schema(description = "支付金额")
    private Integer payPrice;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "订单项")
    private List<RecentOrderRespVO.OrderItemVO> items;
}
