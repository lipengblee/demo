package cn.iocoder.yudao.module.trade.controller.admin.settingoption.vo;

import cn.iocoder.yudao.module.trade.dal.dataobject.settingoption.SettingOptionValueDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 打印设置选项新增/修改 Request VO")
@Data
public class SettingOptionSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20933")
    private Long id;

    @Schema(description = "选项名称（如：纸型、色彩）", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "选项名称（如：纸型、色彩）不能为空")
    private String name;

    @Schema(description = "排序（数字越小越靠前）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序（数字越小越靠前）不能为空")
    private Integer sort;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "打印设置选项值列表")
    private List<SettingOptionValueDO> settingOptionValues;

}