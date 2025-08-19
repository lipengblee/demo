package cn.iocoder.yudao.module.trade.convert.aftersale;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.pay.api.refund.dto.PayRefundCreateReqDTO;
import cn.iocoder.yudao.module.product.api.property.dto.ProductPropertyValueDetailRespDTO;
import cn.iocoder.yudao.module.trade.controller.admin.aftersale.vo.AfterSaleDetailRespVO;
import cn.iocoder.yudao.module.trade.controller.admin.aftersale.vo.AfterSaleRespPageItemVO;
import cn.iocoder.yudao.module.trade.controller.admin.aftersale.vo.log.AfterSaleLogRespVO;
import cn.iocoder.yudao.module.trade.controller.admin.base.member.user.MemberUserRespVO;
import cn.iocoder.yudao.module.trade.controller.admin.base.product.property.ProductPropertyValueDetailRespVO;
import cn.iocoder.yudao.module.trade.controller.admin.order.vo.TradeOrderBaseVO;
import cn.iocoder.yudao.module.trade.controller.app.aftersale.vo.AppAfterSaleCreateReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.aftersale.AfterSaleDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.aftersale.AfterSaleLogDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import cn.iocoder.yudao.module.trade.framework.order.config.TradeOrderProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-18T17:52:03+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class AfterSaleConvertImpl implements AfterSaleConvert {

    @Override
    public AfterSaleDO convert(AppAfterSaleCreateReqVO createReqVO, TradeOrderItemDO tradeOrderItem) {
        if ( createReqVO == null && tradeOrderItem == null ) {
            return null;
        }

        AfterSaleDO afterSaleDO = new AfterSaleDO();

        if ( createReqVO != null ) {
            afterSaleDO.setWay( createReqVO.getWay() );
            afterSaleDO.setApplyReason( createReqVO.getApplyReason() );
            afterSaleDO.setApplyDescription( createReqVO.getApplyDescription() );
            List<String> list = createReqVO.getApplyPicUrls();
            if ( list != null ) {
                afterSaleDO.setApplyPicUrls( new ArrayList<String>( list ) );
            }
            afterSaleDO.setOrderItemId( createReqVO.getOrderItemId() );
            afterSaleDO.setRefundPrice( createReqVO.getRefundPrice() );
        }
        if ( tradeOrderItem != null ) {
            afterSaleDO.setDeleted( tradeOrderItem.getDeleted() );
            afterSaleDO.setUserId( tradeOrderItem.getUserId() );
            afterSaleDO.setOrderId( tradeOrderItem.getOrderId() );
            afterSaleDO.setSpuId( tradeOrderItem.getSpuId() );
            afterSaleDO.setSpuName( tradeOrderItem.getSpuName() );
            afterSaleDO.setSkuId( tradeOrderItem.getSkuId() );
            List<TradeOrderItemDO.Property> list1 = tradeOrderItem.getProperties();
            if ( list1 != null ) {
                afterSaleDO.setProperties( new ArrayList<TradeOrderItemDO.Property>( list1 ) );
            }
            afterSaleDO.setPicUrl( tradeOrderItem.getPicUrl() );
            afterSaleDO.setCount( tradeOrderItem.getCount() );
            if ( afterSaleDO.getTransMap() != null ) {
                Map<String, Object> map = tradeOrderItem.getTransMap();
                if ( map != null ) {
                    afterSaleDO.getTransMap().putAll( map );
                }
            }
        }

        return afterSaleDO;
    }

    @Override
    public PayRefundCreateReqDTO convert(String userIp, AfterSaleDO afterSale, TradeOrderProperties orderProperties) {
        if ( userIp == null && afterSale == null && orderProperties == null ) {
            return null;
        }

        PayRefundCreateReqDTO payRefundCreateReqDTO = new PayRefundCreateReqDTO();

        if ( afterSale != null ) {
            if ( afterSale.getOrderId() != null ) {
                payRefundCreateReqDTO.setMerchantOrderId( String.valueOf( afterSale.getOrderId() ) );
            }
            if ( afterSale.getId() != null ) {
                payRefundCreateReqDTO.setMerchantRefundId( String.valueOf( afterSale.getId() ) );
            }
            payRefundCreateReqDTO.setReason( afterSale.getApplyReason() );
            payRefundCreateReqDTO.setPrice( afterSale.getRefundPrice() );
            payRefundCreateReqDTO.setUserId( afterSale.getUserId() );
        }
        if ( orderProperties != null ) {
            payRefundCreateReqDTO.setAppKey( orderProperties.getPayAppKey() );
        }
        payRefundCreateReqDTO.setUserIp( userIp );

        return payRefundCreateReqDTO;
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
    public PageResult<AfterSaleRespPageItemVO> convertPage(PageResult<AfterSaleDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<AfterSaleRespPageItemVO> pageResult = new PageResult<AfterSaleRespPageItemVO>();

        pageResult.setList( afterSaleDOListToAfterSaleRespPageItemVOList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
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
    public List<AfterSaleLogRespVO> convertList1(List<AfterSaleLogDO> list) {
        if ( list == null ) {
            return null;
        }

        List<AfterSaleLogRespVO> list1 = new ArrayList<AfterSaleLogRespVO>( list.size() );
        for ( AfterSaleLogDO afterSaleLogDO : list ) {
            list1.add( afterSaleLogDOToAfterSaleLogRespVO( afterSaleLogDO ) );
        }

        return list1;
    }

    @Override
    public AfterSaleDetailRespVO convert02(AfterSaleDO bean) {
        if ( bean == null ) {
            return null;
        }

        AfterSaleDetailRespVO afterSaleDetailRespVO = new AfterSaleDetailRespVO();

        afterSaleDetailRespVO.setNo( bean.getNo() );
        afterSaleDetailRespVO.setStatus( bean.getStatus() );
        afterSaleDetailRespVO.setType( bean.getType() );
        afterSaleDetailRespVO.setWay( bean.getWay() );
        afterSaleDetailRespVO.setUserId( bean.getUserId() );
        afterSaleDetailRespVO.setApplyReason( bean.getApplyReason() );
        afterSaleDetailRespVO.setApplyDescription( bean.getApplyDescription() );
        List<String> list = bean.getApplyPicUrls();
        if ( list != null ) {
            afterSaleDetailRespVO.setApplyPicUrls( new ArrayList<String>( list ) );
        }
        afterSaleDetailRespVO.setOrderId( bean.getOrderId() );
        afterSaleDetailRespVO.setOrderNo( bean.getOrderNo() );
        afterSaleDetailRespVO.setOrderItemId( bean.getOrderItemId() );
        afterSaleDetailRespVO.setSpuId( bean.getSpuId() );
        afterSaleDetailRespVO.setSpuName( bean.getSpuName() );
        afterSaleDetailRespVO.setSkuId( bean.getSkuId() );
        afterSaleDetailRespVO.setPicUrl( bean.getPicUrl() );
        afterSaleDetailRespVO.setCount( bean.getCount() );
        afterSaleDetailRespVO.setAuditTime( bean.getAuditTime() );
        afterSaleDetailRespVO.setAuditUserId( bean.getAuditUserId() );
        afterSaleDetailRespVO.setAuditReason( bean.getAuditReason() );
        afterSaleDetailRespVO.setRefundPrice( bean.getRefundPrice() );
        afterSaleDetailRespVO.setPayRefundId( bean.getPayRefundId() );
        afterSaleDetailRespVO.setRefundTime( bean.getRefundTime() );
        afterSaleDetailRespVO.setLogisticsId( bean.getLogisticsId() );
        afterSaleDetailRespVO.setLogisticsNo( bean.getLogisticsNo() );
        afterSaleDetailRespVO.setDeliveryTime( bean.getDeliveryTime() );
        afterSaleDetailRespVO.setReceiveTime( bean.getReceiveTime() );
        afterSaleDetailRespVO.setReceiveReason( bean.getReceiveReason() );
        afterSaleDetailRespVO.setId( bean.getId() );

        return afterSaleDetailRespVO;
    }

    @Override
    public AfterSaleDetailRespVO.OrderItem convert02(TradeOrderItemDO bean) {
        if ( bean == null ) {
            return null;
        }

        AfterSaleDetailRespVO.OrderItem orderItem = new AfterSaleDetailRespVO.OrderItem();

        orderItem.setId( bean.getId() );
        orderItem.setUserId( bean.getUserId() );
        orderItem.setOrderId( bean.getOrderId() );
        orderItem.setSpuId( bean.getSpuId() );
        orderItem.setSpuName( bean.getSpuName() );
        orderItem.setSkuId( bean.getSkuId() );
        orderItem.setPicUrl( bean.getPicUrl() );
        orderItem.setCount( bean.getCount() );
        orderItem.setPrice( bean.getPrice() );
        orderItem.setDiscountPrice( bean.getDiscountPrice() );
        orderItem.setPayPrice( bean.getPayPrice() );
        orderItem.setAfterSaleStatus( bean.getAfterSaleStatus() );
        orderItem.setProperties( propertyListToProductPropertyValueDetailRespVOList( bean.getProperties() ) );

        return orderItem;
    }

    @Override
    public TradeOrderBaseVO convert(TradeOrderDO bean) {
        if ( bean == null ) {
            return null;
        }

        TradeOrderBaseVO tradeOrderBaseVO = new TradeOrderBaseVO();

        tradeOrderBaseVO.setId( bean.getId() );
        tradeOrderBaseVO.setNo( bean.getNo() );
        tradeOrderBaseVO.setCreateTime( bean.getCreateTime() );
        tradeOrderBaseVO.setType( bean.getType() );
        tradeOrderBaseVO.setTerminal( bean.getTerminal() );
        tradeOrderBaseVO.setUserId( bean.getUserId() );
        tradeOrderBaseVO.setUserIp( bean.getUserIp() );
        tradeOrderBaseVO.setUserRemark( bean.getUserRemark() );
        tradeOrderBaseVO.setStatus( bean.getStatus() );
        tradeOrderBaseVO.setProductCount( bean.getProductCount() );
        tradeOrderBaseVO.setFinishTime( bean.getFinishTime() );
        tradeOrderBaseVO.setCancelTime( bean.getCancelTime() );
        tradeOrderBaseVO.setCancelType( bean.getCancelType() );
        tradeOrderBaseVO.setRemark( bean.getRemark() );
        tradeOrderBaseVO.setPayOrderId( bean.getPayOrderId() );
        tradeOrderBaseVO.setPayStatus( bean.getPayStatus() );
        tradeOrderBaseVO.setPayTime( bean.getPayTime() );
        tradeOrderBaseVO.setPayChannelCode( bean.getPayChannelCode() );
        tradeOrderBaseVO.setTotalPrice( bean.getTotalPrice() );
        tradeOrderBaseVO.setDiscountPrice( bean.getDiscountPrice() );
        tradeOrderBaseVO.setDeliveryPrice( bean.getDeliveryPrice() );
        tradeOrderBaseVO.setAdjustPrice( bean.getAdjustPrice() );
        tradeOrderBaseVO.setPayPrice( bean.getPayPrice() );
        tradeOrderBaseVO.setDeliveryType( bean.getDeliveryType() );
        tradeOrderBaseVO.setPickUpStoreId( bean.getPickUpStoreId() );
        if ( bean.getPickUpVerifyCode() != null ) {
            tradeOrderBaseVO.setPickUpVerifyCode( Long.parseLong( bean.getPickUpVerifyCode() ) );
        }
        tradeOrderBaseVO.setLogisticsId( bean.getLogisticsId() );
        tradeOrderBaseVO.setLogisticsNo( bean.getLogisticsNo() );
        tradeOrderBaseVO.setDeliveryTime( bean.getDeliveryTime() );
        tradeOrderBaseVO.setReceiveTime( bean.getReceiveTime() );
        tradeOrderBaseVO.setReceiverName( bean.getReceiverName() );
        tradeOrderBaseVO.setReceiverMobile( bean.getReceiverMobile() );
        tradeOrderBaseVO.setReceiverAreaId( bean.getReceiverAreaId() );
        tradeOrderBaseVO.setReceiverDetailAddress( bean.getReceiverDetailAddress() );
        tradeOrderBaseVO.setRefundPrice( bean.getRefundPrice() );
        tradeOrderBaseVO.setCouponId( bean.getCouponId() );
        tradeOrderBaseVO.setCouponPrice( bean.getCouponPrice() );
        tradeOrderBaseVO.setPointPrice( bean.getPointPrice() );
        tradeOrderBaseVO.setVipPrice( bean.getVipPrice() );
        tradeOrderBaseVO.setBrokerageUserId( bean.getBrokerageUserId() );

        return tradeOrderBaseVO;
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

    protected AfterSaleRespPageItemVO afterSaleDOToAfterSaleRespPageItemVO(AfterSaleDO afterSaleDO) {
        if ( afterSaleDO == null ) {
            return null;
        }

        AfterSaleRespPageItemVO afterSaleRespPageItemVO = new AfterSaleRespPageItemVO();

        afterSaleRespPageItemVO.setNo( afterSaleDO.getNo() );
        afterSaleRespPageItemVO.setStatus( afterSaleDO.getStatus() );
        afterSaleRespPageItemVO.setType( afterSaleDO.getType() );
        afterSaleRespPageItemVO.setWay( afterSaleDO.getWay() );
        afterSaleRespPageItemVO.setUserId( afterSaleDO.getUserId() );
        afterSaleRespPageItemVO.setApplyReason( afterSaleDO.getApplyReason() );
        afterSaleRespPageItemVO.setApplyDescription( afterSaleDO.getApplyDescription() );
        List<String> list = afterSaleDO.getApplyPicUrls();
        if ( list != null ) {
            afterSaleRespPageItemVO.setApplyPicUrls( new ArrayList<String>( list ) );
        }
        afterSaleRespPageItemVO.setOrderId( afterSaleDO.getOrderId() );
        afterSaleRespPageItemVO.setOrderNo( afterSaleDO.getOrderNo() );
        afterSaleRespPageItemVO.setOrderItemId( afterSaleDO.getOrderItemId() );
        afterSaleRespPageItemVO.setSpuId( afterSaleDO.getSpuId() );
        afterSaleRespPageItemVO.setSpuName( afterSaleDO.getSpuName() );
        afterSaleRespPageItemVO.setSkuId( afterSaleDO.getSkuId() );
        afterSaleRespPageItemVO.setPicUrl( afterSaleDO.getPicUrl() );
        afterSaleRespPageItemVO.setCount( afterSaleDO.getCount() );
        afterSaleRespPageItemVO.setAuditTime( afterSaleDO.getAuditTime() );
        afterSaleRespPageItemVO.setAuditUserId( afterSaleDO.getAuditUserId() );
        afterSaleRespPageItemVO.setAuditReason( afterSaleDO.getAuditReason() );
        afterSaleRespPageItemVO.setRefundPrice( afterSaleDO.getRefundPrice() );
        afterSaleRespPageItemVO.setPayRefundId( afterSaleDO.getPayRefundId() );
        afterSaleRespPageItemVO.setRefundTime( afterSaleDO.getRefundTime() );
        afterSaleRespPageItemVO.setLogisticsId( afterSaleDO.getLogisticsId() );
        afterSaleRespPageItemVO.setLogisticsNo( afterSaleDO.getLogisticsNo() );
        afterSaleRespPageItemVO.setDeliveryTime( afterSaleDO.getDeliveryTime() );
        afterSaleRespPageItemVO.setReceiveTime( afterSaleDO.getReceiveTime() );
        afterSaleRespPageItemVO.setReceiveReason( afterSaleDO.getReceiveReason() );
        afterSaleRespPageItemVO.setId( afterSaleDO.getId() );
        afterSaleRespPageItemVO.setCreateTime( afterSaleDO.getCreateTime() );
        afterSaleRespPageItemVO.setProperties( propertyListToProductPropertyValueDetailRespVOList( afterSaleDO.getProperties() ) );

        return afterSaleRespPageItemVO;
    }

    protected List<AfterSaleRespPageItemVO> afterSaleDOListToAfterSaleRespPageItemVOList(List<AfterSaleDO> list) {
        if ( list == null ) {
            return null;
        }

        List<AfterSaleRespPageItemVO> list1 = new ArrayList<AfterSaleRespPageItemVO>( list.size() );
        for ( AfterSaleDO afterSaleDO : list ) {
            list1.add( afterSaleDOToAfterSaleRespPageItemVO( afterSaleDO ) );
        }

        return list1;
    }

    protected AfterSaleLogRespVO afterSaleLogDOToAfterSaleLogRespVO(AfterSaleLogDO afterSaleLogDO) {
        if ( afterSaleLogDO == null ) {
            return null;
        }

        AfterSaleLogRespVO afterSaleLogRespVO = new AfterSaleLogRespVO();

        afterSaleLogRespVO.setId( afterSaleLogDO.getId() );
        afterSaleLogRespVO.setUserId( afterSaleLogDO.getUserId() );
        afterSaleLogRespVO.setUserType( afterSaleLogDO.getUserType() );
        afterSaleLogRespVO.setAfterSaleId( afterSaleLogDO.getAfterSaleId() );
        afterSaleLogRespVO.setBeforeStatus( afterSaleLogDO.getBeforeStatus() );
        afterSaleLogRespVO.setAfterStatus( afterSaleLogDO.getAfterStatus() );
        afterSaleLogRespVO.setContent( afterSaleLogDO.getContent() );
        afterSaleLogRespVO.setCreateTime( afterSaleLogDO.getCreateTime() );

        return afterSaleLogRespVO;
    }
}
