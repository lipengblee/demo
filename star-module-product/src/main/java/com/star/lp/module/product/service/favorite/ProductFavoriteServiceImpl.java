package com.star.lp.module.product.service.favorite;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.product.controller.admin.favorite.vo.ProductFavoritePageReqVO;
import com.star.lp.module.product.controller.app.favorite.vo.AppFavoritePageReqVO;
import com.star.lp.module.product.convert.favorite.ProductFavoriteConvert;
import com.star.lp.module.product.dal.dataobject.favorite.ProductFavoriteDO;
import com.star.lp.module.product.dal.mysql.favorite.ProductFavoriteMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import static com.star.lp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.star.lp.module.product.enums.ErrorCodeConstants.FAVORITE_EXISTS;
import static com.star.lp.module.product.enums.ErrorCodeConstants.FAVORITE_NOT_EXISTS;

/**
 * 商品收藏 Service 实现类
 *
 * @author jason
 */
@Service
@Validated
public class ProductFavoriteServiceImpl implements ProductFavoriteService {

    @Resource
    private ProductFavoriteMapper productFavoriteMapper;

    @Override
    public Long createFavorite(Long userId, Long spuId) {
        ProductFavoriteDO favorite = productFavoriteMapper.selectByUserIdAndSpuId(userId, spuId);
        if (favorite != null) {
            throw exception(FAVORITE_EXISTS);
        }

        ProductFavoriteDO entity = ProductFavoriteConvert.INSTANCE.convert(userId, spuId);
        productFavoriteMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void deleteFavorite(Long userId, Long spuId) {
        ProductFavoriteDO favorite = productFavoriteMapper.selectByUserIdAndSpuId(userId, spuId);
        if (favorite == null) {
            throw exception(FAVORITE_NOT_EXISTS);
        }

        productFavoriteMapper.deleteById(favorite.getId());
    }

    @Override
    public PageResult<ProductFavoriteDO> getFavoritePage(Long userId, @Valid AppFavoritePageReqVO reqVO) {
        return productFavoriteMapper.selectPageByUserAndType(userId, reqVO);
    }

    @Override
    public PageResult<ProductFavoriteDO> getFavoritePage(@Valid ProductFavoritePageReqVO reqVO) {
        return productFavoriteMapper.selectPageByUserId(reqVO);
    }

    @Override
    public ProductFavoriteDO getFavorite(Long userId, Long spuId) {
        return productFavoriteMapper.selectByUserIdAndSpuId(userId, spuId);
    }

    @Override
    public Long getFavoriteCount(Long userId) {
        return productFavoriteMapper.selectCountByUserId(userId);
    }

}
