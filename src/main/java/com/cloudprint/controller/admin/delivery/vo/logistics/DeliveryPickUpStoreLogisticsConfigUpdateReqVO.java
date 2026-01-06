package com.star.lp.module.trade.controller.admin.delivery.vo.logistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;

/**
 * 管理后台 - 自提门店物流配置更新 Request VO
 */
@Schema(description = "管理后台 - 自提门店物流配置更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeliveryPickUpStoreLogisticsConfigUpdateReqVO extends DeliveryPickUpStoreLogisticsConfigBaseVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
