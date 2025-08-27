package com.star.lp.module.product.service.print;

import cn.hutool.core.collection.CollUtil;
import com.star.lp.framework.common.util.object.BeanUtils;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentDO;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentSpuDO;
import com.star.lp.module.product.dal.dataobject.spu.ProductSpuDO;
import com.star.lp.module.product.dal.mysql.print.ProductPrintDocumentMapper;
import com.star.lp.module.product.dal.mysql.print.ProductPrintDocumentSpuMapper;
import com.star.lp.module.product.enums.spu.ProductSpuStatusEnum;
import com.star.lp.module.product.service.print.ProductPrintDocumentSpuService;
import com.star.lp.module.product.service.spu.ProductSpuService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import static com.star.lp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.star.lp.framework.common.util.collection.CollectionUtils.convertList;
import static com.star.lp.framework.common.util.collection.CollectionUtils.convertSet;
import static com.star.lp.module.product.enums.PrintErrorCodeConstants.*;

/**
 * 打印文档与SPU关联 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class ProductPrintDocumentSpuServiceImpl implements ProductPrintDocumentSpuService {

    @Resource
    private ProductPrintDocumentSpuMapper documentSpuMapper;

    @Resource
    private ProductPrintDocumentMapper documentMapper;

    @Resource
    @Lazy
    private ProductSpuService productSpuService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByDocumentId(Long documentId) {
        documentSpuMapper.deleteByDocumentId(documentId);
        log.info("删除文档关联SPU成功，文档ID: {}", documentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDocumentSpuRelations(Long spuId, List<Long> documentIds) {
        if (CollUtil.isEmpty(documentIds)) {
            return;
        }

        // 验证文档是否存在
        validateDocumentsExist(documentIds);

        List<ProductPrintDocumentSpuDO> relations = new ArrayList<>();
        for (int i = 0; i < documentIds.size(); i++) {
            ProductPrintDocumentSpuDO relation = ProductPrintDocumentSpuDO.builder()
                    .documentId(documentIds.get(i))
                    .spuId(spuId)
                    .sort(i + 1) // 按照顺序设置排序
                    .build();
            relations.add(relation);
        }

        documentSpuMapper.insertBatch(relations);

        log.info("创建文档SPU关联成功，SPU: {}, 文档数量: {}", spuId, documentIds.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDocumentSpuRelations(Long spuId, List<Long> documentIds) {
        // 1. 删除现有关联
        documentSpuMapper.deleteBySpuId(spuId);

        // 2. 创建新关联
        if (CollUtil.isNotEmpty(documentIds)) {
            createDocumentSpuRelations(spuId, documentIds);
        }

        log.info("更新文档SPU关联成功，SPU: {}, 新文档数量: {}", spuId,
                documentIds != null ? documentIds.size() : 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBySpuId(Long spuId) {
        documentSpuMapper.deleteBySpuId(spuId);
        log.info("删除SPU关联文档成功，SPU: {}", spuId);
    }

    @Override
    public List<ProductPrintDocumentDO> getDocumentsBySpuId(Long spuId) {
        // 1. 查询关联关系
        List<ProductPrintDocumentSpuDO> relations = documentSpuMapper.selectBySpuId(spuId);
        if (CollUtil.isEmpty(relations)) {
            return Collections.emptyList();
        }

        // 2. 查询文档信息
        List<Long> documentIds = convertList(relations, ProductPrintDocumentSpuDO::getDocumentId);
        List<ProductPrintDocumentDO> documents = documentMapper.selectByIds(documentIds);

        // 3. 按照关联表中的排序返回
        Map<Long, ProductPrintDocumentDO> documentMap =
                documents.stream().collect(Collectors.toMap(ProductPrintDocumentDO::getId, doc -> doc));

        return relations.stream()
                .map(relation -> documentMap.get(relation.getDocumentId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getSpuIdsByDocumentId(Long documentId) {
        List<ProductPrintDocumentSpuDO> relations = documentSpuMapper.selectByDocumentId(documentId);
        return convertList(relations, ProductPrintDocumentSpuDO::getSpuId);
    }

    @Override
    public boolean isDocumentUsedBySpu(Long documentId) {
        return documentSpuMapper.existsByDocumentId(documentId);
    }

    @Override
    public boolean canDeleteDocument(Long documentId, Long userId) {
        // 1. 检查文档是否被SPU使用
        if (!isDocumentUsedBySpu(documentId)) {
            return true; // 未被任何SPU使用，可以删除
        }

        // 2. 查询使用该文档的SPU列表
        List<Long> spuIds = getSpuIdsByDocumentId(documentId);
        if (CollUtil.isEmpty(spuIds)) {
            return true;
        }

        // 3. 检查这些SPU是否有未完成的订单
        for (Long spuId : spuIds) {
            ProductSpuDO spu = productSpuService.getSpu(spuId);
            if (spu == null) {
                continue; // SPU已被删除，不影响
            }

            // 如果SPU仍在售卖状态，需要进一步检查订单
            if (ProductSpuStatusEnum.isEnable(spu.getStatus())) {
                // TODO: 这里可以调用订单服务检查是否有未完成的订单
                // 为了安全起见，如果SPU仍在售卖，则不允许删除文档
                return false;
            }
        }

        return true;
    }

    @Override
    public List<ProductPrintDocumentSpuDO> getRelationsBySpuId(Long spuId) {
        return documentSpuMapper.selectBySpuId(spuId);
    }

    @Override
    public Map<Long, Boolean> batchCheckDocumentUsage(List<Long> documentIds) {
        if (CollUtil.isEmpty(documentIds)) {
            return Collections.emptyMap();
        }

        List<ProductPrintDocumentSpuDO> relations = documentSpuMapper.selectByDocumentIds(documentIds);
        Set<Long> usedDocumentIds = convertSet(relations, ProductPrintDocumentSpuDO::getDocumentId);

        Map<Long, Boolean> result = new HashMap<>();
        for (Long documentId : documentIds) {
            result.put(documentId, usedDocumentIds.contains(documentId));
        }

        return result;
    }

    // ========== 私有方法 ==========

    private void validateDocumentsExist(List<Long> documentIds) {
        List<ProductPrintDocumentDO> documents = documentMapper.selectByIds(documentIds);
        if (documents.size() != documentIds.size()) {
            Set<Long> existIds = convertSet(documents, ProductPrintDocumentDO::getId);
            List<Long> notExistIds = documentIds.stream()
                    .filter(id -> !existIds.contains(id))
                    .collect(Collectors.toList());
            throw exception(PRINT_DOCUMENT_NOT_EXISTS, "文档不存在: " + notExistIds);
        }
    }

}
