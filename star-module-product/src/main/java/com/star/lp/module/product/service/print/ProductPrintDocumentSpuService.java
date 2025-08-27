package com.star.lp.module.product.service.print;

import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentDO;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentSpuDO;

import java.util.List;

/**
 * 打印文档与SPU关联 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductPrintDocumentSpuService {

    /**
     * 创建文档与SPU的关联
     *
     * @param spuId       SPU编号
     * @param documentIds 文档编号列表
     */
    void createDocumentSpuRelations(Long spuId, List<Long> documentIds);

    /**
     * 更新文档与SPU的关联
     *
     * @param spuId       SPU编号
     * @param documentIds 文档编号列表
     */
    void updateDocumentSpuRelations(Long spuId, List<Long> documentIds);

    /**
     * 删除SPU的所有文档关联
     *
     * @param spuId SPU编号
     */
    void deleteBySpuId(Long spuId);

    /**
     * 根据SPU ID查询关联的文档
     *
     * @param spuId SPU编号
     * @return 文档列表
     */
    List<ProductPrintDocumentDO> getDocumentsBySpuId(Long spuId);

    /**
     * 根据文档ID查询关联的SPU列表
     *
     * @param documentId 文档编号
     * @return SPU编号列表
     */
    List<Long> getSpuIdsByDocumentId(Long documentId);

    /**
     * 删除文档的所有SPU关联
     *
     * @param documentId 文档编号
     */
    void deleteByDocumentId(Long documentId);

    /**
     * 检查文档是否被任何SPU使用
     *
     * @param documentId 文档编号
     * @return 是否被使用
     */
    boolean isDocumentUsedBySpu(Long documentId);

    /**
     * 检查文档是否可以删除（未被任何有效订单使用）
     *
     * @param documentId 文档编号
     * @param userId     用户编号
     * @return 是否可以删除
     */
    boolean canDeleteDocument(Long documentId, Long userId);

    /**
     * 获取SPU的所有关联记录
     *
     * @param spuId SPU编号
     * @return 关联记录列表
     */
    List<ProductPrintDocumentSpuDO> getRelationsBySpuId(Long spuId);

    /**
     * 批量检查文档的使用状态
     *
     * @param documentIds 文档编号列表
     * @return 文档ID -> 是否被使用的映射
     */
    java.util.Map<Long, Boolean> batchCheckDocumentUsage(List<Long> documentIds);

}
