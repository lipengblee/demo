// AppPrintSettingOptionRespVO.java
package com.star.lp.module.product.controller.app.print.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户 APP - 打印设置选项响应 VO")
public class AppPrintSettingOptionRespVO {

    @Schema(description = "选项编号", example = "1024")
    private Long id;

    @Schema(description = "选项名称", example = "纸型")
    private String name;

    @Schema(description = "排序，数字越小越靠前", example = "1")
    private Integer sort;

    @Schema(description = "备注", example = "打印纸张类型设置")
    private String remark;

//    @Schema(description = "创建时间")
//    private LocalDateTime createTime;

    @Schema(description = "选项值列表")
    private List<AppPrintSettingOptionValueRespVO> values;

}