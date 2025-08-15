// AppPrintSettingOptionValueRespVO.java
package cn.iocoder.yudao.module.trade.controller.app.print.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "用户 APP - 打印设置选项值响应 VO")
public class AppPrintSettingOptionValueRespVO {

    @Schema(description = "选项值编号", example = "2048")
    private Long id;

    @Schema(description = "关联选项编号", example = "1024")
    private Long optionId;

    @Schema(description = "选项值", example = "A4")
    private String value;

    @Schema(description = "价格（元）", example = "1.50")
    private BigDecimal price;

    @Schema(description = "排序，数字越小越靠前", example = "1")
    private Integer sort;

    @Schema(description = "备注", example = "标准A4纸张")
    private String remark;

}