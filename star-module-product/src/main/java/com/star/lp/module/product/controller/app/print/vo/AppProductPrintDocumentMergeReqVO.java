package com.star.lp.module.product.controller.app.print.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Schema(description = "用户 App - 文档合成PDF Request VO")
@Data
public class AppProductPrintDocumentMergeReqVO {

    @Schema(description = "要合成的文档编号列表", requiredMode = Schema.RequiredMode.REQUIRED, example = "[1,2,3]")
    @NotEmpty(message = "文档编号列表不能为空")
    private List<Long> documentIds;

    @Schema(description = "合成后的PDF名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "合并文档.pdf")
    @NotBlank(message = "PDF名称不能为空")
    private String pdfName;

}
