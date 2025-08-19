package cn.iocoder.yudao.module.trade.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface PrintErrorCodeConstants {
    ErrorCode DOCUMENT_NOT_EXISTS = new ErrorCode(1_004_018_001, "文档不存在或无权访问");
    ErrorCode DOCUMENT_NOT_READY_FOR_PRINT = new ErrorCode(1_004_018_002, "文档尚未处理完成，暂不支持打印");
    ErrorCode PRINT_PRICE_CONFIG_NOT_EXISTS = new ErrorCode(1_004_018_003, "打印价格配置不存在");
    ErrorCode INVALID_PAGE_RANGE = new ErrorCode(1_004_018_004, "页数范围格式错误");
    ErrorCode TRADE_ORDER_PRINT_ITEMS_EMPTY = new ErrorCode(1_004_018_005, "打印项目不能为空");
}
