package com.star.lp.module.trade.convert.store;

import com.star.lp.module.trade.controller.app.store.vo.*;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreDO;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderDO;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderItemDO;
import com.star.lp.module.trade.dal.dataobject.print.PrintDeviceDO;
import com.star.lp.module.trade.dal.dataobject.print.PrintQueueDO;
import com.star.lp.module.trade.enums.order.TradeOrderStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface StoreManagementConvert {

    StoreManagementConvert INSTANCE = Mappers.getMapper(StoreManagementConvert.class);

    /**
     * 转换门店信息
     */
    StoreInfoRespVO convert(DeliveryPickUpStoreDO store);

    default ArrayList<RecentOrderRespVO> convertRecentOrders(List<TradeOrderDO> orders, List<TradeOrderItemDO> orderItems) {
        ArrayList<RecentOrderRespVO> result = new ArrayList<>();
        // 1. 构建订单项与订单ID的映射关系
        Map<Long, List<TradeOrderItemDO>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(TradeOrderItemDO::getOrderId));

        // 2. 转换每个订单
        for (TradeOrderDO order : orders) {
            RecentOrderRespVO vo = new RecentOrderRespVO();
            vo.setId(order.getId());
            vo.setNo(order.getNo());
            vo.setStatus(TradeOrderStatusEnum.fromStatus(order.getStatus()).getName());
            vo.setPrintStatus(order.getPrintStatus());
            vo.setReceiverName(order.getReceiverName());
            vo.setReceiverMobile(order.getReceiverMobile());
            vo.setPayPrice(order.getPayPrice());
            vo.setCreateTime(order.getCreateTime());

            // 3. 转换订单项
            List<TradeOrderItemDO> items = orderItemMap.getOrDefault(order.getId(), Collections.emptyList());
            List<RecentOrderRespVO.OrderItemVO> itemVOs = items.stream().map(item -> {
                RecentOrderRespVO.OrderItemVO itemVO = new RecentOrderRespVO.OrderItemVO();
                itemVO.setId(item.getId());
                itemVO.setSpuName(item.getSpuName());
                itemVO.setPicUrl(item.getPicUrl());
                itemVO.setCount(item.getCount());
                // 如果需要设置printDocument，这里需要补充对应的转换逻辑
                return itemVO;
            }).collect(Collectors.toList());
            vo.setItems(itemVOs);

            result.add(vo);
        }
        return result;
    }

    /**
     * 转换设备列表
     */
    ArrayList<StoreDeviceRespVO> convertDevices(List<PrintDeviceDO> devices);

    /**
     * 转换门店订单
     */
    ArrayList<StoreOrderRespVO> convertStoreOrders(List<TradeOrderDO> orders, List<TradeOrderItemDO> orderItems);

    /**
     * 转换订单详情
     */
    @Mapping(target = "id", source = "order.id")  // 明确指定id映射
    @Mapping(target = "status", source = "order.status")  // 明确指定id映射
    @Mapping(target = "createTime", source = "order.createTime")  // 明确指定id映射
    @Mapping(target = "remark", source = "order.remark")  // 明确指定id映射
    StoreOrderDetailRespVO convertOrderDetail(TradeOrderDO order,
                                              List<TradeOrderItemDO> orderItems,
                                              PrintDeviceDO assignedDevice,
                                              Integer printProgress,
                                              List<StoreOrderDetailRespVO.OrderLogVO> logs);

    /**
     * 转换设备请求
     */
    PrintDeviceDO convert(AddDeviceReqVO reqVO);

    /**
     * 转换打印队列
     */
    List<PrintQueueItemRespVO> convertPrintQueue(List<PrintQueueDO> queueItems);
}
