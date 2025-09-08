package com.star.lp.module.trade.service.store;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.trade.controller.app.store.vo.*;

import java.util.List;

/**
 * 门店管理 Service 接口
 */
public interface StoreManagementService {

    /**
     * 获取门店信息
     */
    StoreInfoRespVO getStoreInfo(Long userId);

    /**
     * 获取订单统计
     */
    OrderStatsRespVO getOrderStats(Long userId);

    /**
     * 获取最近订单
     */
    PageResult<RecentOrderRespVO> getRecentOrders(Long userId, Integer pageSize);

    /**
     * 获取门店设备列表
     */
    List<StoreDeviceRespVO> getStoreDevices(Long userId);

    /**
     * 获取门店订单列表
     */
    PageResult<StoreOrderRespVO> getStoreOrders(Long userId, StoreOrderPageReqVO reqVO);

    /**
     * 指派打印机
     */
    void assignPrinter(Long userId, AssignPrinterReqVO reqVO);

    /**
     * 获取订单详情
     */
    StoreOrderDetailRespVO getOrderDetail(Long userId, Long orderId);

    /**
     * 暂停订单打印
     */
    void pauseOrderPrinting(Long userId, Long orderId);

    /**
     * 继续订单打印
     */
    void resumeOrderPrinting(Long userId, Long orderId);

    /**
     * 标记订单发货
     */
    void markOrderDelivered(Long userId, Long orderId);

    /**
     * 获取订单打印进度
     */
    OrderProgressRespVO getOrderProgress(Long userId, Long orderId);

    /**
     * 获取设备统计
     */
    DeviceStatsRespVO getDeviceStats(Long userId);

    /**
     * 添加打印设备
     */
    void addDevice(Long userId, AddDeviceReqVO reqVO);

    /**
     * 设备测试打印
     */
    void testDevice(Long userId, Long deviceId);

    /**
     * 暂停设备
     */
    void pauseDevice(Long userId, Long deviceId);

    /**
     * 恢复设备
     */
    void resumeDevice(Long userId, Long deviceId);

    /**
     * 重连设备
     */
    void reconnectDevice(Long userId, Long deviceId);

    /**
     * 清空设备打印队列
     */
    void clearDeviceQueue(Long userId, Long deviceId);

    /**
     * 删除设备
     */
    void deleteDevice(Long userId, Long deviceId);

    /**
     * 获取打印队列
     */
    PageResult<PrintQueueItemRespVO> getPrintQueue(Long userId, PrintQueuePageReqVO reqVO);

    /**
     * 队列任务置顶
     */
    void prioritizeQueueItem(Long userId, Long queueId);

    /**
     * 暂停队列任务
     */
    void pauseQueueItem(Long userId, Long queueId);

    /**
     * 恢复队列任务
     */
    void resumeQueueItem(Long userId, Long queueId);

    /**
     * 重试队列任务
     */
    void retryQueueItem(Long userId, Long queueId);

    /**
     * 取消队列任务
     */
    void cancelQueueItem(Long userId, Long queueId);
}