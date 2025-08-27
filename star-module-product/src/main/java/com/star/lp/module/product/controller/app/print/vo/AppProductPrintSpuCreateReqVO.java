package com.star.lp.module.product.controller.app.print.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 App - 创建打印商品 Request VO")
@Data
public class AppProductPrintSpuCreateReqVO {

    @Schema(description = "文档编号数组", requiredMode = Schema.RequiredMode.REQUIRED, example = "[1,2,3]")
    @NotEmpty(message = "文档编号数组不能为空")
    private List<Long> documentIds;

    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "打印文档包")
    @NotBlank(message = "商品名称不能为空")
    private String spuName;

    @Schema(description = "商品简介", example = "包含多个打印文档的商品")
    private String introduction;

    @Schema(description = "打印选项配置", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "打印选项不能为空")
    private List<PrintOption> printOptions;

    @Schema(description = "打印选项")
    @Data
    public static class PrintOption {

        @Schema(description = "属性ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @NotNull(message = "属性ID不能为空")
        private Long propertyId;

        @Schema(description = "属性值ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @NotNull(message = "属性值ID不能为空")
        private Long valueId;

        @Schema(description = "额外价格（分）", example = "100")
        private Integer extraPrice;

    }

}
