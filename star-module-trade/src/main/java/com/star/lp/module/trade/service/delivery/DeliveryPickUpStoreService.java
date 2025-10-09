package com.star.lp.module.trade.service.delivery;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.trade.controller.admin.delivery.vo.pickup.*;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreDO;

import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;

/**
 * 自提门店 Service 接口
 *
 * @author jason
 */
public interface DeliveryPickUpStoreService {

    /**
     * 创建自提门店
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDeliveryPickUpStore(@Valid DeliveryPickUpStoreCreateReqVO createReqVO);

    /**
     * 更新自提门店
     *
     * @param updateReqVO 更新信息
     */
    void updateDeliveryPickUpStore(@Valid DeliveryPickUpStoreUpdateReqVO updateReqVO);

    /**
     * 删除自提门店
     *
     * @param id 编号
     */
    void deleteDeliveryPickUpStore(Long id);

    /**
     * 获得自提门店
     *
     * @param id 编号
     * @return 自提门店
     */
    DeliveryPickUpStoreDO getDeliveryPickUpStore(Long id);

    /**
     * 获得自提门店列表
     *
     * @param ids 编号
     * @return 自提门店列表
     */
    List<DeliveryPickUpStoreDO> getDeliveryPickUpStoreList(Collection<Long> ids);

    /**
     * 获得指定状态的自提门店列表
     *
     * @param status 状态
     * @return 自提门店列表
     */
    List<DeliveryPickUpStoreDO> getDeliveryPickUpStoreListByStatus(Integer status);

    /**
     * 获得自提门店分页
     *
     * @param pageReqVO 分页查询
     * @return 自提门店分页
     */
    PageResult<DeliveryPickUpStoreDO> getDeliveryPickUpStorePage(DeliveryPickUpStorePageReqVO pageReqVO);

    /**
     * 绑定自提店员
     *
     * @param bindReqVO 绑定数据
     */
    void bindDeliveryPickUpStore(DeliveryPickUpBindReqVO bindReqVO);

    /**
     * 绑定订单操作员
     *
     * @param bindReqVO 绑定数据
     */
    void bindOperationDeliveryPickUpStore(DeliveryPickUpBindOperationReqVO bindReqVO);

    /**
     * 根据店铺运营用户ID获取门店信息
     *
     * @param userId 店铺运营用户ID
     * @return DeliveryPickUpStoreDO 自提点数据对象，包含自提点的详细信息
     */
    DeliveryPickUpStoreDO getDeliveryPickUpStoreByStoreOperationUserId(Long userId);
}
