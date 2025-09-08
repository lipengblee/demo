package com.star.lp.module.trade.convert.order;

import com.star.lp.module.trade.dal.dataobject.order.TradeOrderLogDO;
import com.star.lp.module.trade.service.order.bo.TradeOrderLogCreateReqBO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-03T13:36:43+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class TradeOrderLogConvertImpl implements TradeOrderLogConvert {

    @Override
    public TradeOrderLogDO convert(TradeOrderLogCreateReqBO bean) {
        if ( bean == null ) {
            return null;
        }

        TradeOrderLogDO.TradeOrderLogDOBuilder tradeOrderLogDO = TradeOrderLogDO.builder();

        tradeOrderLogDO.userId( bean.getUserId() );
        tradeOrderLogDO.userType( bean.getUserType() );
        tradeOrderLogDO.orderId( bean.getOrderId() );
        tradeOrderLogDO.beforeStatus( bean.getBeforeStatus() );
        tradeOrderLogDO.afterStatus( bean.getAfterStatus() );
        tradeOrderLogDO.operateType( bean.getOperateType() );
        tradeOrderLogDO.content( bean.getContent() );

        return tradeOrderLogDO.build();
    }
}
