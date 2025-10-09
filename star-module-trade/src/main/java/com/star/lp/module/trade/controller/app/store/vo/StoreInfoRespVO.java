package com.star.lp.module.trade.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import com.star.lp.framework.common.pojo.PageParam;

import java.time.LocalDateTime;
import java.util.List;

// =============== 响应 VO ===============

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
    private Integer status; // 1-营业中, 0-休息中

    @Schema(description = "门店地址")
    private String address;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "营业时间")
    private String businessHours;

    @Schema(description = "店长姓名")
    private String managerName;
}

// =============== 请求 VO ===============

