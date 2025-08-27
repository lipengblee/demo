package com.star.lp.module.product.controller.admin.printconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 打印设置配置 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PrintConfigRespVO {

    @Schema(description = "配置编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1500")
    @ExcelProperty("配置编号")
    private Long id;

    @Schema(description = "配置键", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("配置键")
    private String configKey;

    @Schema(description = "配置值", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("配置值")
    private String configValue;

    @Schema(description = "配置描述")
    @ExcelProperty("配置描述")
    private String configDesc;

    @Schema(description = "状态：1-启用 0-禁用", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态：1-启用 0-禁用")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}