package com.star.lp.module.trade.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 最近订单响应
 */
@Data
@Schema(description = "最近订单响应")
public class RecentOrderRespVO {

    @Schema(description = "订单ID", example = "1")
    private Long id;

    @Schema(description = "订单号", example = "1146347329394184195")
    private String no;

    @Schema(description = "订单状态", example = "UNPAID")
    private String status;

    @Schema(description = "打印状态", example = "pending")
    private String printStatus;

    @Schema(description = "收件人姓名", example = "张三")
    private String receiverName;

    @Schema(description = "收件人手机", example = "13800138000")
    private String receiverMobile;

    @Schema(description = "支付金额", example = "2500")
    private Integer payPrice;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "订单项列表")
    private List<OrderItemVO> items;

    @Data
    @Schema(description = "订单项")
    public static class OrderItemVO {
        @Schema(description = "订单项ID")
        private Long id;

        @Schema(description = "商品名称")
        private String spuName;

        @Schema(description = "商品图片")
        private String picUrl;

        @Schema(description = "商品数量")
        private Integer count;

        @Schema(description = "打印文档信息")
        private PrintDocumentVO printDocument;

        @Schema(description = "支付金额")
        private Integer payPrice;

        @Schema(description = "单价")
        private Integer price;
    }

    @Data
    @Schema(description = "打印文档")
    public static class PrintDocumentVO {
        @Schema(description = "文档ID")
        private Long id;

        @Schema(description = "文档标题")
        private String title;

        @Schema(description = "文档URL")
        private String url;

        @Schema(description = "文档类型")
        private String type;

        @Schema(description = "页数")
        private Integer pages;
    }
}
