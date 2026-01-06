package com.star.lp.module.trade.convert.delivery;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.trade.controller.admin.delivery.vo.logistics.*;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreLogisticsConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 自提门店物流配置 Convert
 *
 * @author star
 */
@Mapper
public interface DeliveryPickUpStoreLogisticsConfigConvert {

    DeliveryPickUpStoreLogisticsConfigConvert INSTANCE = Mappers.getMapper(DeliveryPickUpStoreLogisticsConfigConvert.class);

    DeliveryPickUpStoreLogisticsConfigDO convert(DeliveryPickUpStoreLogisticsConfigCreateReqVO bean);

    DeliveryPickUpStoreLogisticsConfigDO convert(DeliveryPickUpStoreLogisticsConfigUpdateReqVO bean);

    DeliveryPickUpStoreLogisticsConfigRespVO convert(DeliveryPickUpStoreLogisticsConfigDO bean);

    List<DeliveryPickUpStoreLogisticsConfigRespVO> convertList(List<DeliveryPickUpStoreLogisticsConfigDO> list);

    PageResult<DeliveryPickUpStoreLogisticsConfigRespVO> convertPage(PageResult<DeliveryPickUpStoreLogisticsConfigDO> page);

}
