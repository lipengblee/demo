package com.star.lp.module.trade.controller.app.print;

import com.star.lp.framework.common.pojo.CommonResult;
import com.star.lp.module.trade.controller.app.print.vo.AppPrintSettingOptionRespVO;
import com.star.lp.module.trade.convert.print.SettingOptionConvert;
import com.star.lp.module.trade.dal.dataobject.settingoption.SettingOptionDO;
import com.star.lp.module.trade.dal.dataobject.settingoption.SettingOptionValueDO;
import com.star.lp.module.trade.service.settingoption.SettingOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.star.lp.framework.common.pojo.CommonResult.success;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "用户 App - 打印设置选项")
@RestController
@RequestMapping("/trade/print/setting-option")
@Validated
public class AppPrintSettingController {

    @Resource
    private SettingOptionService settingOptionService;

    @GetMapping("/list-all")
    @Operation(summary = "获得所有打印设置选项（含选项值）")
    @PermitAll // 允许未登录访问，打印设置通常是公开配置
    public CommonResult<List<AppPrintSettingOptionRespVO>> getAllSettingOptions() {

        List<SettingOptionDO> optionList = settingOptionService.getAllSettingOptions();
        if (optionList.isEmpty()) {
            return success(Collections.emptyList());
        }

        // 批量查询所有选项值（一次性查询，避免循环调用 DAO）
        List<Long> optionIds = optionList.stream()
                .map(SettingOptionDO::getId)
                .collect(Collectors.toList());

        List<SettingOptionValueDO> allValueList = settingOptionService.getSettingOptionValueListByOptionIds(optionIds);

        // 转换为 VO 列表
        List<AppPrintSettingOptionRespVO> result = SettingOptionConvert.INSTANCE.convertList(optionList, allValueList);
        return success(result);
    }

}
