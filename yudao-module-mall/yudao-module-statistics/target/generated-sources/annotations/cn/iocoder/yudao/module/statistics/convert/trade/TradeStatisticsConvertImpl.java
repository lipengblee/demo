package cn.iocoder.yudao.module.statistics.convert.trade;

import cn.iocoder.yudao.module.statistics.controller.admin.common.vo.DataComparisonRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.trade.vo.TradeOrderCountRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.trade.vo.TradeSummaryRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.trade.vo.TradeTrendSummaryExcelVO;
import cn.iocoder.yudao.module.statistics.controller.admin.trade.vo.TradeTrendSummaryRespVO;
import cn.iocoder.yudao.module.statistics.dal.dataobject.trade.TradeStatisticsDO;
import cn.iocoder.yudao.module.statistics.service.trade.bo.AfterSaleSummaryRespBO;
import cn.iocoder.yudao.module.statistics.service.trade.bo.TradeOrderSummaryRespBO;
import cn.iocoder.yudao.module.statistics.service.trade.bo.WalletSummaryRespBO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-18T17:52:11+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class TradeStatisticsConvertImpl implements TradeStatisticsConvert {

    @Override
    public DataComparisonRespVO<TradeSummaryRespVO> convert(TradeSummaryRespVO value, TradeSummaryRespVO reference) {
        if ( value == null && reference == null ) {
            return null;
        }

        DataComparisonRespVO<TradeSummaryRespVO> dataComparisonRespVO = new DataComparisonRespVO<TradeSummaryRespVO>();

        dataComparisonRespVO.setValue( value );
        dataComparisonRespVO.setReference( reference );

        return dataComparisonRespVO;
    }

    @Override
    public DataComparisonRespVO<TradeTrendSummaryRespVO> convert(TradeTrendSummaryRespVO value, TradeTrendSummaryRespVO reference) {
        if ( value == null && reference == null ) {
            return null;
        }

        DataComparisonRespVO<TradeTrendSummaryRespVO> dataComparisonRespVO = new DataComparisonRespVO<TradeTrendSummaryRespVO>();

        dataComparisonRespVO.setValue( value );
        dataComparisonRespVO.setReference( reference );

        return dataComparisonRespVO;
    }

    @Override
    public List<TradeTrendSummaryExcelVO> convertList02(List<TradeTrendSummaryRespVO> list) {
        if ( list == null ) {
            return null;
        }

        List<TradeTrendSummaryExcelVO> list1 = new ArrayList<TradeTrendSummaryExcelVO>( list.size() );
        for ( TradeTrendSummaryRespVO tradeTrendSummaryRespVO : list ) {
            list1.add( tradeTrendSummaryRespVOToTradeTrendSummaryExcelVO( tradeTrendSummaryRespVO ) );
        }

        return list1;
    }

    @Override
    public TradeStatisticsDO convert(LocalDateTime time, TradeOrderSummaryRespBO orderSummary, AfterSaleSummaryRespBO afterSaleSummary, Integer brokerageSettlementPrice, WalletSummaryRespBO walletSummary) {
        if ( time == null && orderSummary == null && afterSaleSummary == null && brokerageSettlementPrice == null && walletSummary == null ) {
            return null;
        }

        TradeStatisticsDO.TradeStatisticsDOBuilder tradeStatisticsDO = TradeStatisticsDO.builder();

        if ( orderSummary != null ) {
            tradeStatisticsDO.orderCreateCount( orderSummary.getOrderCreateCount() );
            tradeStatisticsDO.orderPayCount( orderSummary.getOrderPayCount() );
            tradeStatisticsDO.orderPayPrice( orderSummary.getOrderPayPrice() );
        }
        if ( afterSaleSummary != null ) {
            tradeStatisticsDO.afterSaleCount( afterSaleSummary.getAfterSaleCount() );
            tradeStatisticsDO.afterSaleRefundPrice( afterSaleSummary.getAfterSaleRefundPrice() );
        }
        if ( walletSummary != null ) {
            tradeStatisticsDO.walletPayPrice( walletSummary.getWalletPayPrice() );
            tradeStatisticsDO.rechargePayCount( walletSummary.getRechargePayCount() );
            tradeStatisticsDO.rechargePayPrice( walletSummary.getRechargePayPrice() );
            tradeStatisticsDO.rechargeRefundCount( walletSummary.getRechargeRefundCount() );
            tradeStatisticsDO.rechargeRefundPrice( walletSummary.getRechargeRefundPrice() );
        }
        tradeStatisticsDO.time( time );
        tradeStatisticsDO.brokerageSettlementPrice( brokerageSettlementPrice );

        return tradeStatisticsDO.build();
    }

    @Override
    public List<TradeTrendSummaryRespVO> convertList(List<TradeStatisticsDO> list) {
        if ( list == null ) {
            return null;
        }

        List<TradeTrendSummaryRespVO> list1 = new ArrayList<TradeTrendSummaryRespVO>( list.size() );
        for ( TradeStatisticsDO tradeStatisticsDO : list ) {
            list1.add( convert( tradeStatisticsDO ) );
        }

        return list1;
    }

    @Override
    public TradeTrendSummaryRespVO convertA(TradeStatisticsDO tradeStatistics) {
        if ( tradeStatistics == null ) {
            return null;
        }

        TradeTrendSummaryRespVO tradeTrendSummaryRespVO = new TradeTrendSummaryRespVO();

        tradeTrendSummaryRespVO.setOrderPayPrice( tradeStatistics.getOrderPayPrice() );
        tradeTrendSummaryRespVO.setWalletPayPrice( tradeStatistics.getWalletPayPrice() );
        tradeTrendSummaryRespVO.setAfterSaleRefundPrice( tradeStatistics.getAfterSaleRefundPrice() );
        tradeTrendSummaryRespVO.setBrokerageSettlementPrice( tradeStatistics.getBrokerageSettlementPrice() );

        return tradeTrendSummaryRespVO;
    }

    @Override
    public TradeOrderCountRespVO convert(Long undelivered, Long pickUp, Long afterSaleApply, Long auditingWithdraw) {
        if ( undelivered == null && pickUp == null && afterSaleApply == null && auditingWithdraw == null ) {
            return null;
        }

        TradeOrderCountRespVO tradeOrderCountRespVO = new TradeOrderCountRespVO();

        tradeOrderCountRespVO.setUndelivered( undelivered );
        tradeOrderCountRespVO.setPickUp( pickUp );
        tradeOrderCountRespVO.setAfterSaleApply( afterSaleApply );
        tradeOrderCountRespVO.setAuditingWithdraw( auditingWithdraw );

        return tradeOrderCountRespVO;
    }

    protected TradeTrendSummaryExcelVO tradeTrendSummaryRespVOToTradeTrendSummaryExcelVO(TradeTrendSummaryRespVO tradeTrendSummaryRespVO) {
        if ( tradeTrendSummaryRespVO == null ) {
            return null;
        }

        TradeTrendSummaryExcelVO tradeTrendSummaryExcelVO = new TradeTrendSummaryExcelVO();

        tradeTrendSummaryExcelVO.setDate( tradeTrendSummaryRespVO.getDate() );
        tradeTrendSummaryExcelVO.setTurnoverPrice( tradeTrendSummaryRespVO.getTurnoverPrice() );
        tradeTrendSummaryExcelVO.setOrderPayPrice( tradeTrendSummaryRespVO.getOrderPayPrice() );
        tradeTrendSummaryExcelVO.setRechargePrice( tradeTrendSummaryRespVO.getRechargePrice() );
        tradeTrendSummaryExcelVO.setExpensePrice( tradeTrendSummaryRespVO.getExpensePrice() );
        tradeTrendSummaryExcelVO.setWalletPayPrice( tradeTrendSummaryRespVO.getWalletPayPrice() );
        tradeTrendSummaryExcelVO.setBrokerageSettlementPrice( tradeTrendSummaryRespVO.getBrokerageSettlementPrice() );
        tradeTrendSummaryExcelVO.setAfterSaleRefundPrice( tradeTrendSummaryRespVO.getAfterSaleRefundPrice() );

        return tradeTrendSummaryExcelVO;
    }
}
