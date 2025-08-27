package com.star.lp.module.product.controller.app.print.vo;

import com.star.lp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "用户 App - 打印文档分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppProductPrintDocumentPageReqVO extends PageParam {

    @Schema(description = "用户编号（内部设置，前端无需传递）", hidden = true)
    private Long userId;

    @Schema(description = "文档名称", example = "合同")
    private String name;

    @Schema(description = "文件类型", example = "pdf")
    private String fileType;

    @Schema(description = "状态", example = "1")
    private Integer status;

}
