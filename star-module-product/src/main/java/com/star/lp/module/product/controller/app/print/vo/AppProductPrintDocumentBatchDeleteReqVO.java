package com.star.lp.module.product.controller.app.print.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 App - 批量删除文档 Request VO")
@Data
public class AppProductPrintDocumentBatchDeleteReqVO {

    @Schema(description = "文档编号列表", requiredMode = Schema.RequiredMode.REQUIRED, example = "[1,2,3]")
    @NotEmpty(message = "文档编号列表不能为空")
    private List<Long> documentIds;

}
