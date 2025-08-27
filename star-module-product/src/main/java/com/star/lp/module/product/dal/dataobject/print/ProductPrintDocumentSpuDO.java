package com.star.lp.module.product.dal.dataobject.print;

import com.star.lp.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 打印文档与商品SPU关联 DO
 */
@TableName("product_print_document_spu")
@KeySequence("product_print_document_spu_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPrintDocumentSpuDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId
    private Long id;

    /**
     * 文档编号
     */
    private Long documentId;

    /**
     * 商品SPU编号
     */
    private Long spuId;

    /**
     * 排序字段（用于控制文档在商品中的显示顺序）
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

}