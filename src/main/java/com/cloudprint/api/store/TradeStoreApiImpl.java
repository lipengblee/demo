package com.star.lp.module.trade.api.store;

import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreDO;
import com.star.lp.module.trade.service.delivery.DeliveryPickUpStoreService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.star.lp.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Service
@Validated
public class TradeStoreApiImpl implements TradeStoreApi{

    @Resource
    private DeliveryPickUpStoreService deliveryPickUpStoreService;
    @Override
    public boolean checkUserStoreRule() {
        Long userId = getLoginUserId();
        DeliveryPickUpStoreDO deliveryPickUpStore =  deliveryPickUpStoreService.getDeliveryPickUpStoreByStoreOperationUserId(userId);
        if(deliveryPickUpStore != null){
            return true;
        }
        return false;
    }
}
