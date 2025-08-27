package com.star.lp.module.product.controller.app.print.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Schema(description = "用户 App - 文档统计信息 Response VO")
@Data
public class AppProductPrintDocumentStatisticsRespVO {

    @Schema(description = "文档总数", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long totalCount;

    @Schema(description = "处理中文档数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Long processingCount;

    @Schema(description = "已使用文档数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "30")
    private Long usedCount;

    @Schema(description = "未使用文档数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "70")
    private Long unusedCount;

    @Schema(description = "文档总大小（字节）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024000")
    private Long totalSize;

    @Schema(description = "文件类型分布", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, Long> fileTypeDistribution;

}
