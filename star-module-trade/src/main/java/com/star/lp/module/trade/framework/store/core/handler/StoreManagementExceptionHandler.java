package com.star.lp.module.trade.framework.store.core.handler;

import com.star.lp.framework.common.exception.ServiceException;
import com.star.lp.framework.common.pojo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 门店管理异常处理器
 */
@ControllerAdvice("com.star.lp.module.trade.controller.app.store")
@Slf4j
public class StoreManagementExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public CommonResult<?> handleServiceException(ServiceException e) {
        log.warn("[StoreManagementExceptionHandler] 业务异常: {}", e.getMessage());
        return CommonResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResult<?> handleException(Exception e) {
        log.error("[StoreManagementExceptionHandler] 系统异常", e);
        return CommonResult.error(500, "系统异常，请联系管理员");
    }
}