package com.star.lp.module.trade.service.delivery;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.trade.controller.admin.delivery.vo.logistics.*;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreLogisticsConfigDO;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 自提门店物流配置 Service 接口
 *
 * @author star
 */
public interface DeliveryPickUpStoreLogisticsConfigService {

    /**
     * 创建自提门店物流配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDeliveryPickUpStoreLogisticsConfig(@Valid DeliveryPickUpStoreLogisticsConfigCreateReqVO createReqVO);

    /**
     * 更新自提门店物流配置
     *
     * @param updateReqVO 更新信息
     */
    void updateDeliveryPickUpStoreLogisticsConfig(@Valid DeliveryPickUpStoreLogisticsConfigUpdateReqVO updateReqVO);

    /**
     * 删除自提门店物流配置
     *
     * @param id 编号
     */
    void deleteDeliveryPickUpStoreLogisticsConfig(Long id);

    /**
     * 获得自提门店物流配置
     *
     * @param id 编号
     * @return 自提门店物流配置
     */
    DeliveryPickUpStoreLogisticsConfigDO getDeliveryPickUpStoreLogisticsConfig(Long id);

    /**
     * 获得自提门店物流配置列表
     *
     * @param ids 编号
     * @return 自提门店物流配置列表
     */
    List<DeliveryPickUpStoreLogisticsConfigDO> getDeliveryPickUpStoreLogisticsConfigList(Collection<Long> ids);

    /**
     * 获得自提门店物流配置分页
     *
     * @param pageReqVO 分页查询
     * @return 自提门店物流配置分页
     */
    PageResult<DeliveryPickUpStoreLogisticsConfigDO> getDeliveryPickUpStoreLogisticsConfigPage(DeliveryPickUpStoreLogisticsConfigPageReqVO pageReqVO);

    /**
     * 根据门店ID获取物流配置列表
     *
     * @param storeId 门店ID
     * @return 物流配置列表
     */
    List<DeliveryPickUpStoreLogisticsConfigDO> getLogisticsConfigListByStoreId(Long storeId);

    /**
     * 根据门店ID和物流名称获取物流配置
     *
     * @param storeId 门店ID
     * @param logisticsName 物流名称
     * @return 物流配置
     */
    DeliveryPickUpStoreLogisticsConfigDO getLogisticsConfigByStoreIdAndLogisticsName(Long storeId, String logisticsName);
}
