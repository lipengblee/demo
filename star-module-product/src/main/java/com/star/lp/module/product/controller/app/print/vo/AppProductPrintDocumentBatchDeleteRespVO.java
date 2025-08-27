package com.star.lp.module.product.controller.app.print.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 App - 批量删除文档 Response VO")
@Data
public class AppProductPrintDocumentBatchDeleteRespVO {

    @Schema(description = "成功删除的文档编号列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> successIds;

    @Schema(description = "删除失败的文档信息列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<FailedItem> failedItems;

    @Schema(description = "成功数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer successCount;

    @Schema(description = "失败数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer failedCount;

    @Schema(description = "失败项")
    @Data
    public static class FailedItem {

        @Schema(description = "文档编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private Long documentId;

        @Schema(description = "失败原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "文档正在被订单使用")
        private String reason;

    }

}