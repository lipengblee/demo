package com.star.lp.module.product.controller.app.print.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 App - 创建打印商品 Response VO")
@Data
public class AppProductPrintSpuCreateRespVO {
    @Schema(description = "SPU ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long spuId;

    @Schema(description = "SKU ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    private Long skuId;

    @Schema(description = "Category Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    private Long categoryId;
}
