package com.star.lp.module.product.controller.app.print.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 App - 更新打印文档 Request VO")
@Data
public class AppProductPrintDocumentUpdateReqVO {

    @Schema(description = "文档编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "文档编号不能为空")
    private Long id;

    @Schema(description = "文档名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "合同文件.pdf")
    @NotBlank(message = "文档名称不能为空")
    private String name;

}
