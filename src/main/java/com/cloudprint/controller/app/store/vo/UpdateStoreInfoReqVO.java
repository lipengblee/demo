package com.star.lp.module.trade.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalTime;

/**
 * 更新门店信息请求
 */
@Data
@Schema(description = "更新门店信息请求")
public class UpdateStoreInfoReqVO {

    @Schema(description = "门店名称", required = true, example = "星辰打印店")
    @NotBlank(message = "门店名称不能为空")
    @Length(max = 64, message = "门店名称长度不能超过64个字符")
    private String name;

    @Schema(description = "门店简介", example = "专业打印服务")
    @Length(max = 256, message = "门店简介长度不能超过256个字符")
    private String introduction;

    @Schema(description = "联系电话", required = true, example = "13800138000")
    @NotBlank(message = "联系电话不能为空")
    @Length(max = 16, message = "联系电话长度不能超过16个字符")
    private String phone;

    @Schema(description = "区域编号", required = true, example = "110101")
    @NotNull(message = "区域编号不能为空")
    private Integer areaId;

    @Schema(description = "详细地址", required = true, example = "xx街道xx号")
    @NotBlank(message = "详细地址不能为空")
    @Length(max = 256, message = "详细地址长度不能超过256个字符")
    private String detailAddress;

    @Schema(description = "门店Logo", required = true)
    @NotBlank(message = "门店Logo不能为空")
    @Length(max = 256, message = "Logo地址长度不能超过256个字符")
    private String logo;

    @Schema(description = "营业开始时间", required = true, example = "09:00:00")
    @NotNull(message = "营业开始时间不能为空")
    private LocalTime openingTime;

    @Schema(description = "营业结束时间", required = true, example = "18:00:00")
    @NotNull(message = "营业结束时间不能为空")
    private LocalTime closingTime;

    @Schema(description = "纬度", required = true, example = "39.9042")
    @NotNull(message = "纬度不能为空")
    private Double latitude;

    @Schema(description = "经度", required = true, example = "116.4074")
    @NotNull(message = "经度不能为空")
    private Double longitude;

    @Schema(description = "联系人", example = "张三")
    @Length(max = 100, message = "联系人长度不能超过100个字符")
    private String contact;

    @Schema(description = "门店状态", example = "1")
    private Integer status;
}