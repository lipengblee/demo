package com.star.lp.module.promotion.controller.admin.feedback.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 意见反馈新增/修改 Request VO")
@Data
public class FeedbackSaveReqVO {

    @Schema(description = "反馈ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18891")
    private Long id;

    @Schema(description = "用户ID", example = "27654")
    private Long userId;

    @Schema(description = "反馈类型：bug-功能异常，suggestion-功能建议，ui-界面问题，performance-性能问题，other-其他问题", requiredMode = Schema.RequiredMode.REQUIRED, example = "bug")
    @NotEmpty(message = "反馈类型：bug-功能异常，suggestion-功能建议，ui-界面问题，performance-性能问题，other-其他问题不能为空")
    private String type;

    @Schema(description = "问题描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "随便")
    @NotEmpty(message = "问题描述不能为空")
    private String description;

    @Schema(description = "联系手机号")
    private String contactPhone;

    @Schema(description = "联系邮箱")
    private String contactEmail;

    @Schema(description = "图片列表JSON格式：[\"url1\", \"url2\"]")
    private String images;

    @Schema(description = "处理状态：1-待处理，2-已处理，3-已关闭", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "处理状态：1-待处理，2-已处理，3-已关闭不能为空")
    private Integer status;

    @Schema(description = "管理员回复")
    private String adminReply;

    @Schema(description = "回复时间")
    private LocalDateTime replyTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "创建时间不能为空")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "更新时间不能为空")
    private LocalDateTime updateTime;

}