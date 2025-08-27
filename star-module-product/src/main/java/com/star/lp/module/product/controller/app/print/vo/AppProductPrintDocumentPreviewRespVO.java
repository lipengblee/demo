package com.star.lp.module.product.controller.app.print.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 App - 文档预览 Response VO")
@Data
public class AppProductPrintDocumentPreviewRespVO {

    @Schema(description = "文档编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "文档名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "合同文件.pdf")
    private String name;

    @Schema(description = "预览图片URL数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> previewUrls;

    @Schema(description = "页数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer pageCount;

    @Schema(description = "文件大小（字节）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024000")
    private Long fileSize;

    @Schema(description = "建议打印设置")
    private PrintSetting suggestedSetting;

    @Schema(description = "打印设置")
    @Data
    public static class PrintSetting {

        @Schema(description = "纸张尺寸", example = "A4")
        private String paperSize;

        @Schema(description = "打印方向", example = "portrait")
        private String orientation;

        @Schema(description = "打印质量", example = "high")
        private String quality;

        @Schema(description = "颜色模式", example = "color")
        private String colorMode;

        @Schema(description = "是否双面打印", example = "true")
        private Boolean duplex;

    }

}
