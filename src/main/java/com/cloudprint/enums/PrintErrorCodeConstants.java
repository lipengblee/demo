package com.star.lp.module.trade.enums;

import com.star.lp.framework.common.exception.ErrorCode;

public interface PrintErrorCodeConstants {
    ErrorCode DOCUMENT_NOT_EXISTS = new ErrorCode(1_004_018_001, "文档不存在或无权访问");
    ErrorCode DOCUMENT_NOT_READY_FOR_PRINT = new ErrorCode(1_004_018_002, "文档尚未处理完成，暂不支持打印");
    ErrorCode PRINT_PRICE_CONFIG_NOT_EXISTS = new ErrorCode(1_004_018_003, "打印价格配置不存在");
    ErrorCode INVALID_PAGE_RANGE = new ErrorCode(1_004_018_004, "页数范围格式错误");
    ErrorCode TRADE_ORDER_PRINT_ITEMS_EMPTY = new ErrorCode(1_004_018_005, "打印项目不能为空");

    /**
     * 门店管理相关错误码
     */
    ErrorCode STORE_NOT_FOUND_OR_NO_PERMISSION = new ErrorCode(1_003_001_001, "门店不存在或无权限");
    ErrorCode ORDER_NOT_FOUND_OR_NO_PERMISSION = new ErrorCode(1_003_001_002, "订单不存在或无权限");
    ErrorCode DEVICE_NOT_FOUND_OR_NO_PERMISSION = new ErrorCode(1_003_001_003, "设备不存在或无权限");
    ErrorCode DEVICE_DELETE_HAS_ACTIVE_QUEUE = new ErrorCode(1_003_001_004, "设备有正在执行的任务，无法删除");
    ErrorCode QUEUE_ITEM_NOT_FOUND = new ErrorCode(1_003_001_005, "队列任务不存在");

    /**
     * 设备管理相关错误码
     */
    ErrorCode DEVICE_NOT_AVAILABLE = new ErrorCode(1_004_001_001, "设备不可用");
    ErrorCode ORDER_PRINT_NOT_COMPLETED = new ErrorCode(1_004_001_002, "订单打印未完成");
    ErrorCode DEVICE_NAME_EXISTS = new ErrorCode(1_004_001_003, "设备名称已存在");
    ErrorCode DEVICE_ADDRESS_INVALID = new ErrorCode(1_004_001_004, "设备地址无效");

    ErrorCode STORE_TIME_INVALID = new ErrorCode(1_011_020_001, "营业时间设置不合法，结束时间必须晚于开始时间");
    ErrorCode STORE_COORDINATE_INVALID = new ErrorCode(1_011_020_002, "门店坐标不合法");

}
