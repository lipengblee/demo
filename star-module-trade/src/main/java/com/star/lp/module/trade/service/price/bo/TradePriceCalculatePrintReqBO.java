package com.star.lp.module.trade.service.price.bo;

import com.star.lp.module.trade.enums.delivery.DeliveryTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 价格计算 Request BO
 *
 * @author star源码
 */
@Data
public class TradePriceCalculatePrintReqBO {

    /**
     * 用户编号
     *
     * 对应 MemberUserDO 的 id 编号
     */
    private Long userId;

    /**
     * 优惠劵编号
     *
     * 对应 CouponDO 的 id 编号
     */
    private Long couponId;

    /**
     * 是否使用积分
     */
    @NotNull(message = "是否使用积分不能为空")
    private Boolean pointStatus;

    /**
     * 配送方式
     *
     * 枚举 {@link DeliveryTypeEnum}
     */
    private Integer deliveryType;
    /**
     * 收货地址编号
     *
     * 对应 MemberAddressDO 的 id 编号
     */
    private Long addressId;
    /**
     * 自提门店编号
     *
     * 对应 PickUpStoreDO 的 id 编号
     */
    private Long pickUpStoreId;

    /**
     * 商品 SKU 数组
     */
    @NotNull(message = "商品数组不能为空")
    private List<Item> items;

    // ========== 秒杀活动相关字段 ==========
    /**
     * 秒杀活动编号
     */
    private Long seckillActivityId;

    // ========== 拼团活动相关字段 ==========
    /**
     * 拼团活动编号
     */
    private Long combinationActivityId;

    /**
     * 拼团团长编号
     */
    private Long combinationHeadId;

    // ========== 砍价活动相关字段 ==========
    /**
     * 砍价记录编号
     */
    private Long bargainRecordId;

    // ========== 积分商城活动相关字段 ==========
    /**
     * 积分商城活动编号
     */
    private Long pointActivityId;

    /**
     * 商品 SKU
     */
    @Data
    @Valid
    public static class Item {

        /**
         * 数量
         */
        @NotNull(message = "商品 SKU 数量不能为空")
        @Min(value = 0L, message = "商品 SKU 数量必须大于等于 0")
        private Integer count;

        /**
         * 购物车项的编号
         */
        private Long cartId;

        /**
         * 是否选中
         */
        @NotNull(message = "是否选中不能为空")
        private Boolean selected;

        @Schema(description = "复制数量", example = "1")
        @Min(value = 1, message = "复制数量最小值为 {value}")
        private Integer copyCount;

        @Schema(description = "总页数", example = "7")
        @NotNull(message = "总页数不能为空")
        private Integer totalPages;

        @Schema(description = "单价", example = "16")
        @NotNull(message = "单价不能为空")
        private Integer unitPrice;

        @Schema(description = "总价", example = "112")
        @NotNull(message = "总价不能为空")
        private Integer totalPrice;


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
