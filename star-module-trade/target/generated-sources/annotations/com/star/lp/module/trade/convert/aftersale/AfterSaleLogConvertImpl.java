package com.star.lp.module.trade.convert.aftersale;

import com.star.lp.module.trade.dal.dataobject.aftersale.AfterSaleLogDO;
import com.star.lp.module.trade.service.aftersale.bo.AfterSaleLogCreateReqBO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-03T12:28:04+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class AfterSaleLogConvertImpl implements AfterSaleLogConvert {

    @Override
    public AfterSaleLogDO convert(AfterSaleLogCreateReqBO bean) {
        if ( bean == null ) {
            return null;
        }

        AfterSaleLogDO.AfterSaleLogDOBuilder afterSaleLogDO = AfterSaleLogDO.builder();

        afterSaleLogDO.userId( bean.getUserId() );
        afterSaleLogDO.userType( bean.getUserType() );
        afterSaleLogDO.afterSaleId( bean.getAfterSaleId() );
        afterSaleLogDO.beforeStatus( bean.getBeforeStatus() );
        afterSaleLogDO.afterStatus( bean.getAfterStatus() );
        afterSaleLogDO.operateType( bean.getOperateType() );
        afterSaleLogDO.content( bean.getContent() );

        return afterSaleLogDO.build();
    }
}
