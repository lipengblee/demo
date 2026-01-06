package com.star.lp.module.trade.service.delivery.impl;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.star.lp.module.trade.controller.admin.delivery.vo.logistics.*;
import com.star.lp.module.trade.convert.delivery.DeliveryPickUpStoreLogisticsConfigConvert;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreLogisticsConfigDO;
import com.star.lp.module.trade.dal.mysql.delivery.DeliveryPickUpStoreLogisticsConfigMapper;
import com.star.lp.module.trade.service.delivery.DeliveryPickUpStoreLogisticsConfigService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.star.lp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.star.lp.module.trade.enums.ErrorCodeConstants.*;

/**
 * 自提门店物流配置 Service 实现类
 *
 * @author star
 */
@Service
@Validated
public class DeliveryPickUpStoreLogisticsConfigServiceImpl implements DeliveryPickUpStoreLogisticsConfigService {

    @Resource
    private DeliveryPickUpStoreLogisticsConfigMapper logisticsConfigMapper;

    @Override
    public Long createDeliveryPickUpStoreLogisticsConfig(DeliveryPickUpStoreLogisticsConfigCreateReqVO createReqVO) {
        // 校验门店是否存在
        validateDeliveryPickUpStoreExists(createReqVO.getStoreId());

        // 校验物流名称是否重复
        validateLogisticsNameUnique(createReqVO.getStoreId(), createReqVO.getLogisticsName(), null);

        // 插入
        DeliveryPickUpStoreLogisticsConfigDO logisticsConfig = DeliveryPickUpStoreLogisticsConfigConvert.INSTANCE.convert(createReqVO);
        logisticsConfigMapper.insert(logisticsConfig);

        // 返回
        return logisticsConfig.getId();
    }

    @Override
    public void updateDeliveryPickUpStoreLogisticsConfig(DeliveryPickUpStoreLogisticsConfigUpdateReqVO updateReqVO) {
        // 校验存在
        validateDeliveryPickUpStoreLogisticsConfigExists(updateReqVO.getId());
        // 校验门店是否存在
        validateDeliveryPickUpStoreExists(updateReqVO.getStoreId());
        // 校验物流名称是否重复
        validateLogisticsNameUnique(updateReqVO.getStoreId(), updateReqVO.getLogisticsName(), updateReqVO.getId());

        // 更新
        DeliveryPickUpStoreLogisticsConfigDO updateObj = DeliveryPickUpStoreLogisticsConfigConvert.INSTANCE.convert(updateReqVO);
        logisticsConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteDeliveryPickUpStoreLogisticsConfig(Long id) {
        // 校验存在
        validateDeliveryPickUpStoreLogisticsConfigExists(id);

        // 删除
        logisticsConfigMapper.deleteById(id);
    }

    private void validateDeliveryPickUpStoreLogisticsConfigExists(Long id) {
        if (logisticsConfigMapper.selectById(id) == null) {
            throw exception(DELIVERY_PICK_UP_STORE_LOGISTICS_CONFIG_NOT_EXISTS);
        }
    }

    private void validateDeliveryPickUpStoreExists(Long storeId) {
        // 这里需要调用DeliveryPickUpStoreService来验证门店是否存在
        // 由于当前是独立模块，这里暂时不做实现，实际使用时需要注入并调用
    }

    private void validateLogisticsNameUnique(Long storeId, String logisticsName, Long excludeId) {
        DeliveryPickUpStoreLogisticsConfigDO config = logisticsConfigMapper.selectOne(
            new LambdaQueryWrapperX<DeliveryPickUpStoreLogisticsConfigDO>()
                .eq(DeliveryPickUpStoreLogisticsConfigDO::getStoreId, storeId)
                .eq(DeliveryPickUpStoreLogisticsConfigDO::getLogisticsName, logisticsName)
        );

        if (config != null && !config.getId().equals(excludeId)) {
            throw exception(DELIVERY_PICK_UP_STORE_LOGISTICS_CONFIG_LOGISTICS_NAME_DUPLICATE);
        }
    }

    @Override
    public DeliveryPickUpStoreLogisticsConfigDO getDeliveryPickUpStoreLogisticsConfig(Long id) {
        return logisticsConfigMapper.selectById(id);
    }

    @Override
    public List<DeliveryPickUpStoreLogisticsConfigDO> getDeliveryPickUpStoreLogisticsConfigList(Collection<Long> ids) {
        return logisticsConfigMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<DeliveryPickUpStoreLogisticsConfigDO> getDeliveryPickUpStoreLogisticsConfigPage(DeliveryPickUpStoreLogisticsConfigPageReqVO pageReqVO) {
        return logisticsConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DeliveryPickUpStoreLogisticsConfigDO> getLogisticsConfigListByStoreId(Long storeId) {
        return logisticsConfigMapper.selectList(
            new LambdaQueryWrapperX<DeliveryPickUpStoreLogisticsConfigDO>()
                .eq(DeliveryPickUpStoreLogisticsConfigDO::getStoreId, storeId)
                .orderByDesc(DeliveryPickUpStoreLogisticsConfigDO::getId)
        );
    }

    @Override
    public DeliveryPickUpStoreLogisticsConfigDO getLogisticsConfigByStoreIdAndLogisticsName(Long storeId, String logisticsName) {
        return logisticsConfigMapper.selectOne(
            new LambdaQueryWrapperX<DeliveryPickUpStoreLogisticsConfigDO>()
                .eq(DeliveryPickUpStoreLogisticsConfigDO::getStoreId, storeId)
                .eq(DeliveryPickUpStoreLogisticsConfigDO::getLogisticsName, logisticsName)
        );
    }

}
