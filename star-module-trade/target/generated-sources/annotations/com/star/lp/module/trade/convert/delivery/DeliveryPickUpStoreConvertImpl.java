package com.star.lp.module.trade.convert.delivery;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.trade.controller.admin.delivery.vo.pickup.DeliveryPickUpStoreCreateReqVO;
import com.star.lp.module.trade.controller.admin.delivery.vo.pickup.DeliveryPickUpStoreRespVO;
import com.star.lp.module.trade.controller.admin.delivery.vo.pickup.DeliveryPickUpStoreSimpleRespVO;
import com.star.lp.module.trade.controller.admin.delivery.vo.pickup.DeliveryPickUpStoreUpdateReqVO;
import com.star.lp.module.trade.controller.app.delivery.vo.pickup.AppDeliveryPickUpStoreRespVO;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-03T13:36:43+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class DeliveryPickUpStoreConvertImpl implements DeliveryPickUpStoreConvert {

    @Override
    public DeliveryPickUpStoreDO convert(DeliveryPickUpStoreCreateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        DeliveryPickUpStoreDO deliveryPickUpStoreDO = new DeliveryPickUpStoreDO();

        deliveryPickUpStoreDO.setName( bean.getName() );
        deliveryPickUpStoreDO.setIntroduction( bean.getIntroduction() );
        deliveryPickUpStoreDO.setPhone( bean.getPhone() );
        deliveryPickUpStoreDO.setAreaId( bean.getAreaId() );
        deliveryPickUpStoreDO.setDetailAddress( bean.getDetailAddress() );
        deliveryPickUpStoreDO.setLogo( bean.getLogo() );
        deliveryPickUpStoreDO.setOpeningTime( bean.getOpeningTime() );
        deliveryPickUpStoreDO.setClosingTime( bean.getClosingTime() );
        deliveryPickUpStoreDO.setLatitude( bean.getLatitude() );
        deliveryPickUpStoreDO.setLongitude( bean.getLongitude() );
        deliveryPickUpStoreDO.setStatus( bean.getStatus() );

        return deliveryPickUpStoreDO;
    }

    @Override
    public DeliveryPickUpStoreDO convert(DeliveryPickUpStoreUpdateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        DeliveryPickUpStoreDO deliveryPickUpStoreDO = new DeliveryPickUpStoreDO();

        deliveryPickUpStoreDO.setId( bean.getId() );
        deliveryPickUpStoreDO.setName( bean.getName() );
        deliveryPickUpStoreDO.setIntroduction( bean.getIntroduction() );
        deliveryPickUpStoreDO.setPhone( bean.getPhone() );
        deliveryPickUpStoreDO.setAreaId( bean.getAreaId() );
        deliveryPickUpStoreDO.setDetailAddress( bean.getDetailAddress() );
        deliveryPickUpStoreDO.setLogo( bean.getLogo() );
        deliveryPickUpStoreDO.setOpeningTime( bean.getOpeningTime() );
        deliveryPickUpStoreDO.setClosingTime( bean.getClosingTime() );
        deliveryPickUpStoreDO.setLatitude( bean.getLatitude() );
        deliveryPickUpStoreDO.setLongitude( bean.getLongitude() );
        deliveryPickUpStoreDO.setStatus( bean.getStatus() );

        return deliveryPickUpStoreDO;
    }

    @Override
    public List<DeliveryPickUpStoreRespVO> convertList(List<DeliveryPickUpStoreDO> list) {
        if ( list == null ) {
            return null;
        }

        List<DeliveryPickUpStoreRespVO> list1 = new ArrayList<DeliveryPickUpStoreRespVO>( list.size() );
        for ( DeliveryPickUpStoreDO deliveryPickUpStoreDO : list ) {
            list1.add( deliveryPickUpStoreDOToDeliveryPickUpStoreRespVO( deliveryPickUpStoreDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<DeliveryPickUpStoreRespVO> convertPage(PageResult<DeliveryPickUpStoreDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<DeliveryPickUpStoreRespVO> pageResult = new PageResult<DeliveryPickUpStoreRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public List<DeliveryPickUpStoreSimpleRespVO> convertList1(List<DeliveryPickUpStoreDO> list) {
        if ( list == null ) {
            return null;
        }

        List<DeliveryPickUpStoreSimpleRespVO> list1 = new ArrayList<DeliveryPickUpStoreSimpleRespVO>( list.size() );
        for ( DeliveryPickUpStoreDO deliveryPickUpStoreDO : list ) {
            list1.add( convert02( deliveryPickUpStoreDO ) );
        }

        return list1;
    }

    @Override
    public DeliveryPickUpStoreSimpleRespVO convert02(DeliveryPickUpStoreDO bean) {
        if ( bean == null ) {
            return null;
        }

        DeliveryPickUpStoreSimpleRespVO deliveryPickUpStoreSimpleRespVO = new DeliveryPickUpStoreSimpleRespVO();

        deliveryPickUpStoreSimpleRespVO.setAreaName( convertAreaIdToAreaName( bean.getAreaId() ) );
        deliveryPickUpStoreSimpleRespVO.setId( bean.getId() );
        deliveryPickUpStoreSimpleRespVO.setName( bean.getName() );
        deliveryPickUpStoreSimpleRespVO.setPhone( bean.getPhone() );
        deliveryPickUpStoreSimpleRespVO.setAreaId( bean.getAreaId() );
        deliveryPickUpStoreSimpleRespVO.setDetailAddress( bean.getDetailAddress() );
        List<Long> list = bean.getVerifyUserIds();
        if ( list != null ) {
            deliveryPickUpStoreSimpleRespVO.setVerifyUserIds( new ArrayList<Long>( list ) );
        }

        return deliveryPickUpStoreSimpleRespVO;
    }

    @Override
    public AppDeliveryPickUpStoreRespVO convert03(DeliveryPickUpStoreDO bean) {
        if ( bean == null ) {
            return null;
        }

        AppDeliveryPickUpStoreRespVO appDeliveryPickUpStoreRespVO = new AppDeliveryPickUpStoreRespVO();

        appDeliveryPickUpStoreRespVO.setAreaName( convertAreaIdToAreaName( bean.getAreaId() ) );
        appDeliveryPickUpStoreRespVO.setId( bean.getId() );
        appDeliveryPickUpStoreRespVO.setName( bean.getName() );
        appDeliveryPickUpStoreRespVO.setLogo( bean.getLogo() );
        appDeliveryPickUpStoreRespVO.setPhone( bean.getPhone() );
        appDeliveryPickUpStoreRespVO.setAreaId( bean.getAreaId() );
        appDeliveryPickUpStoreRespVO.setDetailAddress( bean.getDetailAddress() );
        appDeliveryPickUpStoreRespVO.setOpeningTime( bean.getOpeningTime() );
        appDeliveryPickUpStoreRespVO.setClosingTime( bean.getClosingTime() );
        appDeliveryPickUpStoreRespVO.setLatitude( bean.getLatitude() );
        appDeliveryPickUpStoreRespVO.setLongitude( bean.getLongitude() );

        return appDeliveryPickUpStoreRespVO;
    }

    protected DeliveryPickUpStoreRespVO deliveryPickUpStoreDOToDeliveryPickUpStoreRespVO(DeliveryPickUpStoreDO deliveryPickUpStoreDO) {
        if ( deliveryPickUpStoreDO == null ) {
            return null;
        }

        DeliveryPickUpStoreRespVO deliveryPickUpStoreRespVO = new DeliveryPickUpStoreRespVO();

        deliveryPickUpStoreRespVO.setName( deliveryPickUpStoreDO.getName() );
        deliveryPickUpStoreRespVO.setIntroduction( deliveryPickUpStoreDO.getIntroduction() );
        deliveryPickUpStoreRespVO.setPhone( deliveryPickUpStoreDO.getPhone() );
        deliveryPickUpStoreRespVO.setAreaId( deliveryPickUpStoreDO.getAreaId() );
        deliveryPickUpStoreRespVO.setDetailAddress( deliveryPickUpStoreDO.getDetailAddress() );
        deliveryPickUpStoreRespVO.setLogo( deliveryPickUpStoreDO.getLogo() );
        deliveryPickUpStoreRespVO.setOpeningTime( deliveryPickUpStoreDO.getOpeningTime() );
        deliveryPickUpStoreRespVO.setClosingTime( deliveryPickUpStoreDO.getClosingTime() );
        deliveryPickUpStoreRespVO.setLatitude( deliveryPickUpStoreDO.getLatitude() );
        deliveryPickUpStoreRespVO.setLongitude( deliveryPickUpStoreDO.getLongitude() );
        deliveryPickUpStoreRespVO.setStatus( deliveryPickUpStoreDO.getStatus() );
        deliveryPickUpStoreRespVO.setId( deliveryPickUpStoreDO.getId() );
        deliveryPickUpStoreRespVO.setCreateTime( deliveryPickUpStoreDO.getCreateTime() );

        return deliveryPickUpStoreRespVO;
    }
}
