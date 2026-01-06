package com.star.lp.module.trade.dal.mysql.delivery;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.star.lp.framework.mybatis.core.mapper.BaseMapperX;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreLogisticsConfigDO;
import com.star.lp.module.trade.controller.admin.delivery.vo.logistics.DeliveryPickUpStoreLogisticsConfigPageReqVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 自提门店物流配置 Mapper
 *
 * @author star
 */
@Mapper
public interface DeliveryPickUpStoreLogisticsConfigMapper extends BaseMapperX<DeliveryPickUpStoreLogisticsConfigDO> {

    default PageResult<DeliveryPickUpStoreLogisticsConfigDO> selectPage(DeliveryPickUpStoreLogisticsConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DeliveryPickUpStoreLogisticsConfigDO>()
                .eqIfPresent(DeliveryPickUpStoreLogisticsConfigDO::getStoreId, reqVO.getStoreId())
                .likeIfPresent(DeliveryPickUpStoreLogisticsConfigDO::getLogisticsName, reqVO.getLogisticsName())
                .likeIfPresent(DeliveryPickUpStoreLogisticsConfigDO::getSenderName, reqVO.getSenderName())
                .likeIfPresent(DeliveryPickUpStoreLogisticsConfigDO::getSenderPhone, reqVO.getSenderPhone())
                .likeIfPresent(DeliveryPickUpStoreLogisticsConfigDO::getSenderProvince, reqVO.getSenderProvince())
                .likeIfPresent(DeliveryPickUpStoreLogisticsConfigDO::getSenderCity, reqVO.getSenderCity())
                .likeIfPresent(DeliveryPickUpStoreLogisticsConfigDO::getSenderDistrict, reqVO.getSenderDistrict())
                .orderByDesc(DeliveryPickUpStoreLogisticsConfigDO::getId));
    }

}
