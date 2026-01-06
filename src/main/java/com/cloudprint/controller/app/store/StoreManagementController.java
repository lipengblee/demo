package com.star.lp.module.trade.controller.app.store;

import com.star.lp.framework.common.pojo.CommonResult;
import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.trade.controller.app.store.vo.*;
import com.star.lp.module.trade.service.store.StoreManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.star.lp.framework.common.pojo.CommonResult.success;
import static com.star.lp.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "商家管理 - 门店管理")
@RestController
@RequestMapping("/trade/store")
@Validated
@Slf4j
public class StoreManagementController {

    @Resource
    private StoreManagementService storeManagementService;

    @GetMapping("/info")
    @Operation(summary = "获取门店信息")
    public CommonResult<StoreInfoRespVO> getStoreInfo() {
        return success(storeManagementService.getStoreInfo(getLoginUserId()));
    }

    @GetMapping("/order-stats")
    @Operation(summary = "获取订单统计")
    public CommonResult<OrderStatsRespVO> getOrderStats() {
        return success(storeManagementService.getOrderStats(getLoginUserId()));
    }

    @GetMapping("/recent-orders")
    @Operation(summary = "获取最近订单")
    @Parameter(name = "pageSize", description = "最近订单", example = "5")
    public CommonResult<PageResult<RecentOrderRespVO>> getRecentOrders(
            @RequestParam(defaultValue = "5") Integer pageSize) {
        return success(storeManagementService.getRecentOrders(getLoginUserId(), pageSize));
    }

    @GetMapping("/devices")
    @Operation(summary = "获取门店设备列表")
    public CommonResult<List<StoreDeviceRespVO>> getStoreDevices() {
        return success(storeManagementService.getStoreDevices(getLoginUserId()));
    }

    @GetMapping("/devices/{deviceId}")
    @Operation(summary = "获取门店设备详情")
    public CommonResult<StoreDeviceRespVO> getStoreDevice(@PathVariable Long deviceId) {
        return success(storeManagementService.getStoreDevice(getLoginUserId(), deviceId));
    }

    @GetMapping("/orders")
    @Operation(summary = "获取门店订单列表")
    public CommonResult<PageResult<StoreOrderRespVO>> getStoreOrders(
            @Valid StoreOrderPageReqVO reqVO) {
        return success(storeManagementService.getStoreOrders(getLoginUserId(), reqVO));
    }

    @GetMapping("/assign-printer")
    @Operation(summary = "指派打印机")
    public CommonResult<Boolean> assignPrinter(@Valid AssignPrinterReqVO reqVO) {
        storeManagementService.assignPrinter(getLoginUserId(), reqVO);
        return success(true);
    }

    @GetMapping("/orders/{orderId}")
    @Operation(summary = "获取订单详情")
    @Parameter(name = "orderId", description = "订单ID", required = true)
    public CommonResult<StoreOrderDetailRespVO> getOrderDetail(@PathVariable Long orderId) {
        return success(storeManagementService.getOrderDetail(getLoginUserId(), orderId));
    }

    @PostMapping("/orders/{orderId}/pause")
    @Operation(summary = "暂停订单打印")
    @Parameter(name = "orderId", description = "订单ID", required = true)
    public CommonResult<Boolean> pauseOrderPrinting(@PathVariable Long orderId) {
        storeManagementService.pauseOrderPrinting(getLoginUserId(), orderId);
        return success(true);
    }

    @PostMapping("/orders/{orderId}/resume")
    @Operation(summary = "继续订单打印")
    @Parameter(name = "orderId", description = "订单ID", required = true)
    public CommonResult<Boolean> resumeOrderPrinting(@PathVariable Long orderId) {
        storeManagementService.resumeOrderPrinting(getLoginUserId(), orderId);
        return success(true);
    }

    @PostMapping("/orders/{orderId}/deliver")
    @Operation(summary = "标记订单发货")
    @Parameter(name = "orderId", description = "订单ID", required = true)
    public CommonResult<Boolean> markOrderDelivered(@PathVariable Long orderId) {
        storeManagementService.markOrderDelivered(getLoginUserId(), orderId);
        return success(true);
    }

    @GetMapping("/orders/{orderId}/progress")
    @Operation(summary = "获取订单打印进度")
    @Parameter(name = "orderId", description = "订单ID", required = true)
    public CommonResult<OrderProgressRespVO> getOrderProgress(@PathVariable Long orderId) {
        return success(storeManagementService.getOrderProgress(getLoginUserId(), orderId));
    }

    // 设备管理相关接口
    @GetMapping("/devices/stats")
    @Operation(summary = "获取设备统计")
    public CommonResult<DeviceStatsRespVO> getDeviceStats() {
        return success(storeManagementService.getDeviceStats(getLoginUserId()));
    }

    @PostMapping("/devices")
    @Operation(summary = "添加/编辑打印设备")
    public CommonResult<Boolean> addDevice(@Valid @RequestBody AddDeviceReqVO reqVO) {
        if (reqVO.getId() != null) {
            // 更新设备
            storeManagementService.updateDevice(getLoginUserId(), reqVO, reqVO.getId());
        } else {
            // 添加设备
            storeManagementService.addDevice(getLoginUserId(), reqVO);
        }
        return success(true);
    }

    @PostMapping("/devices/{deviceId}/test")
    @Operation(summary = "设备测试打印")
    @Parameter(name = "deviceId", description = "设备ID", required = true)
    public CommonResult<Boolean> testDevice(@PathVariable Long deviceId) {
        storeManagementService.testDevice(getLoginUserId(), deviceId);
        return success(true);
    }

    @PostMapping("/devices/{deviceId}/pause")
    @Operation(summary = "暂停设备")
    @Parameter(name = "deviceId", description = "设备ID", required = true)
    public CommonResult<Boolean> pauseDevice(@PathVariable Long deviceId) {
        storeManagementService.pauseDevice(getLoginUserId(), deviceId);
        return success(true);
    }

    @PostMapping("/devices/{deviceId}/resume")
    @Operation(summary = "恢复设备")
    @Parameter(name = "deviceId", description = "设备ID", required = true)
    public CommonResult<Boolean> resumeDevice(@PathVariable Long deviceId) {
        storeManagementService.resumeDevice(getLoginUserId(), deviceId);
        return success(true);
    }

    @PostMapping("/devices/{deviceId}/reconnect")
    @Operation(summary = "重连设备")
    @Parameter(name = "deviceId", description = "设备ID", required = true)
    public CommonResult<Boolean> reconnectDevice(@PathVariable Long deviceId) {
        storeManagementService.reconnectDevice(getLoginUserId(), deviceId);
        return success(true);
    }

    @PostMapping("/devices/{deviceId}/clear-queue")
    @Operation(summary = "清空设备打印队列")
    @Parameter(name = "deviceId", description = "设备ID", required = true)
    public CommonResult<Boolean> clearDeviceQueue(@PathVariable Long deviceId) {
        storeManagementService.clearDeviceQueue(getLoginUserId(), deviceId);
        return success(true);
    }

    @DeleteMapping("/devices/{deviceId}")
    @Operation(summary = "删除设备")
    @Parameter(name = "deviceId", description = "设备ID", required = true)
    public CommonResult<Boolean> deleteDevice(@PathVariable Long deviceId) {
        storeManagementService.deleteDevice(getLoginUserId(), deviceId);
        return success(true);
    }

    // 打印队列管理相关接口
    @GetMapping("/print-queue")
    @Operation(summary = "获取打印队列")
    public CommonResult<PageResult<PrintQueueItemRespVO>> getPrintQueue(
            @Valid PrintQueuePageReqVO reqVO) {
        return success(storeManagementService.getPrintQueue(getLoginUserId(), reqVO));
    }

    @PostMapping("/print-queue/{queueId}/priority")
    @Operation(summary = "队列任务置顶")
    @Parameter(name = "queueId", description = "队列任务ID", required = true)
    public CommonResult<Boolean> prioritizeQueueItem(@PathVariable Long queueId) {
        storeManagementService.prioritizeQueueItem(getLoginUserId(), queueId);
        return success(true);
    }

    @PostMapping("/print-queue/{queueId}/pause")
    @Operation(summary = "暂停队列任务")
    @Parameter(name = "queueId", description = "队列任务ID", required = true)
    public CommonResult<Boolean> pauseQueueItem(@PathVariable Long queueId) {
        storeManagementService.pauseQueueItem(getLoginUserId(), queueId);
        return success(true);
    }

    @PostMapping("/print-queue/{queueId}/resume")
    @Operation(summary = "恢复队列任务")
    @Parameter(name = "queueId", description = "队列任务ID", required = true)
    public CommonResult<Boolean> resumeQueueItem(@PathVariable Long queueId) {
        storeManagementService.resumeQueueItem(getLoginUserId(), queueId);
        return success(true);
    }

    @PostMapping("/print-queue/{queueId}/retry")
    @Operation(summary = "重试队列任务")
    @Parameter(name = "queueId", description = "队列任务ID", required = true)
    public CommonResult<Boolean> retryQueueItem(@PathVariable Long queueId) {
        storeManagementService.retryQueueItem(getLoginUserId(), queueId);
        return success(true);
    }

    @PostMapping("/print-queue/{queueId}/cancel")
    @Operation(summary = "取消队列任务")
    @Parameter(name = "queueId", description = "队列任务ID", required = true)
    public CommonResult<Boolean> cancelQueueItem(@PathVariable Long queueId) {
        storeManagementService.cancelQueueItem(getLoginUserId(), queueId);
        return success(true);
    }

    @PutMapping("/info")
    @Operation(summary = "更新门店信息")
    public CommonResult<Boolean> updateStoreInfo(@Valid @RequestBody UpdateStoreInfoReqVO reqVO) {
        storeManagementService.updateStoreInfo(getLoginUserId(), reqVO);
        return success(true);
    }
}