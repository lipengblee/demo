package com.star.lp.module.product.dal.mysql.print;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.mybatis.core.mapper.BaseMapperX;
import com.star.lp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.star.lp.module.product.controller.app.print.vo.AppProductPrintDocumentPageReqVO;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 打印文档 Mapper
 */
@Mapper
public interface ProductPrintDocumentMapper extends BaseMapperX<ProductPrintDocumentDO> {

    /**
     * 分页查询打印文档
     *
     * @param reqVO 查询条件
     * @return 分页结果
     */
    default PageResult<ProductPrintDocumentDO> selectPage(AppProductPrintDocumentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductPrintDocumentDO>()
                .eqIfPresent(ProductPrintDocumentDO::getUserId, reqVO.getUserId())
                .likeIfPresent(ProductPrintDocumentDO::getName, reqVO.getName())
                .eqIfPresent(ProductPrintDocumentDO::getFileType, reqVO.getFileType())
                .eqIfPresent(ProductPrintDocumentDO::getStatus, reqVO.getStatus())
                .ne(ProductPrintDocumentDO::getStatus, ProductPrintDocumentDO.STATUS_DELETED)
                .orderByDesc(ProductPrintDocumentDO::getId));
    }

    /**
     * 根据用户ID和文档ID查询
     *
     * @param userId 用户ID
     * @param id     文档ID
     * @return 文档信息
     */
    default ProductPrintDocumentDO selectByUserIdAndId(Long userId, Long id) {
        return selectOne(new LambdaQueryWrapperX<ProductPrintDocumentDO>()
                .eq(ProductPrintDocumentDO::getUserId, userId)
                .eq(ProductPrintDocumentDO::getId, id)
                .ne(ProductPrintDocumentDO::getStatus, ProductPrintDocumentDO.STATUS_DELETED));
    }

    /**
     * 根据用户ID和文档IDs查询
     *
     * @param userId      用户ID
     * @param documentIds 文档ID列表
     * @return 文档信息列表
     */
    default List<ProductPrintDocumentDO> selectByUserIdAndIds(Long userId, List<Long> documentIds) {
        return selectList(new LambdaQueryWrapperX<ProductPrintDocumentDO>()
                .eq(ProductPrintDocumentDO::getUserId, userId)
                .in(ProductPrintDocumentDO::getId, documentIds)
                .ne(ProductPrintDocumentDO::getStatus, ProductPrintDocumentDO.STATUS_DELETED));
    }

    /**
     * 根据文件哈希值查询
     *
     * @param userId   用户ID
     * @param fileHash 文件哈希值
     * @return 文档信息
     */
    default ProductPrintDocumentDO selectByUserIdAndFileHash(Long userId, String fileHash) {
        return selectOne(new LambdaQueryWrapperX<ProductPrintDocumentDO>()
                .eq(ProductPrintDocumentDO::getUserId, userId)
                .eq(ProductPrintDocumentDO::getFileHash, fileHash)
                .ne(ProductPrintDocumentDO::getStatus, ProductPrintDocumentDO.STATUS_DELETED));
    }

    /**
     * 逻辑删除（更新状态为删除）
     *
     * @param userId 用户ID
     * @param id     文档ID
     */
    default void logicDeleteByUserIdAndId(Long userId, Long id) {
        LambdaUpdateWrapper<ProductPrintDocumentDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ProductPrintDocumentDO::getUserId, userId)
                .eq(ProductPrintDocumentDO::getId, id)
                .set(ProductPrintDocumentDO::getStatus, ProductPrintDocumentDO.STATUS_DELETED);
        update(null, updateWrapper);
    }

    /**
     * 统计用户文档数量
     *
     * @param userId 用户ID
     * @return 文档数量
     */
    default Long countByUserId(Long userId) {
        return selectCount(new LambdaQueryWrapperX<ProductPrintDocumentDO>()
                .eq(ProductPrintDocumentDO::getUserId, userId)
                .ne(ProductPrintDocumentDO::getStatus, ProductPrintDocumentDO.STATUS_DELETED));
    }

    /**
     * 查询用户的处理中文档数量
     *
     * @param userId 用户ID
     * @return 处理中文档数量
     */
    default Long countProcessingByUserId(Long userId) {
        return selectCount(new LambdaQueryWrapperX<ProductPrintDocumentDO>()
                .eq(ProductPrintDocumentDO::getUserId, userId)
                .eq(ProductPrintDocumentDO::getStatus, ProductPrintDocumentDO.STATUS_PROCESSING));
    }

}