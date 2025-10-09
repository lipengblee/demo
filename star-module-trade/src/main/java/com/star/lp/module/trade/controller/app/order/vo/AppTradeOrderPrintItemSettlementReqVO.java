package com.star.lp.module.trade.controller.app.order.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * 打印项目结算请求VO
 */
@Data
@Schema(description = "打印项目结算请求")
public class AppTradeOrderPrintItemSettlementReqVO {

    @Schema(description = "文档ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "文档ID不能为空")
    private Long documentId;

    @Schema(description = "打印份数", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "打印份数不能为空")
    @Min(value = 1, message = "打印份数必须大于0")
    private Integer copies;

    @Schema(description = "打印颜色：1-黑白，2-彩色", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "打印颜色不能为空")
    private Integer colorType;

    @Schema(description = "纸张规格：1-A4，2-A3", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "纸张规格不能为空")
    private Integer paperSize;

    @Schema(description = "打印方式：1-单面，2-双面", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "打印方式不能为空")
    private Integer printSide;

    @Schema(description = "打印页数范围", example = "1-5,10-15")
    private String pageRange;

}