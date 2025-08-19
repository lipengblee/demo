package cn.iocoder.yudao.module.trade.convert.config;

import cn.iocoder.yudao.module.trade.controller.admin.config.vo.TradeConfigRespVO;
import cn.iocoder.yudao.module.trade.controller.admin.config.vo.TradeConfigSaveReqVO;
import cn.iocoder.yudao.module.trade.controller.app.config.vo.AppTradeConfigRespVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.config.TradeConfigDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-18T17:52:03+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class TradeConfigConvertImpl implements TradeConfigConvert {

    @Override
    public TradeConfigDO convert(TradeConfigSaveReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        TradeConfigDO.TradeConfigDOBuilder tradeConfigDO = TradeConfigDO.builder();

        List<String> list = bean.getAfterSaleRefundReasons();
        if ( list != null ) {
            tradeConfigDO.afterSaleRefundReasons( new ArrayList<String>( list ) );
        }
        List<String> list1 = bean.getAfterSaleReturnReasons();
        if ( list1 != null ) {
            tradeConfigDO.afterSaleReturnReasons( new ArrayList<String>( list1 ) );
        }
        tradeConfigDO.deliveryExpressFreeEnabled( bean.getDeliveryExpressFreeEnabled() );
        tradeConfigDO.deliveryExpressFreePrice( bean.getDeliveryExpressFreePrice() );
        tradeConfigDO.deliveryPickUpEnabled( bean.getDeliveryPickUpEnabled() );
        tradeConfigDO.brokerageEnabled( bean.getBrokerageEnabled() );
        tradeConfigDO.brokerageEnabledCondition( bean.getBrokerageEnabledCondition() );
        tradeConfigDO.brokerageBindMode( bean.getBrokerageBindMode() );
        List<String> list2 = bean.getBrokeragePosterUrls();
        if ( list2 != null ) {
            tradeConfigDO.brokeragePosterUrls( new ArrayList<String>( list2 ) );
        }
        tradeConfigDO.brokerageFirstPercent( bean.getBrokerageFirstPercent() );
        tradeConfigDO.brokerageSecondPercent( bean.getBrokerageSecondPercent() );
        tradeConfigDO.brokerageWithdrawMinPrice( bean.getBrokerageWithdrawMinPrice() );
        tradeConfigDO.brokerageWithdrawFeePercent( bean.getBrokerageWithdrawFeePercent() );
        tradeConfigDO.brokerageFrozenDays( bean.getBrokerageFrozenDays() );
        List<Integer> list3 = bean.getBrokerageWithdrawTypes();
        if ( list3 != null ) {
            tradeConfigDO.brokerageWithdrawTypes( new ArrayList<Integer>( list3 ) );
        }

        return tradeConfigDO.build();
    }

    @Override
    public TradeConfigRespVO convert(TradeConfigDO bean) {
        if ( bean == null ) {
            return null;
        }

        TradeConfigRespVO tradeConfigRespVO = new TradeConfigRespVO();

        List<String> list = bean.getAfterSaleRefundReasons();
        if ( list != null ) {
            tradeConfigRespVO.setAfterSaleRefundReasons( new ArrayList<String>( list ) );
        }
        List<String> list1 = bean.getAfterSaleReturnReasons();
        if ( list1 != null ) {
            tradeConfigRespVO.setAfterSaleReturnReasons( new ArrayList<String>( list1 ) );
        }
        tradeConfigRespVO.setDeliveryExpressFreeEnabled( bean.getDeliveryExpressFreeEnabled() );
        tradeConfigRespVO.setDeliveryExpressFreePrice( bean.getDeliveryExpressFreePrice() );
        tradeConfigRespVO.setDeliveryPickUpEnabled( bean.getDeliveryPickUpEnabled() );
        tradeConfigRespVO.setBrokerageEnabled( bean.getBrokerageEnabled() );
        tradeConfigRespVO.setBrokerageEnabledCondition( bean.getBrokerageEnabledCondition() );
        tradeConfigRespVO.setBrokerageBindMode( bean.getBrokerageBindMode() );
        List<String> list2 = bean.getBrokeragePosterUrls();
        if ( list2 != null ) {
            tradeConfigRespVO.setBrokeragePosterUrls( new ArrayList<String>( list2 ) );
        }
        tradeConfigRespVO.setBrokerageFirstPercent( bean.getBrokerageFirstPercent() );
        tradeConfigRespVO.setBrokerageSecondPercent( bean.getBrokerageSecondPercent() );
        tradeConfigRespVO.setBrokerageWithdrawMinPrice( bean.getBrokerageWithdrawMinPrice() );
        tradeConfigRespVO.setBrokerageWithdrawFeePercent( bean.getBrokerageWithdrawFeePercent() );
        tradeConfigRespVO.setBrokerageFrozenDays( bean.getBrokerageFrozenDays() );
        List<Integer> list3 = bean.getBrokerageWithdrawTypes();
        if ( list3 != null ) {
            tradeConfigRespVO.setBrokerageWithdrawTypes( new ArrayList<Integer>( list3 ) );
        }
        tradeConfigRespVO.setId( bean.getId() );

        return tradeConfigRespVO;
    }

    @Override
    public AppTradeConfigRespVO convert02(TradeConfigDO tradeConfig) {
        if ( tradeConfig == null ) {
            return null;
        }

        AppTradeConfigRespVO appTradeConfigRespVO = new AppTradeConfigRespVO();

        appTradeConfigRespVO.setDeliveryPickUpEnabled( tradeConfig.getDeliveryPickUpEnabled() );
        List<String> list = tradeConfig.getAfterSaleRefundReasons();
        if ( list != null ) {
            appTradeConfigRespVO.setAfterSaleRefundReasons( new ArrayList<String>( list ) );
        }
        List<String> list1 = tradeConfig.getAfterSaleReturnReasons();
        if ( list1 != null ) {
            appTradeConfigRespVO.setAfterSaleReturnReasons( new ArrayList<String>( list1 ) );
        }
        List<String> list2 = tradeConfig.getBrokeragePosterUrls();
        if ( list2 != null ) {
            appTradeConfigRespVO.setBrokeragePosterUrls( new ArrayList<String>( list2 ) );
        }
        appTradeConfigRespVO.setBrokerageFrozenDays( tradeConfig.getBrokerageFrozenDays() );
        appTradeConfigRespVO.setBrokerageWithdrawMinPrice( tradeConfig.getBrokerageWithdrawMinPrice() );
        List<Integer> list3 = tradeConfig.getBrokerageWithdrawTypes();
        if ( list3 != null ) {
            appTradeConfigRespVO.setBrokerageWithdrawTypes( new ArrayList<Integer>( list3 ) );
        }

        return appTradeConfigRespVO;
    }
}
