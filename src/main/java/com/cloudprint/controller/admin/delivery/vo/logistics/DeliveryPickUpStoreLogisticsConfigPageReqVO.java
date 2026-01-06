package com.star.lp.module.trade.controller.admin.delivery.vo.logistics;

import com.star.lp.framework.common.pojo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 管理后台 - 自提门店物流配置分页 Request VO
 */
@Schema(description = "管理后台 - 自提门店物流配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeliveryPickUpStoreLogisticsConfigPageReqVO extends PageParam {

    @Schema(description = "自提门店ID，外键关联trade_delivery_pick_up_store.id", example = "1024")
    private Long storeId;

    @Schema(description = "物流名称", example = "顺丰速运")
    private String logisticsName;

    @Schema(description = "寄件人姓名", example = "张三")
    private String senderName;

    @Schema(description = "寄件人电话", example = "13800138000")
    private String senderPhone;

    @Schema(description = "寄件人省份", example = "北京市")
    private String senderProvince;

    @Schema(description = "寄件人城市", example = "北京市")
    private String senderCity;

    @Schema(description = "寄件人区县", example = "朝阳区")
    private String senderDistrict;

}
