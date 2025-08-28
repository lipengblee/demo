package com.star.lp.module.product.enums;

import com.star.lp.framework.common.exception.ErrorCode;

/**
 * 打印模块错误码枚举类
 *
 * 打印模块，使用 1-003-000-000 段
 */
public interface PrintErrorCodeConstants {

    // ========== 打印文档相关 1-003-001-000 ==========
    ErrorCode PRINT_DOCUMENT_NOT_EXISTS = new ErrorCode(1_003_001_000, "打印文档不存在");
    ErrorCode PRINT_DOCUMENT_FILE_EMPTY = new ErrorCode(1_003_001_001, "上传文件不能为空");
    ErrorCode PRINT_DOCUMENT_FILENAME_INVALID = new ErrorCode(1_003_001_002, "文件名不能为空");
    ErrorCode PRINT_DOCUMENT_TYPE_NOT_SUPPORTED = new ErrorCode(1_003_001_003, "不支持的文件格式，支持的格式有：{}");
    ErrorCode PRINT_DOCUMENT_SIZE_EXCEEDED = new ErrorCode(1_003_001_004, "文件大小超过限制，最大支持 {} MB");
    ErrorCode PRINT_DOCUMENT_COUNT_EXCEEDED = new ErrorCode(1_003_001_005, "文档数量超过限制，最多支持 {} 个文档");
    ErrorCode PRINT_DOCUMENT_UPLOAD_FAILED = new ErrorCode(1_003_001_006, "文档上传失败");
    ErrorCode PRINT_DOCUMENT_HASH_FAILED = new ErrorCode(1_003_001_007, "文档哈希计算失败");
    ErrorCode PRINT_DOCUMENT_FILES_EMPTY = new ErrorCode(1_003_001_008, "上传文件列表不能为空");
    ErrorCode PRINT_DOCUMENT_BATCH_LIMIT_EXCEEDED = new ErrorCode(1_003_001_009, "批量上传文件数量超过限制");
    ErrorCode PRINT_DOCUMENT_PROCESSING = new ErrorCode(1_003_001_010, "文档正在处理中，请稍后再试");
    ErrorCode PRINT_DOCUMENT_PARSE_FAILED = new ErrorCode(1_003_001_011, "文档解析失败");
    ErrorCode PRINT_DOCUMENT_CANNOT_DELETE_USED_BY_ORDER = new ErrorCode(1_003_001_012, "文档正在被订单使用，无法删除");

    // ========== 打印选项相关 1-003-002-000 ==========
    ErrorCode PRINT_OPTIONS_EMPTY = new ErrorCode(1_003_002_000, "打印选项不能为空");
    ErrorCode PRINT_OPTION_NOT_EXISTS = new ErrorCode(1_003_002_001, "打印选项不存在");
    ErrorCode PRINT_OPTION_VALUE_INVALID = new ErrorCode(1_003_002_002, "打印选项值无效");
    ErrorCode PRINT_PRICE_CONFIG_NOT_EXISTS = new ErrorCode(1_003_002_003, "打印价格配置不存在");

    // ========== 打印商品相关 1-003-003-000 ==========
    ErrorCode PRINT_SPU_CREATE_FAILED = new ErrorCode(1_003_003_000, "打印商品创建失败");
    ErrorCode PRINT_SPU_DOCUMENT_EMPTY = new ErrorCode(1_003_003_001, "打印商品必须包含至少一个文档");
    ErrorCode PRINT_SPU_OPTIONS_INVALID = new ErrorCode(1_003_003_002, "打印商品选项配置无效");

    // ========== 打印订单相关 1-003-004-000 ==========
    ErrorCode TRADE_ORDER_PRINT_ITEMS_EMPTY = new ErrorCode(1_003_004_000, "打印订单商品不能为空");
    ErrorCode PRINT_ORDER_NOT_EXISTS = new ErrorCode(1_003_004_001, "打印订单不存在");
    ErrorCode PRINT_ORDER_STATUS_INVALID = new ErrorCode(1_003_004_002, "打印订单状态无效");
    ErrorCode PRINT_ORDER_CANNOT_CANCEL = new ErrorCode(1_003_004_003, "打印订单无法取消");

    // ========== 合成先关 1_010_008_000 ==========
    public static final ErrorCode DOCUMENT_MERGE_FAILED = new ErrorCode(1_010_008_001, "文档合成失败");
    public static final ErrorCode DOCUMENT_CONVERT_FAILED = new ErrorCode(1_010_008_002, "文档转换失败：{}");
    public static final ErrorCode DOCUMENT_MERGE_LIMIT_EXCEEDED = new ErrorCode(1_010_008_003, "合成文档数量超过限制，最多支持{}个文档");
    public static final ErrorCode DOCUMENT_STATUS_INVALID = new ErrorCode(1_010_008_004, "文档状态无效，无法合成：{}");
    public static final ErrorCode PRINT_DOCUMENT_IDS_EMPTY = new ErrorCode(1_010_008_005, "文档ID列表不能为空");

}