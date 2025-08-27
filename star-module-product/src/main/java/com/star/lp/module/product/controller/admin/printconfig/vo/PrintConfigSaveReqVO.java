package com.star.lp.module.product.controller.admin.printconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 打印设置配置新增/修改 Request VO")
@Data
public class PrintConfigSaveReqVO {

    @Schema(description = "配置编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1500")
    private Long id;

    @Schema(description = "配置键", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "配置键不能为空")
    private String configKey;

    @Schema(description = "配置值", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "配置值不能为空")
    private String configValue;

    @Schema(description = "配置描述")
    private String configDesc;

    @Schema(description = "状态：1-启用 0-禁用", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态：1-启用 0-禁用不能为空")
    private Integer status;

}