package com.star.lp.module.trade.convert.order;

import com.star.lp.module.member.api.address.dto.MemberAddressRespDTO;
import com.star.lp.module.member.api.user.dto.MemberUserRespDTO;
import com.star.lp.module.product.api.comment.dto.ProductCommentCreateReqDTO;
import com.star.lp.module.product.api.property.dto.ProductPropertyValueDetailRespDTO;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentDO;
import com.star.lp.module.promotion.api.combination.dto.CombinationRecordCreateReqDTO;
import com.star.lp.module.trade.api.order.dto.TradeOrderRespDTO;
import com.star.lp.module.trade.controller.admin.base.member.user.MemberUserRespVO;
import com.star.lp.module.trade.controller.admin.base.product.property.ProductPropertyValueDetailRespVO;
import com.star.lp.module.trade.controller.admin.order.vo.TradeOrderAppointStoreIdReqVO;
import com.star.lp.module.trade.controller.admin.order.vo.TradeOrderDetailRespVO;
import com.star.lp.module.trade.controller.admin.order.vo.TradeOrderPageItemRespVO;
import com.star.lp.module.trade.controller.admin.order.vo.TradeOrderRemarkReqVO;
import com.star.lp.module.trade.controller.admin.order.vo.TradeOrderUpdateAddressReqVO;
import com.star.lp.module.trade.controller.admin.order.vo.TradeOrderUpdatePriceReqVO;
import com.star.lp.module.trade.controller.app.base.property.AppProductPropertyValueDetailRespVO;
import com.star.lp.module.trade.controller.app.order.vo.AppOrderExpressTrackRespDTO;
import com.star.lp.module.trade.controller.app.order.vo.AppTradeOrderCreateReqVO;
import com.star.lp.module.trade.controller.app.order.vo.AppTradeOrderDetailRespVO;
import com.star.lp.module.trade.controller.app.order.vo.AppTradeOrderPageItemRespVO;
import com.star.lp.module.trade.controller.app.order.vo.AppTradeOrderSettlementReqVO;
import com.star.lp.module.trade.controller.app.order.vo.AppTradeOrderSettlementRespVO;
import com.star.lp.module.trade.controller.app.order.vo.item.AppTradeOrderItemCommentCreateReqVO;
import com.star.lp.module.trade.controller.app.order.vo.item.AppTradeOrderItemRespVO;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryExpressDO;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderDO;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderItemDO;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderLogDO;
import com.star.lp.module.trade.framework.delivery.core.client.dto.ExpressTrackRespDTO;
import com.star.lp.module.trade.framework.order.config.TradeOrderProperties;
import com.star.lp.module.trade.service.price.bo.TradePriceCalculateReqBO;
import com.star.lp.module.trade.service.price.bo.TradePriceCalculateRespBO;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-01T17:06:20+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class TradeOrderConvertImpl implements TradeOrderConvert {

    @Override
    public TradeOrderDO convert(Long userId, AppTradeOrderCreateReqVO createReqVO, TradePriceCalculateRespBO calculateRespBO) {
        if ( userId == null && createReqVO == null && calculateRespBO == null ) {
            return null;
        }

        TradeOrderDO.TradeOrderDOBuilder tradeOrderDO = TradeOrderDO.builder();

        if ( createReqVO != null ) {
            tradeOrderDO.couponId( createReqVO.getCouponId() );
            tradeOrderDO.userRemark( createReqVO.getRemark() );
            tradeOrderDO.deliveryType( createReqVO.getDeliveryType() );
            tradeOrderDO.receiverName( createReqVO.getReceiverName() );
            tradeOrderDO.receiverMobile( createReqVO.getReceiverMobile() );
            tradeOrderDO.pickUpStoreId( createReqVO.getPickUpStoreId() );
            tradeOrderDO.seckillActivityId( createReqVO.getSeckillActivityId() );
            tradeOrderDO.bargainRecordId( createReqVO.getBargainRecordId() );
            tradeOrderDO.combinationActivityId( createReqVO.getCombinationActivityId() );
            tradeOrderDO.combinationHeadId( createReqVO.getCombinationHeadId() );
            tradeOrderDO.pointActivityId( createReqVO.getPointActivityId() );
        }
        if ( calculateRespBO != null ) {
            tradeOrderDO.totalPrice( calculateRespBOPriceTotalPrice( calculateRespBO ) );
            tradeOrderDO.discountPrice( calculateRespBOPriceDiscountPrice( calculateRespBO ) );
            tradeOrderDO.deliveryPrice( calculateRespBOPriceDeliveryPrice( calculateRespBO ) );
            tradeOrderDO.couponPrice( calculateRespBOPriceCouponPrice( calculateRespBO ) );
            tradeOrderDO.pointPrice( calculateRespBOPricePointPrice( calculateRespBO ) );
            tradeOrderDO.vipPrice( calculateRespBOPriceVipPrice( calculateRespBO ) );
            tradeOrderDO.payPrice( calculateRespBOPricePayPrice( calculateRespBO ) );
            tradeOrderDO.type( calculateRespBO.getType() );
            tradeOrderDO.usePoint( calculateRespBO.getUsePoint() );
            tradeOrderDO.givePoint( calculateRespBO.getGivePoint() );
            Map<Long, Integer> map = calculateRespBO.getGiveCouponTemplateCounts();
            if ( map != null ) {
                tradeOrderDO.giveCouponTemplateCounts( new LinkedHashMap<Long, Integer>( map ) );
            }
            tradeOrderDO.bargainActivityId( calculateRespBO.getBargainActivityId() );
        }
        tradeOrderDO.userId( userId );

        return tradeOrderDO.build();
    }

    @Override
    public TradeOrderRespDTO convert(TradeOrderDO orderDO) {
        if ( orderDO == null ) {
            return null;
        }

        TradeOrderRespDTO tradeOrderRespDTO = new TradeOrderRespDTO();

        tradeOrderRespDTO.setId( orderDO.getId() );
        tradeOrderRespDTO.setNo( orderDO.getNo() );
        tradeOrderRespDTO.setType( orderDO.getType() );
        tradeOrderRespDTO.setTerminal( orderDO.getTerminal() );
        tradeOrderRespDTO.setUserId( orderDO.getUserId() );
        tradeOrderRespDTO.setUserIp( orderDO.getUserIp() );
        tradeOrderRespDTO.setUserRemark( orderDO.getUserRemark() );
        tradeOrderRespDTO.setStatus( orderDO.getStatus() );
        tradeOrderRespDTO.setProductCount( orderDO.getProductCount() );
        tradeOrderRespDTO.setFinishTime( orderDO.getFinishTime() );
        tradeOrderRespDTO.setCancelTime( orderDO.getCancelTime() );
        tradeOrderRespDTO.setCancelType( orderDO.getCancelType() );
        tradeOrderRespDTO.setRemark( orderDO.getRemark() );
        tradeOrderRespDTO.setCommentStatus( orderDO.getCommentStatus() );
        tradeOrderRespDTO.setPayOrderId( orderDO.getPayOrderId() );
        tradeOrderRespDTO.setPayStatus( orderDO.getPayStatus() );

        return tradeOrderRespDTO;
    }

    @Override
    public TradeOrderItemDO convert(TradePriceCalculateRespBO.OrderItem item) {
        if ( item == null ) {
            return null;
        }

        TradeOrderItemDO tradeOrderItemDO = new TradeOrderItemDO();

        tradeOrderItemDO.setCartId( item.getCartId() );
        tradeOrderItemDO.setSpuId( item.getSpuId() );
        tradeOrderItemDO.setSpuName( item.getSpuName() );
        tradeOrderItemDO.setSkuId( item.getSkuId() );
        tradeOrderItemDO.setProperties( productPropertyValueDetailRespDTOListToPropertyList( item.getProperties() ) );
        tradeOrderItemDO.setPicUrl( item.getPicUrl() );
        tradeOrderItemDO.setCount( item.getCount() );
        tradeOrderItemDO.setPrice( item.getPrice() );
        tradeOrderItemDO.setDiscountPrice( item.getDiscountPrice() );
        tradeOrderItemDO.setDeliveryPrice( item.getDeliveryPrice() );
        tradeOrderItemDO.setPayPrice( item.getPayPrice() );
        tradeOrderItemDO.setCouponPrice( item.getCouponPrice() );
        tradeOrderItemDO.setPointPrice( item.getPointPrice() );
        tradeOrderItemDO.setUsePoint( item.getUsePoint() );
        tradeOrderItemDO.setGivePoint( item.getGivePoint() );
        tradeOrderItemDO.setVipPrice( item.getVipPrice() );

        return tradeOrderItemDO;
    }

    @Override
    public MemberUserRespVO convertUser(MemberUserRespDTO memberUserRespDTO) {
        if ( memberUserRespDTO == null ) {
            return null;
        }

        MemberUserRespVO memberUserRespVO = new MemberUserRespVO();

        memberUserRespVO.setId( memberUserRespDTO.getId() );
        memberUserRespVO.setNickname( memberUserRespDTO.getNickname() );
        memberUserRespVO.setAvatar( memberUserRespDTO.getAvatar() );

        return memberUserRespVO;
    }

    @Override
    public TradeOrderPageItemRespVO convert(TradeOrderDO order, List<TradeOrderItemDO> items) {
        if ( order == null && items == null ) {
            return null;
        }

        TradeOrderPageItemRespVO tradeOrderPageItemRespVO = new TradeOrderPageItemRespVO();

        if ( order != null ) {
            tradeOrderPageItemRespVO.setId( order.getId() );
            tradeOrderPageItemRespVO.setNo( order.getNo() );
            tradeOrderPageItemRespVO.setCreateTime( order.getCreateTime() );
            tradeOrderPageItemRespVO.setType( order.getType() );
            tradeOrderPageItemRespVO.setTerminal( order.getTerminal() );
            tradeOrderPageItemRespVO.setUserId( order.getUserId() );
            tradeOrderPageItemRespVO.setUserIp( order.getUserIp() );
            tradeOrderPageItemRespVO.setUserRemark( order.getUserRemark() );
            tradeOrderPageItemRespVO.setStatus( order.getStatus() );
            tradeOrderPageItemRespVO.setProductCount( order.getProductCount() );
            tradeOrderPageItemRespVO.setFinishTime( order.getFinishTime() );
            tradeOrderPageItemRespVO.setCancelTime( order.getCancelTime() );
            tradeOrderPageItemRespVO.setCancelType( order.getCancelType() );
            tradeOrderPageItemRespVO.setRemark( order.getRemark() );
            tradeOrderPageItemRespVO.setPayOrderId( order.getPayOrderId() );
            tradeOrderPageItemRespVO.setPayStatus( order.getPayStatus() );
            tradeOrderPageItemRespVO.setPayTime( order.getPayTime() );
            tradeOrderPageItemRespVO.setPayChannelCode( order.getPayChannelCode() );
            tradeOrderPageItemRespVO.setTotalPrice( order.getTotalPrice() );
            tradeOrderPageItemRespVO.setDiscountPrice( order.getDiscountPrice() );
            tradeOrderPageItemRespVO.setDeliveryPrice( order.getDeliveryPrice() );
            tradeOrderPageItemRespVO.setAdjustPrice( order.getAdjustPrice() );
            tradeOrderPageItemRespVO.setPayPrice( order.getPayPrice() );
            tradeOrderPageItemRespVO.setDeliveryType( order.getDeliveryType() );
            tradeOrderPageItemRespVO.setPickUpStoreId( order.getPickUpStoreId() );
            if ( order.getPickUpVerifyCode() != null ) {
                tradeOrderPageItemRespVO.setPickUpVerifyCode( Long.parseLong( order.getPickUpVerifyCode() ) );
            }
            tradeOrderPageItemRespVO.setLogisticsId( order.getLogisticsId() );
            tradeOrderPageItemRespVO.setLogisticsNo( order.getLogisticsNo() );
            tradeOrderPageItemRespVO.setDeliveryTime( order.getDeliveryTime() );
            tradeOrderPageItemRespVO.setReceiveTime( order.getReceiveTime() );
            tradeOrderPageItemRespVO.setReceiverName( order.getReceiverName() );
            tradeOrderPageItemRespVO.setReceiverMobile( order.getReceiverMobile() );
            tradeOrderPageItemRespVO.setReceiverAreaId( order.getReceiverAreaId() );
            tradeOrderPageItemRespVO.setReceiverDetailAddress( order.getReceiverDetailAddress() );
            tradeOrderPageItemRespVO.setRefundPrice( order.getRefundPrice() );
            tradeOrderPageItemRespVO.setCouponId( order.getCouponId() );
            tradeOrderPageItemRespVO.setCouponPrice( order.getCouponPrice() );
            tradeOrderPageItemRespVO.setPointPrice( order.getPointPrice() );
            tradeOrderPageItemRespVO.setVipPrice( order.getVipPrice() );
            tradeOrderPageItemRespVO.setBrokerageUserId( order.getBrokerageUserId() );
        }
        tradeOrderPageItemRespVO.setItems( tradeOrderItemDOListToItemList( items ) );

        return tradeOrderPageItemRespVO;
    }

    @Override
    public ProductPropertyValueDetailRespVO convert(ProductPropertyValueDetailRespDTO bean) {
        if ( bean == null ) {
            return null;
        }

        ProductPropertyValueDetailRespVO productPropertyValueDetailRespVO = new ProductPropertyValueDetailRespVO();

        productPropertyValueDetailRespVO.setPropertyId( bean.getPropertyId() );
        productPropertyValueDetailRespVO.setPropertyName( bean.getPropertyName() );
        productPropertyValueDetailRespVO.setValueId( bean.getValueId() );
        productPropertyValueDetailRespVO.setValueName( bean.getValueName() );

        return productPropertyValueDetailRespVO;
    }

    @Override
    public List<TradeOrderDetailRespVO.OrderLog> convertList03(List<TradeOrderLogDO> orderLogs) {
        if ( orderLogs == null ) {
            return null;
        }

        List<TradeOrderDetailRespVO.OrderLog> list = new ArrayList<TradeOrderDetailRespVO.OrderLog>( orderLogs.size() );
        for ( TradeOrderLogDO tradeOrderLogDO : orderLogs ) {
            list.add( tradeOrderLogDOToOrderLog( tradeOrderLogDO ) );
        }

        return list;
    }

    @Override
    public TradeOrderDetailRespVO convert2(TradeOrderDO order, List<TradeOrderItemDO> items) {
        if ( order == null && items == null ) {
            return null;
        }

        TradeOrderDetailRespVO tradeOrderDetailRespVO = new TradeOrderDetailRespVO();

        if ( order != null ) {
            tradeOrderDetailRespVO.setId( order.getId() );
            tradeOrderDetailRespVO.setNo( order.getNo() );
            tradeOrderDetailRespVO.setCreateTime( order.getCreateTime() );
            tradeOrderDetailRespVO.setType( order.getType() );
            tradeOrderDetailRespVO.setTerminal( order.getTerminal() );
            tradeOrderDetailRespVO.setUserId( order.getUserId() );
            tradeOrderDetailRespVO.setUserIp( order.getUserIp() );
            tradeOrderDetailRespVO.setUserRemark( order.getUserRemark() );
            tradeOrderDetailRespVO.setStatus( order.getStatus() );
            tradeOrderDetailRespVO.setProductCount( order.getProductCount() );
            tradeOrderDetailRespVO.setFinishTime( order.getFinishTime() );
            tradeOrderDetailRespVO.setCancelTime( order.getCancelTime() );
            tradeOrderDetailRespVO.setCancelType( order.getCancelType() );
            tradeOrderDetailRespVO.setRemark( order.getRemark() );
            tradeOrderDetailRespVO.setPayOrderId( order.getPayOrderId() );
            tradeOrderDetailRespVO.setPayStatus( order.getPayStatus() );
            tradeOrderDetailRespVO.setPayTime( order.getPayTime() );
            tradeOrderDetailRespVO.setPayChannelCode( order.getPayChannelCode() );
            tradeOrderDetailRespVO.setTotalPrice( order.getTotalPrice() );
            tradeOrderDetailRespVO.setDiscountPrice( order.getDiscountPrice() );
            tradeOrderDetailRespVO.setDeliveryPrice( order.getDeliveryPrice() );
            tradeOrderDetailRespVO.setAdjustPrice( order.getAdjustPrice() );
            tradeOrderDetailRespVO.setPayPrice( order.getPayPrice() );
            tradeOrderDetailRespVO.setDeliveryType( order.getDeliveryType() );
            tradeOrderDetailRespVO.setPickUpStoreId( order.getPickUpStoreId() );
            if ( order.getPickUpVerifyCode() != null ) {
                tradeOrderDetailRespVO.setPickUpVerifyCode( Long.parseLong( order.getPickUpVerifyCode() ) );
            }
            tradeOrderDetailRespVO.setLogisticsId( order.getLogisticsId() );
            tradeOrderDetailRespVO.setLogisticsNo( order.getLogisticsNo() );
            tradeOrderDetailRespVO.setDeliveryTime( order.getDeliveryTime() );
            tradeOrderDetailRespVO.setReceiveTime( order.getReceiveTime() );
            tradeOrderDetailRespVO.setReceiverName( order.getReceiverName() );
            tradeOrderDetailRespVO.setReceiverMobile( order.getReceiverMobile() );
            tradeOrderDetailRespVO.setReceiverAreaId( order.getReceiverAreaId() );
            tradeOrderDetailRespVO.setReceiverDetailAddress( order.getReceiverDetailAddress() );
            tradeOrderDetailRespVO.setRefundPrice( order.getRefundPrice() );
            tradeOrderDetailRespVO.setCouponId( order.getCouponId() );
            tradeOrderDetailRespVO.setCouponPrice( order.getCouponPrice() );
            tradeOrderDetailRespVO.setPointPrice( order.getPointPrice() );
            tradeOrderDetailRespVO.setVipPrice( order.getVipPrice() );
            tradeOrderDetailRespVO.setBrokerageUserId( order.getBrokerageUserId() );
            tradeOrderDetailRespVO.setAppointStoreId( order.getAppointStoreId() );
        }
        tradeOrderDetailRespVO.setItems( tradeOrderItemDOListToItemList1( items ) );

        return tradeOrderDetailRespVO;
    }

    @Override
    public MemberUserRespVO convert(MemberUserRespDTO bean) {
        if ( bean == null ) {
            return null;
        }

        MemberUserRespVO memberUserRespVO = new MemberUserRespVO();

        memberUserRespVO.setId( bean.getId() );
        memberUserRespVO.setNickname( bean.getNickname() );
        memberUserRespVO.setAvatar( bean.getAvatar() );

        return memberUserRespVO;
    }

    @Override
    public AppTradeOrderPageItemRespVO convert02(TradeOrderDO order, List<TradeOrderItemDO> items) {
        if ( order == null && items == null ) {
            return null;
        }

        AppTradeOrderPageItemRespVO appTradeOrderPageItemRespVO = new AppTradeOrderPageItemRespVO();

        if ( order != null ) {
            appTradeOrderPageItemRespVO.setId( order.getId() );
            appTradeOrderPageItemRespVO.setNo( order.getNo() );
            appTradeOrderPageItemRespVO.setType( order.getType() );
            appTradeOrderPageItemRespVO.setStatus( order.getStatus() );
            appTradeOrderPageItemRespVO.setProductCount( order.getProductCount() );
            appTradeOrderPageItemRespVO.setCommentStatus( order.getCommentStatus() );
            appTradeOrderPageItemRespVO.setCreateTime( order.getCreateTime() );
            appTradeOrderPageItemRespVO.setPayOrderId( order.getPayOrderId() );
            appTradeOrderPageItemRespVO.setPayPrice( order.getPayPrice() );
            appTradeOrderPageItemRespVO.setDeliveryType( order.getDeliveryType() );
            appTradeOrderPageItemRespVO.setCombinationRecordId( order.getCombinationRecordId() );
        }
        appTradeOrderPageItemRespVO.setItems( tradeOrderItemDOListToAppTradeOrderItemRespVOList( items ) );

        return appTradeOrderPageItemRespVO;
    }

    @Override
    public AppProductPropertyValueDetailRespVO convert02(ProductPropertyValueDetailRespDTO bean) {
        if ( bean == null ) {
            return null;
        }

        AppProductPropertyValueDetailRespVO appProductPropertyValueDetailRespVO = new AppProductPropertyValueDetailRespVO();

        appProductPropertyValueDetailRespVO.setPropertyId( bean.getPropertyId() );
        appProductPropertyValueDetailRespVO.setPropertyName( bean.getPropertyName() );
        appProductPropertyValueDetailRespVO.setValueId( bean.getValueId() );
        appProductPropertyValueDetailRespVO.setValueName( bean.getValueName() );

        return appProductPropertyValueDetailRespVO;
    }

    @Override
    public AppTradeOrderDetailRespVO convert3(TradeOrderDO order, List<TradeOrderItemDO> items) {
        if ( order == null && items == null ) {
            return null;
        }

        AppTradeOrderDetailRespVO appTradeOrderDetailRespVO = new AppTradeOrderDetailRespVO();

        if ( order != null ) {
            appTradeOrderDetailRespVO.setId( order.getId() );
            appTradeOrderDetailRespVO.setNo( order.getNo() );
            appTradeOrderDetailRespVO.setType( order.getType() );
            appTradeOrderDetailRespVO.setCreateTime( order.getCreateTime() );
            appTradeOrderDetailRespVO.setUserRemark( order.getUserRemark() );
            appTradeOrderDetailRespVO.setStatus( order.getStatus() );
            appTradeOrderDetailRespVO.setProductCount( order.getProductCount() );
            appTradeOrderDetailRespVO.setFinishTime( order.getFinishTime() );
            appTradeOrderDetailRespVO.setCancelTime( order.getCancelTime() );
            appTradeOrderDetailRespVO.setCommentStatus( order.getCommentStatus() );
            appTradeOrderDetailRespVO.setPayStatus( order.getPayStatus() );
            appTradeOrderDetailRespVO.setPayOrderId( order.getPayOrderId() );
            appTradeOrderDetailRespVO.setPayTime( order.getPayTime() );
            appTradeOrderDetailRespVO.setPayChannelCode( order.getPayChannelCode() );
            appTradeOrderDetailRespVO.setTotalPrice( order.getTotalPrice() );
            appTradeOrderDetailRespVO.setDiscountPrice( order.getDiscountPrice() );
            appTradeOrderDetailRespVO.setDeliveryPrice( order.getDeliveryPrice() );
            appTradeOrderDetailRespVO.setAdjustPrice( order.getAdjustPrice() );
            appTradeOrderDetailRespVO.setPayPrice( order.getPayPrice() );
            appTradeOrderDetailRespVO.setDeliveryType( order.getDeliveryType() );
            appTradeOrderDetailRespVO.setLogisticsId( order.getLogisticsId() );
            appTradeOrderDetailRespVO.setLogisticsNo( order.getLogisticsNo() );
            appTradeOrderDetailRespVO.setDeliveryTime( order.getDeliveryTime() );
            appTradeOrderDetailRespVO.setReceiveTime( order.getReceiveTime() );
            appTradeOrderDetailRespVO.setReceiverName( order.getReceiverName() );
            appTradeOrderDetailRespVO.setReceiverMobile( order.getReceiverMobile() );
            appTradeOrderDetailRespVO.setReceiverAreaId( order.getReceiverAreaId() );
            appTradeOrderDetailRespVO.setReceiverDetailAddress( order.getReceiverDetailAddress() );
            appTradeOrderDetailRespVO.setPickUpStoreId( order.getPickUpStoreId() );
            appTradeOrderDetailRespVO.setPickUpVerifyCode( order.getPickUpVerifyCode() );
            appTradeOrderDetailRespVO.setRefundStatus( order.getRefundStatus() );
            appTradeOrderDetailRespVO.setRefundPrice( order.getRefundPrice() );
            appTradeOrderDetailRespVO.setCouponId( order.getCouponId() );
            appTradeOrderDetailRespVO.setCouponPrice( order.getCouponPrice() );
            appTradeOrderDetailRespVO.setPointPrice( order.getPointPrice() );
            appTradeOrderDetailRespVO.setVipPrice( order.getVipPrice() );
            appTradeOrderDetailRespVO.setCombinationRecordId( order.getCombinationRecordId() );
        }
        appTradeOrderDetailRespVO.setItems( tradeOrderItemDOListToAppTradeOrderItemRespVOList( items ) );

        return appTradeOrderDetailRespVO;
    }

    @Override
    public AppTradeOrderItemRespVO convert03(TradeOrderItemDO bean) {
        if ( bean == null ) {
            return null;
        }

        AppTradeOrderItemRespVO appTradeOrderItemRespVO = new AppTradeOrderItemRespVO();

        appTradeOrderItemRespVO.setId( bean.getId() );
        appTradeOrderItemRespVO.setOrderId( bean.getOrderId() );
        appTradeOrderItemRespVO.setSpuId( bean.getSpuId() );
        appTradeOrderItemRespVO.setSpuName( bean.getSpuName() );
        appTradeOrderItemRespVO.setSkuId( bean.getSkuId() );
        appTradeOrderItemRespVO.setProperties( propertyListToAppProductPropertyValueDetailRespVOList( bean.getProperties() ) );
        appTradeOrderItemRespVO.setPicUrl( bean.getPicUrl() );
        appTradeOrderItemRespVO.setCount( bean.getCount() );
        appTradeOrderItemRespVO.setCommentStatus( bean.getCommentStatus() );
        appTradeOrderItemRespVO.setPrice( bean.getPrice() );
        appTradeOrderItemRespVO.setPayPrice( bean.getPayPrice() );
        appTradeOrderItemRespVO.setAfterSaleId( bean.getAfterSaleId() );
        appTradeOrderItemRespVO.setAfterSaleStatus( bean.getAfterSaleStatus() );

        return appTradeOrderItemRespVO;
    }

    @Override
    public ProductCommentCreateReqDTO convert04(AppTradeOrderItemCommentCreateReqVO createReqVO, TradeOrderItemDO tradeOrderItemDO) {
        if ( createReqVO == null && tradeOrderItemDO == null ) {
            return null;
        }

        ProductCommentCreateReqDTO productCommentCreateReqDTO = new ProductCommentCreateReqDTO();

        if ( createReqVO != null ) {
            productCommentCreateReqDTO.setDescriptionScores( createReqVO.getDescriptionScores() );
            productCommentCreateReqDTO.setBenefitScores( createReqVO.getBenefitScores() );
            productCommentCreateReqDTO.setContent( createReqVO.getContent() );
            List<String> list = createReqVO.getPicUrls();
            if ( list != null ) {
                productCommentCreateReqDTO.setPicUrls( new ArrayList<String>( list ) );
            }
            productCommentCreateReqDTO.setAnonymous( createReqVO.getAnonymous() );
        }
        if ( tradeOrderItemDO != null ) {
            productCommentCreateReqDTO.setSkuId( tradeOrderItemDO.getSkuId() );
            productCommentCreateReqDTO.setOrderId( tradeOrderItemDO.getOrderId() );
            productCommentCreateReqDTO.setOrderItemId( tradeOrderItemDO.getId() );
            productCommentCreateReqDTO.setUserId( tradeOrderItemDO.getUserId() );
        }

        return productCommentCreateReqDTO;
    }

    @Override
    public TradePriceCalculateReqBO convert(AppTradeOrderSettlementReqVO settlementReqVO) {
        if ( settlementReqVO == null ) {
            return null;
        }

        TradePriceCalculateReqBO tradePriceCalculateReqBO = new TradePriceCalculateReqBO();

        tradePriceCalculateReqBO.setCouponId( settlementReqVO.getCouponId() );
        tradePriceCalculateReqBO.setPointStatus( settlementReqVO.getPointStatus() );
        tradePriceCalculateReqBO.setDeliveryType( settlementReqVO.getDeliveryType() );
        tradePriceCalculateReqBO.setAddressId( settlementReqVO.getAddressId() );
        tradePriceCalculateReqBO.setPickUpStoreId( settlementReqVO.getPickUpStoreId() );
        tradePriceCalculateReqBO.setItems( itemListToItemList( settlementReqVO.getItems() ) );
        tradePriceCalculateReqBO.setSeckillActivityId( settlementReqVO.getSeckillActivityId() );
        tradePriceCalculateReqBO.setCombinationActivityId( settlementReqVO.getCombinationActivityId() );
        tradePriceCalculateReqBO.setCombinationHeadId( settlementReqVO.getCombinationHeadId() );
        tradePriceCalculateReqBO.setBargainRecordId( settlementReqVO.getBargainRecordId() );
        tradePriceCalculateReqBO.setPointActivityId( settlementReqVO.getPointActivityId() );

        return tradePriceCalculateReqBO;
    }

    @Override
    public AppTradeOrderSettlementRespVO convert0(TradePriceCalculateRespBO calculate, MemberAddressRespDTO address) {
        if ( calculate == null && address == null ) {
            return null;
        }

        AppTradeOrderSettlementRespVO appTradeOrderSettlementRespVO = new AppTradeOrderSettlementRespVO();

        if ( calculate != null ) {
            appTradeOrderSettlementRespVO.setType( calculate.getType() );
            appTradeOrderSettlementRespVO.setItems( orderItemListToItemList( calculate.getItems() ) );
            appTradeOrderSettlementRespVO.setCoupons( couponListToCouponList( calculate.getCoupons() ) );
            appTradeOrderSettlementRespVO.setPrice( priceToPrice( calculate.getPrice() ) );
            appTradeOrderSettlementRespVO.setUsePoint( calculate.getUsePoint() );
            appTradeOrderSettlementRespVO.setTotalPoint( calculate.getTotalPoint() );
            List<TradePriceCalculateRespBO.Promotion> list2 = calculate.getPromotions();
            if ( list2 != null ) {
                appTradeOrderSettlementRespVO.setPromotions( new ArrayList<TradePriceCalculateRespBO.Promotion>( list2 ) );
            }
        }
        appTradeOrderSettlementRespVO.setAddress( memberAddressRespDTOToAddress( address ) );

        return appTradeOrderSettlementRespVO;
    }

    @Override
    public List<AppOrderExpressTrackRespDTO> convertList02(List<ExpressTrackRespDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<AppOrderExpressTrackRespDTO> list1 = new ArrayList<AppOrderExpressTrackRespDTO>( list.size() );
        for ( ExpressTrackRespDTO expressTrackRespDTO : list ) {
            list1.add( expressTrackRespDTOToAppOrderExpressTrackRespDTO( expressTrackRespDTO ) );
        }

        return list1;
    }

    @Override
    public TradeOrderDO convert(TradeOrderUpdateAddressReqVO reqVO) {
        if ( reqVO == null ) {
            return null;
        }

        TradeOrderDO.TradeOrderDOBuilder tradeOrderDO = TradeOrderDO.builder();

        tradeOrderDO.id( reqVO.getId() );
        tradeOrderDO.receiverName( reqVO.getReceiverName() );
        tradeOrderDO.receiverMobile( reqVO.getReceiverMobile() );
        tradeOrderDO.receiverAreaId( reqVO.getReceiverAreaId() );
        tradeOrderDO.receiverDetailAddress( reqVO.getReceiverDetailAddress() );

        return tradeOrderDO.build();
    }

    @Override
    public TradeOrderDO convert(TradeOrderUpdatePriceReqVO reqVO) {
        if ( reqVO == null ) {
            return null;
        }

        TradeOrderDO.TradeOrderDOBuilder tradeOrderDO = TradeOrderDO.builder();

        tradeOrderDO.id( reqVO.getId() );
        tradeOrderDO.adjustPrice( reqVO.getAdjustPrice() );

        return tradeOrderDO.build();
    }

    @Override
    public TradeOrderDO convert(TradeOrderRemarkReqVO reqVO) {
        if ( reqVO == null ) {
            return null;
        }

        TradeOrderDO.TradeOrderDOBuilder tradeOrderDO = TradeOrderDO.builder();

        tradeOrderDO.id( reqVO.getId() );
        tradeOrderDO.remark( reqVO.getRemark() );

        return tradeOrderDO.build();
    }

    @Override
    public TradeOrderDO convert(TradeOrderAppointStoreIdReqVO reqVO) {
        if ( reqVO == null ) {
            return null;
        }

        TradeOrderDO.TradeOrderDOBuilder tradeOrderDO = TradeOrderDO.builder();

        tradeOrderDO.id( reqVO.getId() );
        tradeOrderDO.appointStoreId( reqVO.getAppointStoreId() );

        return tradeOrderDO.build();
    }

    @Override
    public List<TradeOrderRespDTO> convertList04(List<TradeOrderDO> list) {
        if ( list == null ) {
            return null;
        }

        List<TradeOrderRespDTO> list1 = new ArrayList<TradeOrderRespDTO>( list.size() );
        for ( TradeOrderDO tradeOrderDO : list ) {
            list1.add( convert( tradeOrderDO ) );
        }

        return list1;
    }

    @Override
    public CombinationRecordCreateReqDTO convert(TradeOrderDO order, TradeOrderItemDO item) {
        if ( order == null && item == null ) {
            return null;
        }

        CombinationRecordCreateReqDTO combinationRecordCreateReqDTO = new CombinationRecordCreateReqDTO();

        if ( order != null ) {
            combinationRecordCreateReqDTO.setActivityId( order.getCombinationActivityId() );
            combinationRecordCreateReqDTO.setOrderId( order.getId() );
            combinationRecordCreateReqDTO.setUserId( order.getUserId() );
            combinationRecordCreateReqDTO.setHeadId( order.getCombinationHeadId() );
        }
        if ( item != null ) {
            combinationRecordCreateReqDTO.setSpuId( item.getSpuId() );
            combinationRecordCreateReqDTO.setSkuId( item.getSkuId() );
            combinationRecordCreateReqDTO.setCount( item.getCount() );
            combinationRecordCreateReqDTO.setCombinationPrice( item.getPayPrice() );
        }

        return combinationRecordCreateReqDTO;
    }

    @Override
    public AppTradeOrderDetailRespVO convert02(TradeOrderDO order, List<TradeOrderItemDO> orderItems, TradeOrderProperties properties, DeliveryExpressDO express, Map<Long, ProductPrintDocumentDO> orderItemIdToDocumentMap) {
        if ( order == null && orderItems == null && properties == null && express == null && orderItemIdToDocumentMap == null ) {
            return null;
        }

        AppTradeOrderDetailRespVO appTradeOrderDetailRespVO = new AppTradeOrderDetailRespVO();

        if ( order != null ) {
            appTradeOrderDetailRespVO.setId( order.getId() );
            appTradeOrderDetailRespVO.setCreateTime( order.getCreateTime() );
            appTradeOrderDetailRespVO.setStatus( order.getStatus() );
            appTradeOrderDetailRespVO.setNo( order.getNo() );
            appTradeOrderDetailRespVO.setType( order.getType() );
            appTradeOrderDetailRespVO.setUserRemark( order.getUserRemark() );
            appTradeOrderDetailRespVO.setProductCount( order.getProductCount() );
            appTradeOrderDetailRespVO.setFinishTime( order.getFinishTime() );
            appTradeOrderDetailRespVO.setCancelTime( order.getCancelTime() );
            appTradeOrderDetailRespVO.setCommentStatus( order.getCommentStatus() );
            appTradeOrderDetailRespVO.setPayStatus( order.getPayStatus() );
            appTradeOrderDetailRespVO.setPayOrderId( order.getPayOrderId() );
            appTradeOrderDetailRespVO.setPayTime( order.getPayTime() );
            appTradeOrderDetailRespVO.setPayChannelCode( order.getPayChannelCode() );
            appTradeOrderDetailRespVO.setTotalPrice( order.getTotalPrice() );
            appTradeOrderDetailRespVO.setDiscountPrice( order.getDiscountPrice() );
            appTradeOrderDetailRespVO.setDeliveryPrice( order.getDeliveryPrice() );
            appTradeOrderDetailRespVO.setAdjustPrice( order.getAdjustPrice() );
            appTradeOrderDetailRespVO.setPayPrice( order.getPayPrice() );
            appTradeOrderDetailRespVO.setDeliveryType( order.getDeliveryType() );
            appTradeOrderDetailRespVO.setLogisticsId( order.getLogisticsId() );
            appTradeOrderDetailRespVO.setLogisticsNo( order.getLogisticsNo() );
            appTradeOrderDetailRespVO.setDeliveryTime( order.getDeliveryTime() );
            appTradeOrderDetailRespVO.setReceiveTime( order.getReceiveTime() );
            appTradeOrderDetailRespVO.setReceiverName( order.getReceiverName() );
            appTradeOrderDetailRespVO.setReceiverMobile( order.getReceiverMobile() );
            appTradeOrderDetailRespVO.setReceiverAreaId( order.getReceiverAreaId() );
            appTradeOrderDetailRespVO.setReceiverDetailAddress( order.getReceiverDetailAddress() );
            appTradeOrderDetailRespVO.setPickUpStoreId( order.getPickUpStoreId() );
            appTradeOrderDetailRespVO.setPickUpVerifyCode( order.getPickUpVerifyCode() );
            appTradeOrderDetailRespVO.setRefundStatus( order.getRefundStatus() );
            appTradeOrderDetailRespVO.setRefundPrice( order.getRefundPrice() );
            appTradeOrderDetailRespVO.setCouponId( order.getCouponId() );
            appTradeOrderDetailRespVO.setCouponPrice( order.getCouponPrice() );
            appTradeOrderDetailRespVO.setPointPrice( order.getPointPrice() );
            appTradeOrderDetailRespVO.setVipPrice( order.getVipPrice() );
            appTradeOrderDetailRespVO.setCombinationRecordId( order.getCombinationRecordId() );
        }
        appTradeOrderDetailRespVO.setItems( convertOrderItems(orderItems, orderItemIdToDocumentMap) );

        return appTradeOrderDetailRespVO;
    }

    private Integer calculateRespBOPriceTotalPrice(TradePriceCalculateRespBO tradePriceCalculateRespBO) {
        TradePriceCalculateRespBO.Price price = tradePriceCalculateRespBO.getPrice();
        if ( price == null ) {
            return null;
        }
        return price.getTotalPrice();
    }

    private Integer calculateRespBOPriceDiscountPrice(TradePriceCalculateRespBO tradePriceCalculateRespBO) {
        TradePriceCalculateRespBO.Price price = tradePriceCalculateRespBO.getPrice();
        if ( price == null ) {
            return null;
        }
        return price.getDiscountPrice();
    }

    private Integer calculateRespBOPriceDeliveryPrice(TradePriceCalculateRespBO tradePriceCalculateRespBO) {
        TradePriceCalculateRespBO.Price price = tradePriceCalculateRespBO.getPrice();
        if ( price == null ) {
            return null;
        }
        return price.getDeliveryPrice();
    }

    private Integer calculateRespBOPriceCouponPrice(TradePriceCalculateRespBO tradePriceCalculateRespBO) {
        TradePriceCalculateRespBO.Price price = tradePriceCalculateRespBO.getPrice();
        if ( price == null ) {
            return null;
        }
        return price.getCouponPrice();
    }

    private Integer calculateRespBOPricePointPrice(TradePriceCalculateRespBO tradePriceCalculateRespBO) {
        TradePriceCalculateRespBO.Price price = tradePriceCalculateRespBO.getPrice();
        if ( price == null ) {
            return null;
        }
        return price.getPointPrice();
    }

    private Integer calculateRespBOPriceVipPrice(TradePriceCalculateRespBO tradePriceCalculateRespBO) {
        TradePriceCalculateRespBO.Price price = tradePriceCalculateRespBO.getPrice();
        if ( price == null ) {
            return null;
        }
        return price.getVipPrice();
    }

    private Integer calculateRespBOPricePayPrice(TradePriceCalculateRespBO tradePriceCalculateRespBO) {
        TradePriceCalculateRespBO.Price price = tradePriceCalculateRespBO.getPrice();
        if ( price == null ) {
            return null;
        }
        return price.getPayPrice();
    }

    protected TradeOrderItemDO.Property productPropertyValueDetailRespDTOToProperty(ProductPropertyValueDetailRespDTO productPropertyValueDetailRespDTO) {
        if ( productPropertyValueDetailRespDTO == null ) {
            return null;
        }

        TradeOrderItemDO.Property property = new TradeOrderItemDO.Property();

        property.setPropertyId( productPropertyValueDetailRespDTO.getPropertyId() );
        property.setPropertyName( productPropertyValueDetailRespDTO.getPropertyName() );
        property.setValueId( productPropertyValueDetailRespDTO.getValueId() );
        property.setValueName( productPropertyValueDetailRespDTO.getValueName() );

        return property;
    }

    protected List<TradeOrderItemDO.Property> productPropertyValueDetailRespDTOListToPropertyList(List<ProductPropertyValueDetailRespDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<TradeOrderItemDO.Property> list1 = new ArrayList<TradeOrderItemDO.Property>( list.size() );
        for ( ProductPropertyValueDetailRespDTO productPropertyValueDetailRespDTO : list ) {
            list1.add( productPropertyValueDetailRespDTOToProperty( productPropertyValueDetailRespDTO ) );
        }

        return list1;
    }

    protected ProductPropertyValueDetailRespVO propertyToProductPropertyValueDetailRespVO(TradeOrderItemDO.Property property) {
        if ( property == null ) {
            return null;
        }

        ProductPropertyValueDetailRespVO productPropertyValueDetailRespVO = new ProductPropertyValueDetailRespVO();

        productPropertyValueDetailRespVO.setPropertyId( property.getPropertyId() );
        productPropertyValueDetailRespVO.setPropertyName( property.getPropertyName() );
        productPropertyValueDetailRespVO.setValueId( property.getValueId() );
        productPropertyValueDetailRespVO.setValueName( property.getValueName() );

        return productPropertyValueDetailRespVO;
    }

    protected List<ProductPropertyValueDetailRespVO> propertyListToProductPropertyValueDetailRespVOList(List<TradeOrderItemDO.Property> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductPropertyValueDetailRespVO> list1 = new ArrayList<ProductPropertyValueDetailRespVO>( list.size() );
        for ( TradeOrderItemDO.Property property : list ) {
            list1.add( propertyToProductPropertyValueDetailRespVO( property ) );
        }

        return list1;
    }

    protected TradeOrderPageItemRespVO.Item tradeOrderItemDOToItem(TradeOrderItemDO tradeOrderItemDO) {
        if ( tradeOrderItemDO == null ) {
            return null;
        }

        TradeOrderPageItemRespVO.Item item = new TradeOrderPageItemRespVO.Item();

        item.setId( tradeOrderItemDO.getId() );
        item.setUserId( tradeOrderItemDO.getUserId() );
        item.setOrderId( tradeOrderItemDO.getOrderId() );
        item.setSpuId( tradeOrderItemDO.getSpuId() );
        item.setSpuName( tradeOrderItemDO.getSpuName() );
        item.setSkuId( tradeOrderItemDO.getSkuId() );
        item.setPicUrl( tradeOrderItemDO.getPicUrl() );
        item.setCount( tradeOrderItemDO.getCount() );
        item.setPrice( tradeOrderItemDO.getPrice() );
        item.setDiscountPrice( tradeOrderItemDO.getDiscountPrice() );
        item.setPayPrice( tradeOrderItemDO.getPayPrice() );
        item.setAfterSaleStatus( tradeOrderItemDO.getAfterSaleStatus() );
        item.setProperties( propertyListToProductPropertyValueDetailRespVOList( tradeOrderItemDO.getProperties() ) );

        return item;
    }

    protected List<TradeOrderPageItemRespVO.Item> tradeOrderItemDOListToItemList(List<TradeOrderItemDO> list) {
        if ( list == null ) {
            return null;
        }

        List<TradeOrderPageItemRespVO.Item> list1 = new ArrayList<TradeOrderPageItemRespVO.Item>( list.size() );
        for ( TradeOrderItemDO tradeOrderItemDO : list ) {
            list1.add( tradeOrderItemDOToItem( tradeOrderItemDO ) );
        }

        return list1;
    }

    protected TradeOrderDetailRespVO.OrderLog tradeOrderLogDOToOrderLog(TradeOrderLogDO tradeOrderLogDO) {
        if ( tradeOrderLogDO == null ) {
            return null;
        }

        TradeOrderDetailRespVO.OrderLog orderLog = new TradeOrderDetailRespVO.OrderLog();

        orderLog.setContent( tradeOrderLogDO.getContent() );
        orderLog.setCreateTime( tradeOrderLogDO.getCreateTime() );
        orderLog.setUserType( tradeOrderLogDO.getUserType() );

        return orderLog;
    }

    protected TradeOrderDetailRespVO.Item tradeOrderItemDOToItem1(TradeOrderItemDO tradeOrderItemDO) {
        if ( tradeOrderItemDO == null ) {
            return null;
        }

        TradeOrderDetailRespVO.Item item = new TradeOrderDetailRespVO.Item();

        item.setId( tradeOrderItemDO.getId() );
        item.setUserId( tradeOrderItemDO.getUserId() );
        item.setOrderId( tradeOrderItemDO.getOrderId() );
        item.setSpuId( tradeOrderItemDO.getSpuId() );
        item.setSpuName( tradeOrderItemDO.getSpuName() );
        item.setSkuId( tradeOrderItemDO.getSkuId() );
        item.setPicUrl( tradeOrderItemDO.getPicUrl() );
        item.setCount( tradeOrderItemDO.getCount() );
        item.setPrice( tradeOrderItemDO.getPrice() );
        item.setDiscountPrice( tradeOrderItemDO.getDiscountPrice() );
        item.setPayPrice( tradeOrderItemDO.getPayPrice() );
        item.setAfterSaleStatus( tradeOrderItemDO.getAfterSaleStatus() );
        item.setProperties( propertyListToProductPropertyValueDetailRespVOList( tradeOrderItemDO.getProperties() ) );

        return item;
    }

    protected List<TradeOrderDetailRespVO.Item> tradeOrderItemDOListToItemList1(List<TradeOrderItemDO> list) {
        if ( list == null ) {
            return null;
        }

        List<TradeOrderDetailRespVO.Item> list1 = new ArrayList<TradeOrderDetailRespVO.Item>( list.size() );
        for ( TradeOrderItemDO tradeOrderItemDO : list ) {
            list1.add( tradeOrderItemDOToItem1( tradeOrderItemDO ) );
        }

        return list1;
    }

    protected List<AppTradeOrderItemRespVO> tradeOrderItemDOListToAppTradeOrderItemRespVOList(List<TradeOrderItemDO> list) {
        if ( list == null ) {
            return null;
        }

        List<AppTradeOrderItemRespVO> list1 = new ArrayList<AppTradeOrderItemRespVO>( list.size() );
        for ( TradeOrderItemDO tradeOrderItemDO : list ) {
            list1.add( convert03( tradeOrderItemDO ) );
        }

        return list1;
    }

    protected AppProductPropertyValueDetailRespVO propertyToAppProductPropertyValueDetailRespVO(TradeOrderItemDO.Property property) {
        if ( property == null ) {
            return null;
        }

        AppProductPropertyValueDetailRespVO appProductPropertyValueDetailRespVO = new AppProductPropertyValueDetailRespVO();

        appProductPropertyValueDetailRespVO.setPropertyId( property.getPropertyId() );
        appProductPropertyValueDetailRespVO.setPropertyName( property.getPropertyName() );
        appProductPropertyValueDetailRespVO.setValueId( property.getValueId() );
        appProductPropertyValueDetailRespVO.setValueName( property.getValueName() );

        return appProductPropertyValueDetailRespVO;
    }

    protected List<AppProductPropertyValueDetailRespVO> propertyListToAppProductPropertyValueDetailRespVOList(List<TradeOrderItemDO.Property> list) {
        if ( list == null ) {
            return null;
        }

        List<AppProductPropertyValueDetailRespVO> list1 = new ArrayList<AppProductPropertyValueDetailRespVO>( list.size() );
        for ( TradeOrderItemDO.Property property : list ) {
            list1.add( propertyToAppProductPropertyValueDetailRespVO( property ) );
        }

        return list1;
    }

    protected TradePriceCalculateReqBO.Item itemToItem(AppTradeOrderSettlementReqVO.Item item) {
        if ( item == null ) {
            return null;
        }

        TradePriceCalculateReqBO.Item item1 = new TradePriceCalculateReqBO.Item();

        item1.setSkuId( item.getSkuId() );
        item1.setCount( item.getCount() );
        item1.setCartId( item.getCartId() );

        return item1;
    }

    protected List<TradePriceCalculateReqBO.Item> itemListToItemList(List<AppTradeOrderSettlementReqVO.Item> list) {
        if ( list == null ) {
            return null;
        }

        List<TradePriceCalculateReqBO.Item> list1 = new ArrayList<TradePriceCalculateReqBO.Item>( list.size() );
        for ( AppTradeOrderSettlementReqVO.Item item : list ) {
            list1.add( itemToItem( item ) );
        }

        return list1;
    }

    protected List<AppProductPropertyValueDetailRespVO> productPropertyValueDetailRespDTOListToAppProductPropertyValueDetailRespVOList(List<ProductPropertyValueDetailRespDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<AppProductPropertyValueDetailRespVO> list1 = new ArrayList<AppProductPropertyValueDetailRespVO>( list.size() );
        for ( ProductPropertyValueDetailRespDTO productPropertyValueDetailRespDTO : list ) {
            list1.add( convert02( productPropertyValueDetailRespDTO ) );
        }

        return list1;
    }

    protected AppTradeOrderSettlementRespVO.Item orderItemToItem(TradePriceCalculateRespBO.OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        AppTradeOrderSettlementRespVO.Item item = new AppTradeOrderSettlementRespVO.Item();

        item.setCategoryId( orderItem.getCategoryId() );
        item.setSpuId( orderItem.getSpuId() );
        item.setSpuName( orderItem.getSpuName() );
        if ( orderItem.getSkuId() != null ) {
            item.setSkuId( orderItem.getSkuId().intValue() );
        }
        item.setPrice( orderItem.getPrice() );
        item.setPicUrl( orderItem.getPicUrl() );
        item.setProperties( productPropertyValueDetailRespDTOListToAppProductPropertyValueDetailRespVOList( orderItem.getProperties() ) );
        item.setCartId( orderItem.getCartId() );
        item.setCount( orderItem.getCount() );

        return item;
    }

    protected List<AppTradeOrderSettlementRespVO.Item> orderItemListToItemList(List<TradePriceCalculateRespBO.OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<AppTradeOrderSettlementRespVO.Item> list1 = new ArrayList<AppTradeOrderSettlementRespVO.Item>( list.size() );
        for ( TradePriceCalculateRespBO.OrderItem orderItem : list ) {
            list1.add( orderItemToItem( orderItem ) );
        }

        return list1;
    }

    protected AppTradeOrderSettlementRespVO.Coupon couponToCoupon(TradePriceCalculateRespBO.Coupon coupon) {
        if ( coupon == null ) {
            return null;
        }

        AppTradeOrderSettlementRespVO.Coupon coupon1 = new AppTradeOrderSettlementRespVO.Coupon();

        coupon1.setId( coupon.getId() );
        coupon1.setName( coupon.getName() );
        coupon1.setUsePrice( coupon.getUsePrice() );
        coupon1.setValidStartTime( coupon.getValidStartTime() );
        coupon1.setValidEndTime( coupon.getValidEndTime() );
        coupon1.setDiscountType( coupon.getDiscountType() );
        coupon1.setDiscountPercent( coupon.getDiscountPercent() );
        coupon1.setDiscountPrice( coupon.getDiscountPrice() );
        coupon1.setDiscountLimitPrice( coupon.getDiscountLimitPrice() );
        coupon1.setMatch( coupon.getMatch() );
        coupon1.setMismatchReason( coupon.getMismatchReason() );

        return coupon1;
    }

    protected List<AppTradeOrderSettlementRespVO.Coupon> couponListToCouponList(List<TradePriceCalculateRespBO.Coupon> list) {
        if ( list == null ) {
            return null;
        }

        List<AppTradeOrderSettlementRespVO.Coupon> list1 = new ArrayList<AppTradeOrderSettlementRespVO.Coupon>( list.size() );
        for ( TradePriceCalculateRespBO.Coupon coupon : list ) {
            list1.add( couponToCoupon( coupon ) );
        }

        return list1;
    }

    protected AppTradeOrderSettlementRespVO.Price priceToPrice(TradePriceCalculateRespBO.Price price) {
        if ( price == null ) {
            return null;
        }

        AppTradeOrderSettlementRespVO.Price price1 = new AppTradeOrderSettlementRespVO.Price();

        price1.setTotalPrice( price.getTotalPrice() );
        price1.setDiscountPrice( price.getDiscountPrice() );
        price1.setDeliveryPrice( price.getDeliveryPrice() );
        price1.setCouponPrice( price.getCouponPrice() );
        price1.setPointPrice( price.getPointPrice() );
        price1.setVipPrice( price.getVipPrice() );
        price1.setPayPrice( price.getPayPrice() );

        return price1;
    }

    protected AppTradeOrderSettlementRespVO.Address memberAddressRespDTOToAddress(MemberAddressRespDTO memberAddressRespDTO) {
        if ( memberAddressRespDTO == null ) {
            return null;
        }

        AppTradeOrderSettlementRespVO.Address address = new AppTradeOrderSettlementRespVO.Address();

        address.setId( memberAddressRespDTO.getId() );
        address.setName( memberAddressRespDTO.getName() );
        address.setMobile( memberAddressRespDTO.getMobile() );
        if ( memberAddressRespDTO.getAreaId() != null ) {
            address.setAreaId( memberAddressRespDTO.getAreaId().longValue() );
        }
        address.setDetailAddress( memberAddressRespDTO.getDetailAddress() );
        address.setDefaultStatus( memberAddressRespDTO.getDefaultStatus() );

        return address;
    }

    protected AppOrderExpressTrackRespDTO expressTrackRespDTOToAppOrderExpressTrackRespDTO(ExpressTrackRespDTO expressTrackRespDTO) {
        if ( expressTrackRespDTO == null ) {
            return null;
        }

        AppOrderExpressTrackRespDTO appOrderExpressTrackRespDTO = new AppOrderExpressTrackRespDTO();

        appOrderExpressTrackRespDTO.setTime( expressTrackRespDTO.getTime() );
        appOrderExpressTrackRespDTO.setContent( expressTrackRespDTO.getContent() );

        return appOrderExpressTrackRespDTO;
    }
}
