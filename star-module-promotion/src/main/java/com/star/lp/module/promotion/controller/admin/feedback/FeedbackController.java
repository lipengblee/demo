package com.star.lp.module.promotion.controller.admin.feedback;

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

import com.star.lp.module.promotion.controller.admin.feedback.vo.*;
import com.star.lp.module.promotion.dal.dataobject.feedback.FeedbackDO;
import com.star.lp.module.promotion.service.feedback.FeedbackService;

@Tag(name = "管理后台 - 意见反馈")
@RestController
@RequestMapping("/promotion/feedback")
@Validated
public class FeedbackController {

    @Resource
    private FeedbackService feedbackService;

    @PostMapping("/create")
    @Operation(summary = "创建意见反馈")
    @PreAuthorize("@ss.hasPermission('promotion:feedback:create')")
    public CommonResult<Long> createFeedback(@Valid @RequestBody FeedbackSaveReqVO createReqVO) {
        return success(feedbackService.createFeedback(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新意见反馈")
    @PreAuthorize("@ss.hasPermission('promotion:feedback:update')")
    public CommonResult<Boolean> updateFeedback(@Valid @RequestBody FeedbackSaveReqVO updateReqVO) {
        feedbackService.updateFeedback(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除意见反馈")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('promotion:feedback:delete')")
    public CommonResult<Boolean> deleteFeedback(@RequestParam("id") Long id) {
        feedbackService.deleteFeedback(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除意见反馈")
                @PreAuthorize("@ss.hasPermission('promotion:feedback:delete')")
    public CommonResult<Boolean> deleteFeedbackList(@RequestParam("ids") List<Long> ids) {
        feedbackService.deleteFeedbackListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得意见反馈")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('promotion:feedback:query')")
    public CommonResult<FeedbackRespVO> getFeedback(@RequestParam("id") Long id) {
        FeedbackDO feedback = feedbackService.getFeedback(id);
        return success(BeanUtils.toBean(feedback, FeedbackRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得意见反馈分页")
    @PreAuthorize("@ss.hasPermission('promotion:feedback:query')")
    public CommonResult<PageResult<FeedbackRespVO>> getFeedbackPage(@Valid FeedbackPageReqVO pageReqVO) {
        PageResult<FeedbackDO> pageResult = feedbackService.getFeedbackPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, FeedbackRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出意见反馈 Excel")
    @PreAuthorize("@ss.hasPermission('promotion:feedback:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFeedbackExcel(@Valid FeedbackPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<FeedbackDO> list = feedbackService.getFeedbackPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "意见反馈.xls", "数据", FeedbackRespVO.class,
                        BeanUtils.toBean(list, FeedbackRespVO.class));
    }

}