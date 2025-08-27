package com.star.lp.module.product.convert.favorite;

import com.star.lp.module.product.controller.admin.favorite.vo.ProductFavoriteRespVO;
import com.star.lp.module.product.controller.app.favorite.vo.AppFavoriteRespVO;
import com.star.lp.module.product.dal.dataobject.favorite.ProductFavoriteDO;
import com.star.lp.module.product.dal.dataobject.spu.ProductSpuDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-26T17:04:58+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class ProductFavoriteConvertImpl implements ProductFavoriteConvert {

    @Override
    public ProductFavoriteDO convert(Long userId, Long spuId) {
        if ( userId == null && spuId == null ) {
            return null;
        }

        ProductFavoriteDO.ProductFavoriteDOBuilder productFavoriteDO = ProductFavoriteDO.builder();

        productFavoriteDO.userId( userId );
        productFavoriteDO.spuId( spuId );

        return productFavoriteDO.build();
    }

    @Override
    public AppFavoriteRespVO convert(ProductSpuDO spu, ProductFavoriteDO favorite) {
        if ( spu == null && favorite == null ) {
            return null;
        }

        AppFavoriteRespVO appFavoriteRespVO = new AppFavoriteRespVO();

        if ( spu != null ) {
            appFavoriteRespVO.setSpuName( spu.getName() );
            appFavoriteRespVO.setPicUrl( spu.getPicUrl() );
            appFavoriteRespVO.setPrice( spu.getPrice() );
        }
        if ( favorite != null ) {
            appFavoriteRespVO.setId( favorite.getId() );
            appFavoriteRespVO.setSpuId( favorite.getSpuId() );
        }

        return appFavoriteRespVO;
    }

    @Override
    public ProductFavoriteRespVO convert02(ProductSpuDO spu, ProductFavoriteDO favorite) {
        if ( spu == null && favorite == null ) {
            return null;
        }

        ProductFavoriteRespVO productFavoriteRespVO = new ProductFavoriteRespVO();

        if ( spu != null ) {
            productFavoriteRespVO.setName( spu.getName() );
            productFavoriteRespVO.setKeyword( spu.getKeyword() );
            productFavoriteRespVO.setIntroduction( spu.getIntroduction() );
            productFavoriteRespVO.setDescription( spu.getDescription() );
            productFavoriteRespVO.setCategoryId( spu.getCategoryId() );
            productFavoriteRespVO.setBrandId( spu.getBrandId() );
            productFavoriteRespVO.setPicUrl( spu.getPicUrl() );
            List<String> list = spu.getSliderPicUrls();
            if ( list != null ) {
                productFavoriteRespVO.setSliderPicUrls( new ArrayList<String>( list ) );
            }
            productFavoriteRespVO.setSort( spu.getSort() );
            productFavoriteRespVO.setStatus( spu.getStatus() );
            productFavoriteRespVO.setSpecType( spu.getSpecType() );
            productFavoriteRespVO.setPrice( spu.getPrice() );
            productFavoriteRespVO.setMarketPrice( spu.getMarketPrice() );
            productFavoriteRespVO.setCostPrice( spu.getCostPrice() );
            productFavoriteRespVO.setStock( spu.getStock() );
            List<Integer> list1 = spu.getDeliveryTypes();
            if ( list1 != null ) {
                productFavoriteRespVO.setDeliveryTypes( new ArrayList<Integer>( list1 ) );
            }
            productFavoriteRespVO.setDeliveryTemplateId( spu.getDeliveryTemplateId() );
            productFavoriteRespVO.setGiveIntegral( spu.getGiveIntegral() );
            productFavoriteRespVO.setSubCommissionType( spu.getSubCommissionType() );
            productFavoriteRespVO.setSalesCount( spu.getSalesCount() );
            productFavoriteRespVO.setVirtualSalesCount( spu.getVirtualSalesCount() );
            productFavoriteRespVO.setBrowseCount( spu.getBrowseCount() );
        }
        if ( favorite != null ) {
            productFavoriteRespVO.setId( favorite.getId() );
            productFavoriteRespVO.setUserId( favorite.getUserId() );
            productFavoriteRespVO.setSpuId( favorite.getSpuId() );
            productFavoriteRespVO.setCreateTime( favorite.getCreateTime() );
        }

        return productFavoriteRespVO;
    }
}
