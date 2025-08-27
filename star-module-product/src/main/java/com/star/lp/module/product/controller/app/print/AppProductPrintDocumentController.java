package com.star.lp.module.product.controller.app.print;

import com.star.lp.framework.common.pojo.CommonResult;
import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.common.util.object.BeanUtils;
import com.star.lp.module.product.controller.app.print.vo.*;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentDO;
import com.star.lp.module.product.dal.mysql.print.ProductPrintDocumentMapper;
import com.star.lp.module.product.service.print.ProductPrintDocumentService;
import com.star.lp.module.product.service.print.ProductPrintDocumentSpuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.star.lp.framework.common.pojo.CommonResult.success;
import static com.star.lp.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 打印文档管理")
@RestController
@RequestMapping("/product/print-document")
@Validated
public class AppProductPrintDocumentController {

    @Resource
    private ProductPrintDocumentService printDocumentService;

    @Resource
    private ProductPrintDocumentMapper printDocumentMapper;

    @Resource
    private ProductPrintDocumentSpuService productPrintDocumentSpuService;


    @PostMapping("/upload")
    @Operation(summary = "上传打印文档")
    public CommonResult<AppProductPrintDocumentRespVO> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "name", required = false) String name) {
        ProductPrintDocumentDO document = printDocumentService.uploadDocument(getLoginUserId(), file, name);
        return success(BeanUtils.toBean(document, AppProductPrintDocumentRespVO.class));
    }

    @PostMapping("/batch-upload")
    @Operation(summary = "批量上传打印文档")
    public CommonResult<List<AppProductPrintDocumentRespVO>> batchUploadDocuments(
            @RequestParam("files") MultipartFile[] files) {
        List<ProductPrintDocumentDO> documents = printDocumentService.batchUploadDocuments(getLoginUserId(), files);
        return success(BeanUtils.toBean(documents, AppProductPrintDocumentRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得打印文档分页")
    public CommonResult<PageResult<AppProductPrintDocumentRespVO>> getDocumentPage(@Valid AppProductPrintDocumentPageReqVO pageVO) {
        pageVO.setUserId(getLoginUserId()); // 只查询当前用户的文档
        PageResult<ProductPrintDocumentDO> pageResult = printDocumentService.getDocumentPage(pageVO);
        return success(BeanUtils.toBean(pageResult, AppProductPrintDocumentRespVO.class));
    }

    @GetMapping("/get")
    @Operation(summary = "获得打印文档详情")
    @Parameter(name = "id", description = "文档编号", required = true)
    public CommonResult<AppProductPrintDocumentRespVO> getDocument(@RequestParam("id") Long id) {
        ProductPrintDocumentDO document = printDocumentService.getDocument(getLoginUserId(), id);
        return success(BeanUtils.toBean(document, AppProductPrintDocumentRespVO.class));
    }

    @PutMapping("/update")
    @Operation(summary = "更新打印文档信息")
    public CommonResult<Boolean> updateDocument(@Valid @RequestBody AppProductPrintDocumentUpdateReqVO updateReqVO) {
        printDocumentService.updateDocument(getLoginUserId(), updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除打印文档")
    @Parameter(name = "id", description = "文档编号", required = true)
    public CommonResult<Boolean> deleteDocument(@RequestParam("id") Long id) {
        printDocumentService.deleteDocument(getLoginUserId(), id);
        return success(true);
    }

    @PostMapping("/create-spu")
    @Operation(summary = "基于文档创建打印商品文档")
    public CommonResult<AppProductPrintSpuCreateRespVO> createPrintSpu(@Valid @RequestBody AppProductPrintSpuCreateReqVO createReqVO) {
        AppProductPrintSpuCreateRespVO res = printDocumentService.createPrintSpu(getLoginUserId(), createReqVO);
        return success(res);
    }

    @GetMapping("/preview")
    @Operation(summary = "预览打印文档")
    @Parameter(name = "id", description = "文档编号", required = true)
    public CommonResult<AppProductPrintDocumentPreviewRespVO> previewDocument(@RequestParam("id") Long id) {
        AppProductPrintDocumentPreviewRespVO preview = printDocumentService.previewDocument(getLoginUserId(), id);
        return success(preview);
    }

    @GetMapping("/get-print-options")
    @Operation(summary = "获得打印选项配置-基础版本")
    public CommonResult<AppProductPrintOptionsRespVO> getPrintOptions() {
        AppProductPrintOptionsRespVO options = printDocumentService.getPrintOptions();
        return success(options);
    }

    @GetMapping("/get-print-options-type")
    @Operation(summary = "获得打印选项配置")
    public CommonResult<List<AppPrintSettingOptionRespVO>> getPrintOptionsType() {
        List<AppPrintSettingOptionRespVO> options = printDocumentService.getPrintOptionsType();
        return success(options);
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除打印文档")
    public CommonResult<AppProductPrintDocumentBatchDeleteRespVO> batchDeleteDocuments(
            @Valid @RequestBody AppProductPrintDocumentBatchDeleteReqVO batchDeleteReqVO) {
        Long userId = getLoginUserId();
        List<Long> documentIds = batchDeleteReqVO.getDocumentIds();

        AppProductPrintDocumentBatchDeleteRespVO result = new AppProductPrintDocumentBatchDeleteRespVO();
        List<Long> successIds = new ArrayList<>();
        List<AppProductPrintDocumentBatchDeleteRespVO.FailedItem> failedItems = new ArrayList<>();

        for (Long documentId : documentIds) {
            try {
                printDocumentService.deleteDocument(userId, documentId);
                successIds.add(documentId);
            } catch (Exception e) {
                AppProductPrintDocumentBatchDeleteRespVO.FailedItem failedItem =
                        new AppProductPrintDocumentBatchDeleteRespVO.FailedItem();
                failedItem.setDocumentId(documentId);
                failedItem.setReason(e.getMessage());
                failedItems.add(failedItem);
            }
        }

        result.setSuccessIds(successIds);
        result.setFailedItems(failedItems);
        result.setSuccessCount(successIds.size());
        result.setFailedCount(failedItems.size());

        return success(result);
    }

    @GetMapping("/get-statistics")
    @Operation(summary = "获取用户文档统计信息")
    public CommonResult<AppProductPrintDocumentStatisticsRespVO> getDocumentStatistics() {
        Long userId = getLoginUserId();

        // 统计文档总数
        Long totalCount = printDocumentMapper.countByUserId(userId);

        // 统计处理中文档数量
        Long processingCount = printDocumentMapper.countProcessingByUserId(userId);

        // 统计被使用的文档数量
        AppProductPrintDocumentPageReqVO pageReqVO = new AppProductPrintDocumentPageReqVO();
        pageReqVO.setUserId(userId);
        pageReqVO.setPageNo(1);
        pageReqVO.setPageSize(Integer.MAX_VALUE);
        PageResult<ProductPrintDocumentDO> allDocuments = printDocumentService.getDocumentPage(pageReqVO);

        List<Long> allDocumentIds = allDocuments.getList().stream()
                .map(ProductPrintDocumentDO::getId)
                .collect(Collectors.toList());

        Map<Long, Boolean> usageStatus = productPrintDocumentSpuService.batchCheckDocumentUsage(allDocumentIds);
        long usedCount = usageStatus.values().stream().mapToLong(used -> used ? 1 : 0).sum();

        // 统计文件大小
        long totalSize = allDocuments.getList().stream()
                .mapToLong(ProductPrintDocumentDO::getFileSize)
                .sum();

        // 统计文件类型分布
        Map<String, Long> fileTypeDistribution = allDocuments.getList().stream()
                .collect(Collectors.groupingBy(ProductPrintDocumentDO::getFileType,
                        Collectors.counting()));

        AppProductPrintDocumentStatisticsRespVO statistics = new AppProductPrintDocumentStatisticsRespVO();
        statistics.setTotalCount(totalCount);
        statistics.setProcessingCount(processingCount);
        statistics.setUsedCount(usedCount);
        statistics.setUnusedCount(totalCount - usedCount);
        statistics.setTotalSize(totalSize);
        statistics.setFileTypeDistribution(fileTypeDistribution);

        return success(statistics);
    }

}