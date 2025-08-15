package cn.iocoder.yudao.module.trade.controller.app.print.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户 APP - 打印设置选项分页请求 VO")
public class AppPrintSettingOptionPageReqVO extends PageParam {

    @Schema(description = "选项名称，模糊匹配", example = "纸型")
    private String name;

    @Schema(description = "排序方式，asc/desc", example = "asc")
    private String sortOrder;

}