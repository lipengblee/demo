package com.star.lp.module.promotion.controller.admin.feedback.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.star.lp.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.star.lp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 意见反馈分页 Request VO")
@Data
public class FeedbackPageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "27654")
    private Long userId;

    @Schema(description = "反馈类型：bug-功能异常，suggestion-功能建议，ui-界面问题，performance-性能问题，other-其他问题", example = "bug")
    private String type;

    @Schema(description = "联系手机号")
    private String contactPhone;

    @Schema(description = "处理状态：1-待处理，2-已处理，3-已关闭", example = "1")
    private Integer status;

    @Schema(description = "更新时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] updateTime;

}