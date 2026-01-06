package com.star.lp.module.trade.framework.store.core.interceptor;

import cn.hutool.core.util.StrUtil;
import com.star.lp.framework.common.exception.ServiceException;
import com.star.lp.framework.security.core.util.SecurityFrameworkUtils;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreDO;
import com.star.lp.module.trade.dal.mysql.delivery.DeliveryPickUpStoreMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

import static com.star.lp.module.trade.enums.PrintErrorCodeConstants.STORE_NOT_FOUND_OR_NO_PERMISSION;

/**
 * 门店管理权限拦截器
 */
@Component
@Slf4j
public class StoreManagementInterceptor implements HandlerInterceptor {

    @Resource
    private DeliveryPickUpStoreMapper deliveryPickUpStoreMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取当前登录用户ID
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        if (userId == null) {
            return true; // 让Spring Security处理未登录情况
        }

        // 检查用户是否有门店管理权限
        if (!hasStorePermission(userId)) {
            throw new ServiceException(STORE_NOT_FOUND_OR_NO_PERMISSION);
        }

        return true;
    }

    /**
     * 检查用户是否有门店管理权限
     */
    private boolean hasStorePermission(Long userId) {
        DeliveryPickUpStoreDO store = deliveryPickUpStoreMapper.selectByStoreOperationUserId(userId);
        if (store == null) {
            return false;
        }

        // 检查用户ID是否在门店的验证用户列表中
        String verifyUserIds = store.getStoreOperationUserIds().toString();
        if (StrUtil.isBlank(verifyUserIds)) {
            return false;
        }

        List<String> userIdList = Arrays.asList(verifyUserIds.split(","));
        return userIdList.contains(userId.toString());
    }
}
