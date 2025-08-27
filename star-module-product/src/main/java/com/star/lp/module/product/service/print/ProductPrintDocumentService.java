package com.star.lp.module.product.service.print;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.product.controller.app.print.vo.*;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentDO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 打印文档 Service 接口
 *
 */
public interface ProductPrintDocumentService {

    /**
     * 上传打印文档
     *
     * @param userId 用户编号
     * @param file   文件
     * @param name   文档名称（可选）
     * @return 文档信息
     */
    ProductPrintDocumentDO uploadDocument(Long userId, MultipartFile file, String name);

    /**
     * 批量上传打印文档
     *
     * @param userId 用户编号
     * @param files  文件数组
     * @return 文档信息列表
     */
    List<ProductPrintDocumentDO> batchUploadDocuments(Long userId, MultipartFile[] files);

    /**
     * 获得打印文档分页
     *
     * @param pageReqVO 分页查询
     * @return 打印文档分页
     */
    PageResult<ProductPrintDocumentDO> getDocumentPage(AppProductPrintDocumentPageReqVO pageReqVO);

    /**
     * 获得打印文档
     *
     * @param userId 用户编号
     * @param id     文档编号
     * @return 打印文档
     */
    ProductPrintDocumentDO getDocument(Long userId, Long id);

    /**
     * 更新打印文档信息
     *
     * @param userId      用户编号
     * @param updateReqVO 更新信息
     */
    void updateDocument(Long userId, AppProductPrintDocumentUpdateReqVO updateReqVO);

    /**
     * 删除打印文档
     *
     * @param userId 用户编号
     * @param id     文档编号
     */
    void deleteDocument(Long userId, Long id);

    /**
     * 基于文档创建打印商品
     *
     * @param userId      用户编号
     * @param createReqVO 创建信息
     * @return SPU编号
     */
    AppProductPrintSpuCreateRespVO createPrintSpu(Long userId, AppProductPrintSpuCreateReqVO createReqVO);

    /**
     * 预览打印文档
     *
     * @param userId 用户编号
     * @param id     文档编号
     * @return 预览信息
     */
    AppProductPrintDocumentPreviewRespVO previewDocument(Long userId, Long id);

    /**
     * 获得打印选项配置
     *
     * @return 打印选项
     */
    AppProductPrintOptionsRespVO getPrintOptions();

    /**
     * 获得打印选项配置
     *
     * @return 打印选项
     */
    List<AppPrintSettingOptionRespVO> getPrintOptionsType();

    /**
     * 解析文档信息
     *
     * @param file 文件
     * @return 文档信息
     */
    AppProductPrintDocumentInfoRespVO parseDocumentInfo(MultipartFile file);

    /**
     * 校验文档是否属于用户
     *
     * @param userId 用户编号
     * @param id     文档编号
     * @return 文档信息
     */
    ProductPrintDocumentDO validateDocumentOwner(Long userId, Long id);

}