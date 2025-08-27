package com.star.lp.module.product.controller.admin.printconfig;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
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

import com.star.lp.module.product.controller.admin.printconfig.vo.*;
import com.star.lp.module.product.dal.dataobject.printconfig.PrintConfigDO;
import com.star.lp.module.product.service.printconfig.PrintConfigService;

@Tag(name = "管理后台 - 打印设置配置")
@RestController
@RequestMapping("/product/print-config")
@Validated
public class PrintConfigController {

    @Resource
    private PrintConfigService printConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建打印设置配置")
    @PreAuthorize("@ss.hasPermission('product:print-config:create')")
    public CommonResult<Long> createPrintConfig(@Valid @RequestBody PrintConfigSaveReqVO createReqVO) {
        return success(printConfigService.createPrintConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新打印设置配置")
    @PreAuthorize("@ss.hasPermission('product:print-config:update')")
    public CommonResult<Boolean> updatePrintConfig(@Valid @RequestBody PrintConfigSaveReqVO updateReqVO) {
        printConfigService.updatePrintConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除打印设置配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('product:print-config:delete')")
    public CommonResult<Boolean> deletePrintConfig(@RequestParam("id") Long id) {
        printConfigService.deletePrintConfig(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除打印设置配置")
                @PreAuthorize("@ss.hasPermission('product:print-config:delete')")
    public CommonResult<Boolean> deletePrintConfigList(@RequestParam("ids") List<Long> ids) {
        printConfigService.deletePrintConfigListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得打印设置配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('product:print-config:query')")
    public CommonResult<PrintConfigRespVO> getPrintConfig(@RequestParam("id") Long id) {
        PrintConfigDO printConfig = printConfigService.getPrintConfig(id);
        return success(BeanUtils.toBean(printConfig, PrintConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得打印设置配置分页")
    @PreAuthorize("@ss.hasPermission('product:print-config:query')")
    public CommonResult<PageResult<PrintConfigRespVO>> getPrintConfigPage(@Valid PrintConfigPageReqVO pageReqVO) {
        PageResult<PrintConfigDO> pageResult = printConfigService.getPrintConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PrintConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出打印设置配置 Excel")
    @PreAuthorize("@ss.hasPermission('product:print-config:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportPrintConfigExcel(@Valid PrintConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PrintConfigDO> list = printConfigService.getPrintConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "打印设置配置.xls", "数据", PrintConfigRespVO.class,
                        BeanUtils.toBean(list, PrintConfigRespVO.class));
    }

}