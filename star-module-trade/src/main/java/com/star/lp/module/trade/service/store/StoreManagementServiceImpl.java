package com.star.lp.module.trade.service.store;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.ip.core.utils.AreaUtils;
import com.star.lp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentDO;
import com.star.lp.module.product.service.print.ProductPrintDocumentSpuService;
import com.star.lp.module.trade.controller.app.store.vo.*;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreDO;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderDO;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderItemDO;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderLogDO;
import com.star.lp.module.trade.dal.dataobject.print.PrintDeviceDO;
import com.star.lp.module.trade.dal.dataobject.print.PrintQueueDO;
import com.star.lp.module.trade.dal.mysql.delivery.DeliveryPickUpStoreMapper;
import com.star.lp.module.trade.dal.mysql.order.TradeOrderLogMapper;
import com.star.lp.module.trade.dal.mysql.order.TradeOrderMapper;
import com.star.lp.module.trade.dal.mysql.order.TradeOrderItemMapper;
import com.star.lp.module.trade.dal.mysql.print.PrintDeviceMapper;
import com.star.lp.module.trade.dal.mysql.print.PrintQueueMapper;
import com.star.lp.module.trade.enums.order.TradeOrderStatusEnum;
import com.star.lp.module.trade.enums.store.DeviceStatusEnum;
import com.star.lp.module.trade.enums.store.PrintStatusEnum;
import com.star.lp.module.trade.framework.store.event.PrintTaskFailedEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.star.lp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.star.lp.framework.common.util.collection.CollectionUtils.convertSet;
import static com.star.lp.module.trade.enums.PrintErrorCodeConstants.*;

@Service
@Slf4j
public class StoreManagementServiceImpl implements StoreManagementService {

    @Resource
    private DeliveryPickUpStoreMapper deliveryPickUpStoreMapper;
    @Resource
    private TradeOrderMapper tradeOrderMapper;
    @Resource
    private TradeOrderItemMapper tradeOrderItemMapper;
    @Resource
    private TradeOrderLogMapper tradeOrderLogMapper;
    @Resource
    private PrintDeviceMapper printDeviceMapper;
    @Resource
    private PrintQueueMapper printQueueMapper;
    @Resource
    private ApplicationEventPublisher eventPublisher;
    @Resource
    private ProductPrintDocumentSpuService productPrintDocumentSpuService;

    @Override
    @Cacheable(value = "store:info", key = "#userId")
    public StoreInfoRespVO getStoreInfo(Long userId) {
        DeliveryPickUpStoreDO store = getStoreByUserId(userId);

        StoreInfoRespVO storeInfo = new StoreInfoRespVO();
        storeInfo.setId(store.getId());
        storeInfo.setName(store.getName());
        storeInfo.setAddress(store.getDetailAddress());
        storeInfo.setPhone(store.getPhone());
        storeInfo.setManagerName(store.getContact());

        // 判断营业状态（可以根据营业时间等规则）
        storeInfo.setStatus(isStoreOpen(store) ? 1 : 0);
        storeInfo.setBusinessHours("09:00-18:00"); // 可以从配置中获取

        return storeInfo;
    }

    @Override
    public OrderStatsRespVO getOrderStats(Long userId) {

        Long storeId = getStoreIdByUserId(userId);

        OrderStatsRespVO stats = new OrderStatsRespVO();

        // 今日时间范围
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        // 统计各状态订单
        stats.setPending(countOrdersByStoreAndPrintStatus(storeId, PrintStatusEnum.PENDING.getStatus()));
        stats.setPrinting(countOrdersByStoreAndPrintStatus(storeId, PrintStatusEnum.PRINTING.getStatus()));
        stats.setCompleted(countOrdersByStoreAndStatus(storeId, TradeOrderStatusEnum.COMPLETED.getStatus()));

        // 今日数据统计
        stats.setTodayCount(countTodayOrders(storeId, startOfDay, endOfDay));
        stats.setTodayAmount(getTodayAmount(storeId, startOfDay, endOfDay));

        // 总订单数（包含历史）
        stats.setTotal(countTotalOrdersByStore(storeId));

        return stats;
    }

    @Override
    public PageResult<RecentOrderRespVO> getRecentOrders(Long userId, Integer pageSize) {
        Long storeId = getStoreIdByUserId(userId);

        // 查询最近订单
        List<TradeOrderDO> orders = tradeOrderMapper.selectList(
                new LambdaQueryWrapperX<TradeOrderDO>()
                        .eq(TradeOrderDO::getAppointStoreId, storeId)
                        .eq(TradeOrderDO::getOrderType, 2)
                        .orderByDesc(TradeOrderDO::getCreateTime)
                        .last("LIMIT " + pageSize)
        );

        if (CollUtil.isEmpty(orders)) {
            return PageResult.empty();
        }

        // 查询订单项和打印文档信息
        List<RecentOrderRespVO> recentOrders = buildRecentOrderResponse(orders);

        return new PageResult<>(recentOrders, (long) recentOrders.size());
    }

    @Override
    public List<StoreDeviceRespVO> getStoreDevices(Long userId) {
        Long storeId = getStoreIdByUserId(userId);

        List<PrintDeviceDO> devices = printDeviceMapper.selectListByStoreId(storeId);

        return devices.stream().map(this::convertToDeviceRespVO).collect(Collectors.toList());
    }

    @Override
    public PageResult<StoreOrderRespVO> getStoreOrders(Long userId, StoreOrderPageReqVO reqVO) {
        Long storeId = getStoreIdByUserId(userId);
        // 构建查询条件
        LambdaQueryWrapperX<TradeOrderDO> wrapper = (LambdaQueryWrapperX<TradeOrderDO>) new LambdaQueryWrapperX<TradeOrderDO>()
                .eq(TradeOrderDO::getAppointStoreId, storeId)
                .eq(TradeOrderDO::getOrderType, 2) //店铺只显示打印类订单 -- 订单类型 1:普通商品订单 2:打印类订单
                .eqIfPresent(TradeOrderDO::getPrintStatus, convertOrderStatus(reqVO.getPrintStatus()))
                .eqIfPresent(TradeOrderDO::getStatus, convertOrderStatus(reqVO.getStatus()))
                .and(StrUtil.isNotBlank(reqVO.getKeyword()), w -> w
                        .like(TradeOrderDO::getNo, reqVO.getKeyword())
                        .or()
                        .like(TradeOrderDO::getReceiverName, reqVO.getKeyword()))
                .orderByDesc(TradeOrderDO::getCreateTime);
        PageResult<TradeOrderDO> pageResult = tradeOrderMapper.selectPage(reqVO, wrapper);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return PageResult.empty();
        }
        // 构建订单响应
        List<StoreOrderRespVO> orders = buildStoreOrderResponse(pageResult.getList());
        return new PageResult<>(orders, pageResult.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPrinter(Long userId, AssignPrinterReqVO reqVO) {
        Long storeId = getStoreIdByUserId(userId);

        // 校验订单和设备
        TradeOrderDO order = validateStoreOrder(storeId, reqVO.getOrderId());
        PrintDeviceDO device = validateStoreDevice(storeId, reqVO.getDeviceId());

        // 检查设备是否可用
        if (!DeviceStatusEnum.ONLINE.getStatus().equals(device.getStatus())) {
            throw exception(DEVICE_NOT_AVAILABLE);
        }

        // 获取订单项和打印文档信息
        List<TradeOrderItemDO> orderItems = tradeOrderItemMapper.selectListByOrderId(reqVO.getOrderId());

        for (TradeOrderItemDO orderItem : orderItems) {
            // 查找对应的打印文档
            ProductPrintDocumentDO printDocument = getPrintDocumentByOrderItem(orderItem);
            if (printDocument != null) {
                // 创建打印队列任务
                createPrintQueueTask(order, orderItem, printDocument, device, reqVO);
            }
        }

        // 更新订单打印状态
        updateOrderPrintStatus(reqVO.getOrderId(), PrintStatusEnum.ASSIGNED.getStatus());

        log.info("[assignPrinter] 订单 {} 已指派给设备 {}", reqVO.getOrderId(), device.getName());
    }

    @Override
    public StoreOrderDetailRespVO getOrderDetail(Long userId, Long orderId) {
        Long storeId = getStoreIdByUserId(userId);
        // 获取订单基本信息
        TradeOrderDO order = validateStoreOrder(storeId, orderId);
        List<TradeOrderItemDO> orderItems = tradeOrderItemMapper.selectListByOrderId(orderId);
        // 获取打印相关信息
        PrintDeviceDO assignedDevice = getAssignedDevice(orderId);
        Integer printProgress = getPrintProgress(orderId);
        List<StoreOrderDetailRespVO.OrderLogVO> logs = getOrderLogs(orderId);
        // 构建响应对象
        StoreOrderDetailRespVO detail = buildOrderDetailResponse(order, orderItems, assignedDevice, printProgress, logs);
        // 处理收货地址
        detail.setReceiverAreaName(AreaUtils.format(order.getReceiverAreaId()));
        return detail;
    }

    @Override
    public void pauseOrderPrinting(Long userId, Long orderId) {
        Long storeId = getStoreIdByUserId(userId);
        validateStoreOrder(storeId, orderId);

        // 暂停打印队列中的任务
        int updated = printQueueMapper.updateStatusByOrderId(orderId,
                PrintStatusEnum.PRINTING.getStatus(), PrintStatusEnum.PAUSED.getStatus());

        if (updated > 0) {
            updateOrderPrintStatus(orderId, PrintStatusEnum.PAUSED.getStatus());
            log.info("[pauseOrderPrinting] 订单 {} 打印已暂停", orderId);
        }
    }

    @Override
    public void resumeOrderPrinting(Long userId, Long orderId) {
        Long storeId = getStoreIdByUserId(userId);
        validateStoreOrder(storeId, orderId);

        // 恢复打印队列中的任务
        int updated = printQueueMapper.updateStatusByOrderId(orderId,
                PrintStatusEnum.PAUSED.getStatus(), PrintStatusEnum.WAITING.getStatus());

        if (updated > 0) {
            updateOrderPrintStatus(orderId, PrintStatusEnum.PRINTING.getStatus());
            log.info("[resumeOrderPrinting] 订单 {} 打印已恢复", orderId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markOrderDelivered(Long userId, Long orderId) {
        Long storeId = getStoreIdByUserId(userId);
        TradeOrderDO order = validateStoreOrder(storeId, orderId);

        // 检查打印是否完成
        PrintQueueDO queueItem = printQueueMapper.selectByOrderId(orderId);
        if (queueItem != null && !PrintStatusEnum.COMPLETED.getStatus().equals(queueItem.getStatus())) {
            throw exception(ORDER_PRINT_NOT_COMPLETED);
        }

        // 更新订单状态为已发货
        tradeOrderMapper.updateById(
                new TradeOrderDO()
                        .setId(orderId)
                        .setStatus(TradeOrderStatusEnum.DELIVERED.getStatus())
                        .setDeliveryTime(LocalDateTime.now())
                        .setLogisticsId(0L) // 门店自提，无需物流
                        .setLogisticsNo("")
        );

        // 记录操作日志
        recordOrderLog(orderId, "商家发货", "门店已完成打印并发货", userId);

        log.info("[markOrderDelivered] 订单 {} 已标记为发货", orderId);
    }

    @Override
    public OrderProgressRespVO getOrderProgress(Long userId, Long orderId) {
        Long storeId = getStoreIdByUserId(userId);
        validateStoreOrder(storeId, orderId);

        PrintQueueDO queueItem = printQueueMapper.selectByOrderId(orderId);

        OrderProgressRespVO progress = new OrderProgressRespVO();
        if (queueItem != null) {
            progress.setProgress(queueItem.getProgress() != null ? queueItem.getProgress() : 0);
            progress.setStatus(queueItem.getStatus());
            progress.setCurrentPage(queueItem.getCurrentPage() != null ? queueItem.getCurrentPage() : 0);
            progress.setTotalPages(queueItem.getTotalPages() != null ? queueItem.getTotalPages() : 0);
            progress.setEstimatedCompleteTime(calculateEstimatedCompleteTime(queueItem));
        } else {
            progress.setProgress(0);
            progress.setStatus(PrintStatusEnum.PENDING.getStatus());
        }

        return progress;
    }

    // ============== 设备管理实现 ==============

    @Override
    public DeviceStatsRespVO getDeviceStats(Long userId) {
        Long storeId = getStoreIdByUserId(userId);

        List<PrintDeviceDO> devices = printDeviceMapper.selectListByStoreId(storeId);

        DeviceStatsRespVO stats = new DeviceStatsRespVO();
        stats.setOnline((int) devices.stream().filter(d -> DeviceStatusEnum.ONLINE.getStatus().equals(d.getStatus())).count());
        stats.setBusy((int) devices.stream().filter(d -> DeviceStatusEnum.BUSY.getStatus().equals(d.getStatus())).count());
        stats.setOffline((int) devices.stream().filter(d -> DeviceStatusEnum.OFFLINE.getStatus().equals(d.getStatus())).count());
        stats.setTotal(devices.size());

        return stats;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDevice(Long userId, AddDeviceReqVO reqVO) {
        Long storeId = getStoreIdByUserId(userId);

        // 验证设备名称唯一性
        if (isDeviceNameExists(storeId, reqVO.getName())) {
            throw exception(DEVICE_NAME_EXISTS);
        }

        // 验证连接地址格式
        if (!isValidDeviceAddress(reqVO.getAddress(), reqVO.getConnectionType())) {
            throw exception(DEVICE_ADDRESS_INVALID);
        }

        PrintDeviceDO device = new PrintDeviceDO();
        device.setStoreId(storeId);
        device.setName(reqVO.getName());
        device.setType(reqVO.getType());
        device.setModel(reqVO.getModel());
        device.setLocation(reqVO.getLocation());
        device.setAddress(reqVO.getAddress());
        device.setConnectionType(reqVO.getConnectionType());
        device.setPort(reqVO.getPort());
        device.setStatus(DeviceStatusEnum.OFFLINE.getStatus());
        device.setPaperStatus("sufficient");
        device.setInkLevel(100);
        device.setQueueCount(0);
        device.setTodayCount(0);
        device.setSuccessRate(100);
        device.setRemark(reqVO.getRemark());
        device.setDeleted(false);

        printDeviceMapper.insert(device);

        log.info("[addDevice] 门店 {} 添加设备: {}", storeId, reqVO.getName());
    }

    @Override
    public void testDevice(Long userId, Long deviceId) {

        // 更新设备状态
        printDeviceMapper.updateById(
                new PrintDeviceDO()
                        .setId(deviceId)
                        .setStatus(DeviceStatusEnum.ONLINE.getStatus())
                        .setLastConnectTime(LocalDateTime.now())
        );

        // 恢复设备上的暂停任务
        printQueueMapper.updateStatusByDeviceId(deviceId,
                PrintStatusEnum.PAUSED.getStatus(), PrintStatusEnum.WAITING.getStatus());

        log.info("[resumeDevice] 设备 {} 已恢复");
    }

    @Override
    public void pauseDevice(Long userId, Long deviceId) {
        Long storeId = getStoreIdByUserId(userId);
        PrintDeviceDO device = validateStoreDevice(storeId, deviceId);

        // 更新设备状态
        printDeviceMapper.updateById(
                new PrintDeviceDO().setId(deviceId).setStatus(DeviceStatusEnum.PAUSED.getStatus())
        );

        // 暂停设备上的所有打印任务
        printQueueMapper.updateStatusByDeviceId(deviceId,
                PrintStatusEnum.PRINTING.getStatus(), PrintStatusEnum.PAUSED.getStatus());

        log.info("[pauseDevice] 设备 {} 已暂停", device.getName());
    }

    @Override
    public void resumeDevice(Long userId, Long deviceId) {
        Long storeId = getStoreIdByUserId(userId);
        PrintDeviceDO device = validateStoreDevice(storeId, deviceId);
    }

    @Override
    public void reconnectDevice(Long userId, Long deviceId) {
        Long storeId = getStoreIdByUserId(userId);
        PrintDeviceDO device = validateStoreDevice(storeId, deviceId);

        // TODO: 实现具体的设备重连逻辑
        // 这里可以调用设备驱动或网络连接

        // 模拟重连成功，更新设备状态
        printDeviceMapper.updateById(
                new PrintDeviceDO()
                        .setId(deviceId)
                        .setStatus(DeviceStatusEnum.ONLINE.getStatus())
                        .setLastConnectTime(LocalDateTime.now())
        );

        log.info("[reconnectDevice] 设备 {} 重连成功", device.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearDeviceQueue(Long userId, Long deviceId) {
        Long storeId = getStoreIdByUserId(userId);
        PrintDeviceDO device = validateStoreDevice(storeId, deviceId);

        // 删除等待和暂停的任务
        List<String> clearableStatus = Arrays.asList(
                PrintStatusEnum.WAITING.getStatus(),
                PrintStatusEnum.PAUSED.getStatus()
        );

        int deletedCount = printQueueMapper.deleteByDeviceIdAndStatus(deviceId, clearableStatus);

        // 更新设备队列数量
        updateDeviceQueueCount(deviceId);

        log.info("[clearDeviceQueue] 设备 {} 队列已清空，删除 {} 个任务", device.getName(), deletedCount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDevice(Long userId, Long deviceId) {
        Long storeId = getStoreIdByUserId(userId);
        PrintDeviceDO device = validateStoreDevice(storeId, deviceId);

        // 检查是否有正在执行的任务
        List<String> activeStatus = Arrays.asList(
                PrintStatusEnum.PRINTING.getStatus(),
                PrintStatusEnum.WAITING.getStatus()
        );

        boolean hasActiveQueue = printQueueMapper.existsByDeviceIdAndStatus(deviceId, activeStatus);
        if (hasActiveQueue) {
            throw exception(DEVICE_DELETE_HAS_ACTIVE_QUEUE);
        }

        // 软删除设备
        printDeviceMapper.updateById(
                new PrintDeviceDO().setId(deviceId).setDeleted(true)
        );

        log.info("[deleteDevice] 设备 {} 已删除", device.getName());
    }

    // ============== 打印队列管理实现 ==============

    @Override
    public PageResult<PrintQueueItemRespVO> getPrintQueue(Long userId, PrintQueuePageReqVO reqVO) {
        Long storeId = getStoreIdByUserId(userId);

        // 如果指定了设备，验证设备权限
        if (reqVO.getDeviceId() != null) {
            validateStoreDevice(storeId, reqVO.getDeviceId());
        }

        // 构建查询条件
        LambdaQueryWrapperX<PrintQueueDO> wrapper = (LambdaQueryWrapperX<PrintQueueDO>) new LambdaQueryWrapperX<PrintQueueDO>()
                .eqIfPresent(PrintQueueDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(PrintQueueDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(PrintQueueDO::getCreateTime, reqVO.getStartTime(), reqVO.getEndTime())
                .eq(PrintQueueDO::getDeleted, false)
                .orderByAsc(PrintQueueDO::getPriority)
                .orderByAsc(PrintQueueDO::getCreateTime);

        PageResult<PrintQueueDO> pageResult = printQueueMapper.selectPage(reqVO, wrapper);

        if (CollUtil.isEmpty(pageResult.getList())) {
            return PageResult.empty();
        }

        // 转换结果
        List<PrintQueueItemRespVO> queueItems = pageResult.getList().stream()
                .map(this::convertToPrintQueueItemRespVO)
                .collect(Collectors.toList());

        return new PageResult<>(queueItems, pageResult.getTotal());
    }

    @Override
    public void prioritizeQueueItem(Long userId, Long queueId) {
        Long storeId = getStoreIdByUserId(userId);
        PrintQueueDO queueItem = validateStoreQueueItem(storeId, queueId);

        // 将优先级设置为最高
        printQueueMapper.updateById(
                new PrintQueueDO().setId(queueId).setPriority(1)
        );

        log.info("[prioritizeQueueItem] 队列任务 {} 已置顶", queueId);
    }

    @Override
    public void pauseQueueItem(Long userId, Long queueId) {
        Long storeId = getStoreIdByUserId(userId);
        validateStoreQueueItem(storeId, queueId);

        printQueueMapper.updateById(
                new PrintQueueDO().setId(queueId).setStatus(PrintStatusEnum.PAUSED.getStatus())
        );

        log.info("[pauseQueueItem] 队列任务 {} 已暂停", queueId);
    }

    @Override
    public void resumeQueueItem(Long userId, Long queueId) {
        Long storeId = getStoreIdByUserId(userId);
        validateStoreQueueItem(storeId, queueId);

        printQueueMapper.updateById(
                new PrintQueueDO().setId(queueId).setStatus(PrintStatusEnum.WAITING.getStatus())
        );

        log.info("[resumeQueueItem] 队列任务 {} 已恢复", queueId);
    }

    @Override
    public void retryQueueItem(Long userId, Long queueId) {
        Long storeId = getStoreIdByUserId(userId);
        PrintQueueDO queueItem = validateStoreQueueItem(storeId, queueId);

        // 重置任务状态
        printQueueMapper.updateById(
                new PrintQueueDO()
                        .setId(queueId)
                        .setStatus(PrintStatusEnum.WAITING.getStatus())
                        .setProgress(0)
                        .setCurrentPage(0)
                        .setErrorMessage(null)
                        .setRetryCount((queueItem.getRetryCount() != null ? queueItem.getRetryCount() : 0) + 1)
        );

        // 发布重试事件
        PrintTaskFailedEvent retryEvent = new PrintTaskFailedEvent();
        retryEvent.setQueueId(queueId);
        retryEvent.setOrderId(queueItem.getOrderId());
        retryEvent.setDeviceId(queueItem.getDeviceId());
        retryEvent.setErrorMessage("用户手动重试");
        retryEvent.setRetryCount(queueItem.getRetryCount() + 1);
        eventPublisher.publishEvent(retryEvent);

        log.info("[retryQueueItem] 队列任务 {} 已重试", queueId);
    }

    @Override
    public void cancelQueueItem(Long userId, Long queueId) {
        Long storeId = getStoreIdByUserId(userId);
        validateStoreQueueItem(storeId, queueId);

        printQueueMapper.updateById(
                new PrintQueueDO()
                        .setId(queueId)
                        .setStatus(PrintStatusEnum.CANCELLED.getStatus())
                        .setCompleteTime(LocalDateTime.now())
        );

        log.info("[cancelQueueItem] 队列任务 {} 已取消", queueId);
    }

    // ============== 私有辅助方法 ==============

    /**
     * 根据用户ID获取可操作的取件门店信息
     *
     * @param userId 用户ID
     * @return DeliveryPickUpStoreDO 取件门店实体对象
     * @throws 如果用户无权限或未找到对应门店，抛出异常
     */
    private DeliveryPickUpStoreDO getStoreByUserId(Long userId) {
        // 通过用户ID查询可操作的门店列表
        // 查询条件：门店操作用户ID列表包含当前用户，且门店未标记为已删除
        DeliveryPickUpStoreDO store = deliveryPickUpStoreMapper.selectByStoreOperationUserId(userId);
        // 如果查询结果为空，抛出门店未找到或无权限异常
        if (ObjUtil.isEmpty(store)) {
            throw exception(STORE_NOT_FOUND_OR_NO_PERMISSION);
        }
        // 验证用户ID确实在验证列表中
        List<Long> userIds = store.getStoreOperationUserIds();
        if (userIds != null && userIds.contains(userId)) {
            return store;
        }
        throw exception(STORE_NOT_FOUND_OR_NO_PERMISSION);
    }

    private Long getStoreIdByUserId(Long userId) {
        return getStoreByUserId(userId).getId();
    }

    private TradeOrderDO validateStoreOrder(Long storeId, Long orderId) {
        TradeOrderDO order = tradeOrderMapper.selectById(orderId);
        if (order == null || !ObjUtil.equals(order.getAppointStoreId(), storeId)) {
            throw exception(ORDER_NOT_FOUND_OR_NO_PERMISSION);
        }
        return order;
    }

    private PrintDeviceDO validateStoreDevice(Long storeId, Long deviceId) {
        PrintDeviceDO device = printDeviceMapper.selectById(deviceId);
        if (device == null || !ObjUtil.equals(device.getStoreId(), storeId) || device.getDeleted()) {
            throw exception(DEVICE_NOT_FOUND_OR_NO_PERMISSION);
        }
        return device;
    }

    private PrintQueueDO validateStoreQueueItem(Long storeId, Long queueId) {
        PrintQueueDO queueItem = printQueueMapper.selectById(queueId);
        if (queueItem == null || queueItem.getDeleted()) {
            throw exception(QUEUE_ITEM_NOT_FOUND);
        }

        // 通过设备验证权限
        validateStoreDevice(storeId, queueItem.getDeviceId());
        return queueItem;
    }

    private boolean isStoreOpen(DeliveryPickUpStoreDO store) {
        // TODO: 实现营业时间判断逻辑
        // 这里可以根据门店的营业时间配置来判断
        return true; // 暂时默认营业中
    }

    private Integer countOrdersByStoreAndPrintStatus(Long storeId, String printStatus) {
        return Math.toIntExact(tradeOrderMapper.selectCount(
                new LambdaQueryWrapperX<TradeOrderDO>()
                        .eq(TradeOrderDO::getAppointStoreId, storeId)
                        .eq(TradeOrderDO::getPrintStatus, printStatus)
                        .eq(TradeOrderDO::getOrderType, 2)
        ));
    }

    private Integer countOrdersByStoreAndStatus(Long storeId, Integer status) {
        return Math.toIntExact(tradeOrderMapper.selectCount(
                new LambdaQueryWrapperX<TradeOrderDO>()
                        .eq(TradeOrderDO::getAppointStoreId, storeId)
                        .eq(TradeOrderDO::getStatus, status)
                        .eq(TradeOrderDO::getOrderType, 2)
        ));
    }

    private Integer countTodayOrders(Long storeId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return Math.toIntExact(tradeOrderMapper.selectCount(
                new LambdaQueryWrapperX<TradeOrderDO>()
                        .eq(TradeOrderDO::getAppointStoreId, storeId)
                        .eq(TradeOrderDO::getOrderType, 2)
                        .between(TradeOrderDO::getCreateTime, startOfDay, endOfDay)
        ));
    }

    private Integer getTodayAmount(Long storeId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        List<TradeOrderDO> orders = tradeOrderMapper.selectList(
                new LambdaQueryWrapperX<TradeOrderDO>()
                        .eq(TradeOrderDO::getAppointStoreId, storeId)
                        .eq(TradeOrderDO::getOrderType, 2)
                        .eq(TradeOrderDO::getPayStatus, true)
                        .between(TradeOrderDO::getCreateTime, startOfDay, endOfDay)
        );

        return orders.stream().mapToInt(TradeOrderDO::getPayPrice).sum();
    }

    private Integer countTotalOrdersByStore(Long storeId) {
        return Math.toIntExact(tradeOrderMapper.selectCount(
                new LambdaQueryWrapperX<TradeOrderDO>()
                        .eq(TradeOrderDO::getAppointStoreId, storeId)
                        .eq(TradeOrderDO::getOrderType, 2)
        ));
    }

    private List<RecentOrderRespVO> buildRecentOrderResponse(List<TradeOrderDO> orders) {
        // 查询订单项
        Set<Long> orderIds = convertSet(orders, TradeOrderDO::getId);
        List<TradeOrderItemDO> orderItems = tradeOrderItemMapper.selectListByOrderIds(orderIds);

        // 按订单ID分组
        Map<Long, List<TradeOrderItemDO>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(TradeOrderItemDO::getOrderId));

        return orders.stream().map(order -> {
            RecentOrderRespVO respVO = new RecentOrderRespVO();
            respVO.setId(order.getId());
            respVO.setNo(order.getNo());
            respVO.setStatus(order.getStatus().toString());
            respVO.setPrintStatus(order.getPrintStatus());
            respVO.setReceiverName(order.getReceiverName());
            respVO.setReceiverMobile(order.getReceiverMobile());
            respVO.setPayPrice(order.getPayPrice());
            respVO.setCreateTime(order.getCreateTime());

            // 设置订单项
            List<TradeOrderItemDO> items = orderItemMap.get(order.getId());
            if (CollUtil.isNotEmpty(items)) {
                List<RecentOrderRespVO.OrderItemVO> itemVOs = items.stream()
                        .map(this::convertToOrderItemVO)
                        .collect(Collectors.toList());
                respVO.setItems(itemVOs);
            }

            return respVO;
        }).collect(Collectors.toList());
    }

    private List<StoreOrderRespVO> buildStoreOrderResponse(List<TradeOrderDO> orders) {
        // 类似buildRecentOrderResponse的实现
        Set<Long> orderIds = convertSet(orders, TradeOrderDO::getId);
        List<TradeOrderItemDO> orderItems = tradeOrderItemMapper.selectListByOrderIds(orderIds);

        Map<Long, List<TradeOrderItemDO>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(TradeOrderItemDO::getOrderId));

        return orders.stream().map(order -> {
            StoreOrderRespVO respVO = new StoreOrderRespVO();
            respVO.setId(order.getId());
            respVO.setNo(order.getNo());
            respVO.setStatus(order.getStatus().toString());
            respVO.setPrintStatus(order.getPrintStatus());
            respVO.setReceiverName(order.getReceiverName());
            respVO.setReceiverMobile(order.getReceiverMobile());
            respVO.setPayPrice(order.getPayPrice());
            respVO.setCreateTime(order.getCreateTime());

            // 设置订单项
            List<TradeOrderItemDO> items = orderItemMap.get(order.getId());
            if (CollUtil.isNotEmpty(items)) {
                List<RecentOrderRespVO.OrderItemVO> itemVOs = items.stream()
                        .map(this::convertToOrderItemVO)
                        .collect(Collectors.toList());
                respVO.setItems(itemVOs);
            }

            return respVO;
        }).collect(Collectors.toList());
    }

    private StoreOrderDetailRespVO buildOrderDetailResponse(TradeOrderDO order,
                                                            List<TradeOrderItemDO> orderItems,
                                                            PrintDeviceDO assignedDevice,
                                                            Integer printProgress,
                                                            List<StoreOrderDetailRespVO.OrderLogVO> logs) {
        StoreOrderDetailRespVO detail = new StoreOrderDetailRespVO();

        // 基本订单信息
        detail.setId(order.getId());
        detail.setNo(order.getNo());
        detail.setStatus(order.getStatus().toString());
        detail.setPrintStatus(order.getPrintStatus());
        detail.setReceiverName(order.getReceiverName());
        detail.setReceiverMobile(order.getReceiverMobile());
        detail.setReceiverDetailAddress(order.getReceiverDetailAddress());
        detail.setPayPrice(order.getPayPrice());
        detail.setDiscountPrice(order.getDiscountPrice());
        detail.setDeliveryPrice(order.getDeliveryPrice());
        detail.setTotalPrice(order.getTotalPrice());
        detail.setCreateTime(order.getCreateTime());
        detail.setUserRemark(order.getUserRemark());
        detail.setRemark(order.getRemark());

        // 订单项
        if (CollUtil.isNotEmpty(orderItems)) {
            List<RecentOrderRespVO.OrderItemVO> itemVOs = orderItems.stream()
                    .map(this::convertToOrderItemVO)
                    .collect(Collectors.toList());
            detail.setItems(itemVOs);
        }
        // 指派的设备
        if (assignedDevice != null) {
            detail.setAssignedDevice(convertToDeviceRespVO(assignedDevice));
        }
        // 打印进度
        detail.setPrintProgress(printProgress);
        // 订单日志
        detail.setLogs(logs != null ? logs : new ArrayList<>());

        return detail;
    }

    private RecentOrderRespVO.OrderItemVO convertToOrderItemVO(TradeOrderItemDO orderItem) {
        RecentOrderRespVO.OrderItemVO itemVO = new RecentOrderRespVO.OrderItemVO();
        itemVO.setId(orderItem.getId());
        itemVO.setSpuName(orderItem.getSpuName());
        itemVO.setPicUrl(orderItem.getPicUrl());
        itemVO.setCount(orderItem.getCount());
        itemVO.setPayPrice(orderItem.getPayPrice());
        itemVO.setPrice(orderItem.getPrice());
        // 获取打印文档信息
        ProductPrintDocumentDO printDocument = getPrintDocumentByOrderItem(orderItem);
        if (printDocument != null) {
            RecentOrderRespVO.PrintDocumentVO docVO = new RecentOrderRespVO.PrintDocumentVO();
            docVO.setId(printDocument.getId());
            docVO.setTitle(printDocument.getName());
            docVO.setUrl(printDocument.getFileUrl());
            docVO.setType(printDocument.getFileType());
            docVO.setPages(printDocument.getPageCount());
            itemVO.setPrintDocument(docVO);
        }
        return itemVO;
    }

    private StoreDeviceRespVO convertToDeviceRespVO(PrintDeviceDO device) {
        StoreDeviceRespVO respVO = new StoreDeviceRespVO();
        respVO.setId(device.getId());
        respVO.setName(device.getName());
        respVO.setType(device.getType());
        respVO.setModel(device.getModel());
        respVO.setStatus(device.getStatus());
        respVO.setLocation(device.getLocation());
        respVO.setQueueCount(device.getQueueCount() != null ? device.getQueueCount() : 0);
        respVO.setTodayCount(device.getTodayCount() != null ? device.getTodayCount() : 0);
        respVO.setSuccessRate(device.getSuccessRate() != null ? device.getSuccessRate() : 100);
        respVO.setPaperStatus(device.getPaperStatus());
        respVO.setInkLevel(device.getInkLevel() != null ? device.getInkLevel() : 100);
        respVO.setLastConnectTime(device.getLastConnectTime());
        return respVO;
    }

    private PrintQueueItemRespVO convertToPrintQueueItemRespVO(PrintQueueDO queueItem) {
        PrintQueueItemRespVO respVO = new PrintQueueItemRespVO();
        respVO.setId(queueItem.getId());
        respVO.setOrderId(queueItem.getOrderId());
        respVO.setOrderNo(queueItem.getOrderNo());
        respVO.setQueueIndex(queueItem.getQueueIndex());
        respVO.setStatus(queueItem.getStatus());
        respVO.setDocumentTitle(queueItem.getDocumentTitle());
        respVO.setPages(queueItem.getPages());
        respVO.setCopies(queueItem.getCopies());
        respVO.setCustomerName(queueItem.getCustomerName());
        respVO.setProgress(queueItem.getProgress() != null ? queueItem.getProgress() : 0);
        respVO.setErrorMessage(queueItem.getErrorMessage());
        respVO.setCreateTime(queueItem.getCreateTime());
        respVO.setStartTime(queueItem.getStartTime());
        respVO.setCompleteTime(queueItem.getCompleteTime());
        return respVO;
    }

    private ProductPrintDocumentDO getPrintDocumentByOrderItem(TradeOrderItemDO orderItem) {
        List<ProductPrintDocumentDO> printDocuments = productPrintDocumentSpuService.getDocumentsBySpuId(orderItem.getSpuId());
        // 检查列表是否不为空且包含元素
        if (printDocuments != null && !printDocuments.isEmpty()) {
            return printDocuments.get(0); // 返回第一条数据
        }
        // 如果列表为空则返回null
        return null;
    }

    private void createPrintQueueTask(TradeOrderDO order, TradeOrderItemDO orderItem,
                                      ProductPrintDocumentDO printDocument, PrintDeviceDO device,
                                      AssignPrinterReqVO reqVO) {
        // TODO: 实现创建打印队列任务的逻辑
    }

    private void updateOrderPrintStatus(Long orderId, String status) {
        tradeOrderMapper.updateById(
                new TradeOrderDO().setId(orderId).setPrintStatus(status)
        );
    }

    private PrintDeviceDO getAssignedDevice(Long orderId) {
        return printDeviceMapper.selectAssignedDevice(orderId);
    }

    private Integer getPrintProgress(Long orderId) {
        PrintQueueDO queueItem = printQueueMapper.selectByOrderId(orderId);
        return queueItem != null ? queueItem.getProgress() : null;
    }

    private List<StoreOrderDetailRespVO.OrderLogVO> getOrderLogs(Long orderId) {
        List<TradeOrderLogDO> logs = tradeOrderLogMapper.selectListByOrderId(orderId);
        return logs.stream().map(log -> {
            StoreOrderDetailRespVO.OrderLogVO logVO = new StoreOrderDetailRespVO.OrderLogVO();
            logVO.setTitle(log.getTitle());
            logVO.setContent(log.getContent());
            logVO.setCreateTime(log.getCreateTime());
            return logVO;
        }).collect(Collectors.toList());
    }

    private LocalDateTime calculateEstimatedCompleteTime(PrintQueueDO queueItem) {
        if (queueItem.getTotalPages() == null || queueItem.getCurrentPage() == null) {
            return null;
        }

        int remainingPages = queueItem.getTotalPages() - queueItem.getCurrentPage();
        if (remainingPages <= 0) {
            return LocalDateTime.now();
        }

        // 假设每页打印30秒
        return LocalDateTime.now().plusSeconds(remainingPages * 30L);
    }

    private String convertOrderStatus(String status) {
        // 将前端状态转换为数据库状态
        if (StrUtil.isBlank(status) || "all".equals(status)) {
            return null;
        }
        switch (status) {
            case "pending":
                return PrintStatusEnum.PENDING.getStatus();
            case "printing":
                return PrintStatusEnum.PRINTING.getStatus();
            case "completed":
                return PrintStatusEnum.COMPLETED.getStatus();
            case "delivered":
                return TradeOrderStatusEnum.DELIVERED.getStatus().toString();
            default:
                return status;
        }
    }

    private void recordOrderLog(Long orderId, String title, String content, Long userId) {
        TradeOrderLogDO log = new TradeOrderLogDO();
        log.setOrderId(orderId);
        log.setTitle(title);
        log.setContent(content);
        log.setUserId(userId);
        tradeOrderLogMapper.insert(log);
    }

    private boolean isDeviceNameExists(Long storeId, String name) {
        return printDeviceMapper.selectCount(
                new LambdaQueryWrapperX<PrintDeviceDO>()
                        .eq(PrintDeviceDO::getStoreId, storeId)
                        .eq(PrintDeviceDO::getName, name)
                        .eq(PrintDeviceDO::getDeleted, false)
        ) > 0;
    }

    private boolean isValidDeviceAddress(String address, String connectionType) {
        if (StrUtil.isBlank(address) || StrUtil.isBlank(connectionType)) {
            return false;
        }

        // 简单的地址格式验证
        switch (connectionType.toLowerCase()) {
            case "tcp":
                return address.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
            case "usb":
                return address.startsWith("/dev/") || address.startsWith("COM");
            case "bluetooth":
                return address.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
            default:
                return true;
        }
    }

    private void updateDeviceQueueCount(Long deviceId) {
        Long queueCount = printQueueMapper.countByDeviceIdAndStatus(deviceId,
                PrintStatusEnum.WAITING.getStatus());
        printDeviceMapper.updateById(
                new PrintDeviceDO().setId(deviceId).setQueueCount(queueCount.intValue())
        );
    }
}