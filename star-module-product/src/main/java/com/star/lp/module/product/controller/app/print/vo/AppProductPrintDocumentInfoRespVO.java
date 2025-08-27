package com.star.lp.module.product.controller.app.print.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 App - 文档信息 Response VO")
@Data
public class AppProductPrintDocumentInfoRespVO {

    @Schema(description = "文件名", requiredMode = Schema.RequiredMode.REQUIRED, example = "document.pdf")
    private String fileName;

    @Schema(description = "文件类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "pdf")
    private String fileType;

    @Schema(description = "文件大小", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024000")
    private Long fileSize;

    @Schema(description = "页数", example = "10")
    private Integer pageCount;

    @Schema(description = "是否支持打印", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean supportPrint;

    @Schema(description = "不支持原因", example = "文件格式不支持")
    private String unsupportedReason;

}
