package com.cloudprint.util;

import com.cloudprint.model.SystemUser;
import com.cloudprint.model.TradeDeliveryPickUpStore;

/**
 * 用户会话管理类
 * 用于保存当前登录用户和选择的门店信息
 */
public class UserSession {
    private static SystemUser currentUser = null;
    private static TradeDeliveryPickUpStore currentStore = null;

    /**
     * 获取当前登录用户
     * @return 当前登录用户，如果未登录则返回null
     */
    public static SystemUser getCurrentUser() {
        return currentUser;
    }

    /**
     * 设置当前登录用户
     * @param user 登录用户
     */
    public static void setCurrentUser(SystemUser user) {
        currentUser = user;
    }

    /**
     * 获取当前选择的门店
     * @return 当前选择的门店，如果未选择则返回null
     */
    public static TradeDeliveryPickUpStore getCurrentStore() {
        return currentStore;
    }

    /**
     * 设置当前选择的门店
     * @param store 选择的门店
     */
    public static void setCurrentStore(TradeDeliveryPickUpStore store) {
        currentStore = store;
    }

    /**
     * 清除当前会话
     */
    public static void clearSession() {
        currentUser = null;
        currentStore = null;
    }

    /**
     * 检查是否有活跃会话
     * @return 是否有活跃会话
     */
    public static boolean hasActiveSession() {
        return currentUser != null;
    }
}
