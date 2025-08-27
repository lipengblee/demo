package com.star.lp.module.product.dal.mysql.property;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.mybatis.core.mapper.BaseMapperX;
import com.star.lp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.star.lp.module.product.controller.admin.property.vo.property.ProductPropertyPageReqVO;
import com.star.lp.module.product.dal.dataobject.property.ProductPropertyDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductPropertyMapper extends BaseMapperX<ProductPropertyDO> {

    default PageResult<ProductPropertyDO> selectPage(ProductPropertyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductPropertyDO>()
                .likeIfPresent(ProductPropertyDO::getName, reqVO.getName())
                .betweenIfPresent(ProductPropertyDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductPropertyDO::getId));
    }

    default ProductPropertyDO selectByName(String name) {
        return selectOne(ProductPropertyDO::getName, name);
    }

    /**
     * 查询指定商品类型的属性列表
     * @param productType 商品类型，用于筛选特定类型的属性
     * @return 属性列表
     */
    default List<ProductPropertyDO> selectListByProductType(Integer productType) {
        return selectList(new LambdaQueryWrapperX<ProductPropertyDO>()
                .eq(ProductPropertyDO::getProductType, productType)
                .orderByAsc(ProductPropertyDO::getSort));
    }
}
