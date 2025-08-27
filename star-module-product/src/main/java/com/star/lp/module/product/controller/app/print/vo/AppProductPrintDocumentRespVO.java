package com.star.lp.module.product.controller.app.print.vo;

import com.star.lp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

// ========== Response VO ==========

@Schema(description = "用户 App - 打印文档 Response VO")
@Data
public class AppProductPrintDocumentRespVO {

    @Schema(description = "文档编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long userId;

    @Schema(description = "文档名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "合同文件.pdf")
    private String name;

    @Schema(description = "原始文件名", requiredMode = Schema.RequiredMode.REQUIRED, example = "contract.pdf")
    private String originalName;

    @Schema(description = "文件类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "pdf")
    private String fileType;

    @Schema(description = "文件大小（字节）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024000")
    private Long fileSize;

    @Schema(description = "文件URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "http://example.com/file.pdf")
    private String fileUrl;

    @Schema(description = "缩略图URL", example = "http://example.com/thumb.jpg")
    private String thumbnailUrl;

    @Schema(description = "页数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer pageCount;

    @Schema(description = "文档状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}

// ========== Page Request VO ==========

// ========== Update Request VO ==========

// ========== Create Print SPU Request VO ==========

// ========== Document Preview Response VO ==========

// ========== Print Options Response VO ==========

// ========== Document Info Response VO ==========

