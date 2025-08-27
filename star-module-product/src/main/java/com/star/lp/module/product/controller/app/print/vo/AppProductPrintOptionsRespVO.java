package com.star.lp.module.product.controller.app.print.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 App - 打印选项配置 Response VO")
@Data
public class AppProductPrintOptionsRespVO {

    @Schema(description = "纸张尺寸选项", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<PrintOption> paperSizes;

    @Schema(description = "打印质量选项", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<PrintOption> qualities;

    @Schema(description = "颜色模式选项", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<PrintOption> colorModes;

    @Schema(description = "双面打印选项", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<PrintOption> duplexOptions;

    @Schema(description = "装订选项")
    private List<PrintOption> bindingOptions;

    @Schema(description = "打印选项")
    @Data
    public static class PrintOption {

        @Schema(description = "属性ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private Long propertyId;

        @Schema(description = "属性名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "纸张尺寸")
        private String propertyName;

        @Schema(description = "选项值ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private Long valueId;

        @Schema(description = "选项值名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "A4")
        private String valueName;

        @Schema(description = "基础价格（分）", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
        private Integer basePrice;

        @Schema(description = "每页额外价格（分）", example = "10")
        private Integer pricePerPage;

        @Schema(description = "是否默认选择", example = "true")
        private Boolean isDefault;

    }

}
