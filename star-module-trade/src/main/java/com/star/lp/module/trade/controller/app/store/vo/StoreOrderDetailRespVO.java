package com.star.lp.module.trade.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情响应
 */
@Data
@Schema(description = "订单详情响应")
public class StoreOrderDetailRespVO extends StoreOrderRespVO {

    @Schema(description = "用户备注")
    private String userRemark;

    @Schema(description = "商家备注")
    private String remark;

    @Schema(description = "收货地址")
    private String receiverDetailAddress;

    @Schema(description = "收货地区")
    private String receiverAreaName;

    @Schema(description = "指派的设备")
    private StoreDeviceRespVO assignedDevice;

    @Schema(description = "打印进度", example = "65")
    private Integer printProgress;

    @Schema(description = "订单日志")
    private List<OrderLogVO> logs;

    @Schema(description = "优惠金额")
    private Integer discountPrice;

    @Schema(description = "运费")
    private Integer deliveryPrice;

    @Schema(description = "商品总价")
    private Integer totalPrice;

    @Data
    @Schema(description = "订单日志")
    public static class OrderLogVO {
        @Schema(description = "日志标题")
        private String title;

        @Schema(description = "日志内容")
        private String content;

        @Schema(description = "创建时间")
        private LocalDateTime createTime;
    }
}
