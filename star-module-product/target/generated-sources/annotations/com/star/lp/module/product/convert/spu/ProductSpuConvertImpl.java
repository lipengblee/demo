package com.star.lp.module.product.convert.spu;

import com.star.lp.module.product.controller.admin.spu.vo.ProductSpuPageReqVO;
import com.star.lp.module.product.controller.app.spu.vo.AppProductSpuPageReqVO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-26T17:04:57+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class ProductSpuConvertImpl implements ProductSpuConvert {

    @Override
    public ProductSpuPageReqVO convert(AppProductSpuPageReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        ProductSpuPageReqVO productSpuPageReqVO = new ProductSpuPageReqVO();

        productSpuPageReqVO.setPageNo( bean.getPageNo() );
        productSpuPageReqVO.setPageSize( bean.getPageSize() );
        productSpuPageReqVO.setCategoryId( bean.getCategoryId() );

        return productSpuPageReqVO;
    }
}
