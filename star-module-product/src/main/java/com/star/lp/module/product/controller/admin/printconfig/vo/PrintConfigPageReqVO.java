package com.star.lp.module.product.controller.admin.printconfig.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.star.lp.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.star.lp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 打印设置配置分页 Request VO")
@Data
public class PrintConfigPageReqVO extends PageParam {

    @Schema(description = "配置键")
    private String configKey;

    @Schema(description = "配置值")
    private String configValue;

    @Schema(description = "配置描述")
    private String configDesc;

    @Schema(description = "状态：1-启用 0-禁用", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}