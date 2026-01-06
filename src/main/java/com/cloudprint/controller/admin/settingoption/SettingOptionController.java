package com.star.lp.module.trade.controller.admin.settingoption;

import com.star.lp.module.trade.controller.admin.settingoption.vo.SettingOptionPageReqVO;
import com.star.lp.module.trade.controller.admin.settingoption.vo.SettingOptionRespVO;
import com.star.lp.module.trade.controller.admin.settingoption.vo.SettingOptionSaveReqVO;
import com.star.lp.module.trade.dal.dataobject.settingoption.SettingOptionDO;
import com.star.lp.module.trade.dal.dataobject.settingoption.SettingOptionValueDO;
import com.star.lp.module.trade.service.settingoption.SettingOptionService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import com.star.lp.framework.common.pojo.PageParam;
import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.common.pojo.CommonResult;
import com.star.lp.framework.common.util.object.BeanUtils;
import static com.star.lp.framework.common.pojo.CommonResult.success;

import com.star.lp.framework.excel.core.util.ExcelUtils;

import com.star.lp.framework.apilog.core.annotation.ApiAccessLog;
import static com.star.lp.framework.apilog.core.enums.OperateTypeEnum.*;


/*
 * 打印设置选项 - 以废弃，不再使用
 */
@Tag(name = "管理后台 - 打印设置选项")
@RestController
@RequestMapping("/print/setting-option")
@Validated
public class SettingOptionController {

    @Resource
    private SettingOptionService settingOptionService;

    @PostMapping("/create")
    @Operation(summary = "创建打印设置选项")
    @PreAuthorize("@ss.hasPermission('print:setting-option:create')")
    public CommonResult<Long> createSettingOption(@Valid @RequestBody SettingOptionSaveReqVO createReqVO) {
        return success(settingOptionService.createSettingOption(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新打印设置选项")
    @PreAuthorize("@ss.hasPermission('print:setting-option:update')")
    public CommonResult<Boolean> updateSettingOption(@Valid @RequestBody SettingOptionSaveReqVO updateReqVO) {
        settingOptionService.updateSettingOption(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除打印设置选项")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('print:setting-option:delete')")
    public CommonResult<Boolean> deleteSettingOption(@RequestParam("id") Long id) {
        settingOptionService.deleteSettingOption(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除打印设置选项")
                @PreAuthorize("@ss.hasPermission('print:setting-option:delete')")
    public CommonResult<Boolean> deleteSettingOptionList(@RequestParam("ids") List<Long> ids) {
        settingOptionService.deleteSettingOptionListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得打印设置选项")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('print:setting-option:query')")
    public CommonResult<SettingOptionRespVO> getSettingOption(@RequestParam("id") Long id) {
        SettingOptionDO settingOption = settingOptionService.getSettingOption(id);
        return success(BeanUtils.toBean(settingOption, SettingOptionRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得打印设置选项分页")
    @PreAuthorize("@ss.hasPermission('print:setting-option:query')")
    public CommonResult<PageResult<SettingOptionRespVO>> getSettingOptionPage(@Valid SettingOptionPageReqVO pageReqVO) {
        PageResult<SettingOptionDO> pageResult = settingOptionService.getSettingOptionPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SettingOptionRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出打印设置选项 Excel")
    @PreAuthorize("@ss.hasPermission('print:setting-option:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSettingOptionExcel(@Valid SettingOptionPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SettingOptionDO> list = settingOptionService.getSettingOptionPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "打印设置选项.xls", "数据", SettingOptionRespVO.class,
                        BeanUtils.toBean(list, SettingOptionRespVO.class));
    }

    // ==================== 子表（打印设置选项值） ====================

    @GetMapping("/setting-option-value/list-by-option-id")
    @Operation(summary = "获得打印设置选项值列表")
    @Parameter(name = "optionId", description = "关联选项ID（print_setting_option.id）")
    @PreAuthorize("@ss.hasPermission('print:setting-option:query')")
    public CommonResult<List<SettingOptionValueDO>> getSettingOptionValueListByOptionId(@RequestParam("optionId") Long optionId) {
        return success(settingOptionService.getSettingOptionValueListByOptionId(optionId));
    }

}