package com.star.lp.module.trade.controller.admin.delivery.vo.logistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 自提门店物流配置 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class DeliveryPickUpStoreLogisticsConfigBaseVO {

    @Schema(description = "自提门店ID，外键关联trade_delivery_pick_up_store.id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "自提门店ID不能为空")
    private Long storeId;

    @Schema(description = "物流名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "顺丰速运")
    @NotBlank(message = "物流名称不能为空")
    private String logisticsName;

    @Schema(description = "应用密钥", requiredMode = Schema.RequiredMode.REQUIRED, example = "abc123")
    @NotBlank(message = "应用密钥不能为空")
    private String appKey;

    @Schema(description = "应用密钥", requiredMode = Schema.RequiredMode.REQUIRED, example = "def456")
    @NotBlank(message = "应用密钥不能为空")
    private String appSecret;

    @Schema(description = "访问令牌", example = "token789")
    private String accessToken;

    @Schema(description = "寄件人姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotBlank(message = "寄件人姓名不能为空")
    private String senderName;

    @Schema(description = "寄件人电话", requiredMode = Schema.RequiredMode.REQUIRED, example = "13800138000")
    @NotBlank(message = "寄件人电话不能为空")
    private String senderPhone;

    @Schema(description = "寄件人详细地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京市朝阳区某某街道123号")
    @NotBlank(message = "寄件人详细地址不能为空")
    private String senderAddress;

    @Schema(description = "寄件人省份", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京市")
    @NotBlank(message = "寄件人省份不能为空")
    private String senderProvince;

    @Schema(description = "寄件人城市", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京市")
    @NotBlank(message = "寄件人城市不能为空")
    private String senderCity;

    @Schema(description = "寄件人区县", requiredMode = Schema.RequiredMode.REQUIRED, example = "朝阳区")
    @NotBlank(message = "寄件人区县不能为空")
    private String senderDistrict;

}
