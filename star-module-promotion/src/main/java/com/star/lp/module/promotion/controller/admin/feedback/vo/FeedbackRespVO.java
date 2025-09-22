package com.star.lp.module.promotion.controller.admin.feedback.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;
import com.star.lp.framework.excel.core.annotations.DictFormat;
import com.star.lp.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 意见反馈 Response VO")
@Data
@ExcelIgnoreUnannotated
public class FeedbackRespVO {

    @Schema(description = "反馈ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18891")
    @ExcelProperty("反馈ID")
    private Long id;

    @Schema(description = "用户ID", example = "27654")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "反馈类型：bug-功能异常，suggestion-功能建议，ui-界面问题，performance-性能问题，other-其他问题", requiredMode = Schema.RequiredMode.REQUIRED, example = "bug")
    @ExcelProperty(value = "反馈类型：bug-功能异常，suggestion-功能建议，ui-界面问题，performance-性能问题，other-其他问题", converter = DictConvert.class)
    @DictFormat("feedback_types") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String type;

    @Schema(description = "问题描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "随便")
    @ExcelProperty("问题描述")
    private String description;

    @Schema(description = "联系手机号")
    @ExcelProperty("联系手机号")
    private String contactPhone;

    @Schema(description = "联系邮箱")
    @ExcelProperty("联系邮箱")
    private String contactEmail;

    @Schema(description = "图片列表JSON格式：[\"url1\", \"url2\"]")
    @ExcelProperty("图片列表JSON格式：[\"url1\", \"url2\"]")
    private String images;

    @Schema(description = "处理状态：1-待处理，2-已处理，3-已关闭", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "处理状态：1-待处理，2-已处理，3-已关闭", converter = DictConvert.class)
    @DictFormat("feedback_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "管理员回复")
    @ExcelProperty("管理员回复")
    private String adminReply;

    @Schema(description = "回复时间")
    @ExcelProperty("回复时间")
    private LocalDateTime replyTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

}