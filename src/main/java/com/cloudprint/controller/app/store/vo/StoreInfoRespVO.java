package com.star.lp.module.trade.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import com.star.lp.framework.common.pojo.PageParam;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 门店信息响应
 */
@Data
@Schema(description = "门店信息响应")
public class StoreInfoRespVO {

    @Schema(description = "门店ID", example = "1")
    private Long id;

    @Schema(description = "门店名称", example = "星辰打印店")
    private String name;

    @Schema(description = "门店状态", example = "1")
    private Integer status;

    @Schema(description = "门店地址")
    private String address;

    @Schema(description = "详细地址")
    private String detailAddress;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "营业时间")
    private String businessHours;

    @Schema(description = "营业开始时间")
    private LocalTime openingTime;

    @Schema(description = "营业结束时间")
    private LocalTime closingTime;

    @Schema(description = "店长姓名")
    private String managerName;

    @Schema(description = "联系人")
    private String contact;

    @Schema(description = "门店简介")
    private String introduction;

    @Schema(description = "门店Logo")
    private String logo;

    @Schema(description = "区域编号")
    private Integer areaId;

    @Schema(description = "纬度")
    private Double latitude;

    @Schema(description = "经度")
    private Double longitude;
}

