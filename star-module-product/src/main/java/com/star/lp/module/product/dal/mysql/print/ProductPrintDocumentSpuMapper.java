package com.star.lp.module.product.dal.mysql.print;

import com.star.lp.framework.mybatis.core.mapper.BaseMapperX;
import com.star.lp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentSpuDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 打印文档与商品SPU关联 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductPrintDocumentSpuMapper extends BaseMapperX<ProductPrintDocumentSpuDO> {

    /**
     * 根据SPU ID查询关联的文档
     *
     * @param spuId SPU编号
     * @return 关联记录列表
     */
    default List<ProductPrintDocumentSpuDO> selectBySpuId(Long spuId) {
        return selectList(new LambdaQueryWrapperX<ProductPrintDocumentSpuDO>()
                .eq(ProductPrintDocumentSpuDO::getSpuId, spuId)
                .orderByAsc(ProductPrintDocumentSpuDO::getSort)
                .orderByAsc(ProductPrintDocumentSpuDO::getId));
    }

    /**
     * 根据文档ID查询关联的SPU
     *
     * @param documentId 文档编号
     * @return 关联记录列表
     */
    default List<ProductPrintDocumentSpuDO> selectByDocumentId(Long documentId) {
        return selectList(new LambdaQueryWrapperX<ProductPrintDocumentSpuDO>()
                .eq(ProductPrintDocumentSpuDO::getDocumentId, documentId));
    }

    /**
     * 根据文档ID列表查询关联的SPU
     *
     * @param documentIds 文档编号列表
     * @return 关联记录列表
     */
    default List<ProductPrintDocumentSpuDO> selectByDocumentIds(List<Long> documentIds) {
        return selectList(new LambdaQueryWrapperX<ProductPrintDocumentSpuDO>()
                .in(ProductPrintDocumentSpuDO::getDocumentId, documentIds));
    }

    /**
     * 根据SPU ID删除所有关联记录
     *
     * @param spuId SPU编号
     */
    default void deleteBySpuId(Long spuId) {
        delete(new LambdaQueryWrapperX<ProductPrintDocumentSpuDO>()
                .eq(ProductPrintDocumentSpuDO::getSpuId, spuId));
    }

    /**
     * 根据文档ID删除所有关联记录
     *
     * @param documentId 文档编号
     */
    default void deleteByDocumentId(Long documentId) {
        delete(new LambdaQueryWrapperX<ProductPrintDocumentSpuDO>()
                .eq(ProductPrintDocumentSpuDO::getDocumentId, documentId));
    }

    /**
     * 检查文档是否被SPU使用
     *
     * @param documentId 文档编号
     * @return 是否被使用
     */
    default boolean existsByDocumentId(Long documentId) {
        return selectCount(new LambdaQueryWrapperX<ProductPrintDocumentSpuDO>()
                .eq(ProductPrintDocumentSpuDO::getDocumentId, documentId)) > 0;
    }

    /**
     * 批量插入文档SPU关联
     *
     * @param relations 关联记录列表
     */
    default void insertBatch(List<ProductPrintDocumentSpuDO> relations) {
        if (relations != null && !relations.isEmpty()) {
            relations.forEach(this::insert);
        }
    }

    /**
     * 根据SPU和文档查询关联记录
     *
     * @param spuId      SPU编号
     * @param documentId 文档编号
     * @return 关联记录
     */
    default ProductPrintDocumentSpuDO selectBySpuIdAndDocumentId(Long spuId, Long documentId) {
        return selectOne(new LambdaQueryWrapperX<ProductPrintDocumentSpuDO>()
                .eq(ProductPrintDocumentSpuDO::getSpuId, spuId)
                .eq(ProductPrintDocumentSpuDO::getDocumentId, documentId));
    }

}