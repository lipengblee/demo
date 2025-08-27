package com.star.lp.module.product.dal.dataobject.print;

import com.star.lp.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 打印文档 DO
 *
 * @author 芋道源码
 */
@TableName("product_print_document")
@KeySequence("product_print_document_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPrintDocumentDO extends BaseDO {

    /**
     * 文档编号
     */
    @TableId
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 文档名称
     */
    private String name;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;

    /**
     * 页数
     */
    private Integer pageCount;

    /**
     * 文档状态
     *
     * 1 - 正常
     * 2 - 处理中
     * 3 - 处理失败
     * 0 - 已删除
     */
    private Integer status;

    /**
     * 文件哈希值（用于去重）
     */
    private String fileHash;

    /**
     * 备注
     */
    private String remark;

    // ========== 状态枚举 ==========

    public static final Integer STATUS_NORMAL = 1;
    public static final Integer STATUS_PROCESSING = 2;
    public static final Integer STATUS_FAILED = 3;
    public static final Integer STATUS_DELETED = 0;

}