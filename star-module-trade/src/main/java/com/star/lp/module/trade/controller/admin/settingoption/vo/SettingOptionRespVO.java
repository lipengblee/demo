package com.star.lp.module.trade.controller.admin.settingoption.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 打印设置选项 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SettingOptionRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20933")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "选项名称（如：纸型、色彩）", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("选项名称（如：纸型、色彩）")
    private String name;

    @Schema(description = "排序（数字越小越靠前）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("排序（数字越小越靠前）")
    private Integer sort;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}