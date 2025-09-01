package com.star.lp.module.trade.controller.app.order.vo;

import cn.hutool.core.util.ObjUtil;
import com.star.lp.framework.common.validation.InEnum;
import com.star.lp.framework.common.validation.Mobile;
import com.star.lp.module.trade.enums.delivery.DeliveryTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "用户 App - 交易订单结算-Print Request VO")
@Data
@Valid
public class AppTradeOrderSettlementPrintReqVO {

    @Schema(description = "打印项数组", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "打印项数据不能为空")
    private List<Item> items;

    @Schema(description = "优惠劵编号", example = "1024")
    private Long couponId;

    @Schema(description = "是否使用积分", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "是否使用积分不能为空")
    private Boolean pointStatus;

    // ========== 配送相关相关字段 ==========
    @Schema(description = "配送方式", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @InEnum(value = DeliveryTypeEnum.class, message = "配送方式不正确")
    private Integer deliveryType;

    @Schema(description = "收件地址编号", example = "1")
    private Long addressId;

    @Schema(description = "自提门店编号", example = "1088")
    private Long pickUpStoreId;
    @Schema(description = "收件人名称", example = "芋艿") // 选择门店自提时，该字段为联系人名
    private String receiverName;
    @Schema(description = "收件人手机", example = "15601691300") // 选择门店自提时，该字段为联系人手机
    @Mobile(message = "收件人手机格式不正确")
    private String receiverMobile;

    // ========== 秒杀活动相关字段 ==========
    @Schema(description = "秒杀活动编号", example = "1024")
    private Long seckillActivityId;

    // ========== 拼团活动相关字段 ==========
    @Schema(description = "拼团活动编号", example = "1024")
    private Long combinationActivityId;

    @Schema(description = "拼团团长编号", example = "2048")
    private Long combinationHeadId;

    // ========== 砍价活动相关字段 ==========
    @Schema(description = "砍价记录编号", example = "123")
    private Long bargainRecordId;

    // ========== 积分商城活动相关字段 ==========
    @Schema(description = "积分商城活动编号", example = "123")
    private Long pointActivityId;

    @AssertTrue(message = "活动商品每次只能购买一种规格")
    @JsonIgnore
    public boolean isValidActivityItems() {
        // 校验是否是活动订单
        if (ObjUtil.isAllEmpty(seckillActivityId, combinationActivityId, combinationHeadId, bargainRecordId)) {
            return true;
        }
        // 校验订单项是否超出
        return items.size() == 1;
    }

    @Schema(description = "用户 App - 文档项")
    @Data
    @Valid
    public static class Item {

        @Schema(description = "复制数量", example = "1")
        @Min(value = 1, message = "复制数量最小值为 {value}")
        private Integer copyCount;

        @Schema(description = "总页数", example = "7")
        @NotNull(message = "总页数不能为空")
        private Integer totalPages;

        @Schema(description = "单价", example = "1.6")
        @NotNull(message = "单价不能为空")
        private Integer unitPrice;

        @Schema(description = "总价", example = "11.2")
        @NotNull(message = "总价不能为空")
        private Integer totalPrice;

        @Schema(description = "购物车项的编号", example = "1024")
        private Long cartId;

        @Schema(description = "选中选项", example = "{\"1\":1,\"2\":4}")
        @NotNull(message = "选中选项不能为空")
        private String selectedOptions;

        @AssertTrue(message = "商品数据不正确")
        @JsonIgnore
        public boolean isValid() {
            return copyCount != null && totalPages != null &&
                    unitPrice != null && totalPrice != null &&
                    selectedOptions != null && !selectedOptions.isEmpty();
        }
    }


}
