package com.star.lp.module.promotion.api.discount;

import com.star.lp.framework.common.util.object.BeanUtils;
import com.star.lp.module.promotion.api.discount.dto.DiscountProductRespDTO;
import com.star.lp.module.promotion.dal.dataobject.discount.DiscountProductDO;
import com.star.lp.module.promotion.service.discount.DiscountActivityService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;

/**
 * 限时折扣 API 实现类
 *
 *
 */
@Service
@Validated
public class DiscountActivityApiImpl implements DiscountActivityApi {

    @Resource
    private DiscountActivityService discountActivityService;

    @Override
    public List<DiscountProductRespDTO> getMatchDiscountProductListBySkuIds(Collection<Long> skuIds) {
        List<DiscountProductDO> list = discountActivityService.getMatchDiscountProductListBySkuIds(skuIds);
        return BeanUtils.toBean(list, DiscountProductRespDTO.class);
    }

}
