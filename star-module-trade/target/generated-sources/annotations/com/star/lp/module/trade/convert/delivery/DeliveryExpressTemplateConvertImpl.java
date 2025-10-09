package com.star.lp.module.trade.convert.delivery;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.trade.controller.admin.delivery.vo.expresstemplate.DeliveryExpressTemplateChargeBaseVO;
import com.star.lp.module.trade.controller.admin.delivery.vo.expresstemplate.DeliveryExpressTemplateCreateReqVO;
import com.star.lp.module.trade.controller.admin.delivery.vo.expresstemplate.DeliveryExpressTemplateDetailRespVO;
import com.star.lp.module.trade.controller.admin.delivery.vo.expresstemplate.DeliveryExpressTemplateFreeBaseVO;
import com.star.lp.module.trade.controller.admin.delivery.vo.expresstemplate.DeliveryExpressTemplateRespVO;
import com.star.lp.module.trade.controller.admin.delivery.vo.expresstemplate.DeliveryExpressTemplateSimpleRespVO;
import com.star.lp.module.trade.controller.admin.delivery.vo.expresstemplate.DeliveryExpressTemplateUpdateReqVO;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryExpressTemplateChargeDO;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryExpressTemplateDO;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryExpressTemplateFreeDO;
import com.star.lp.module.trade.service.delivery.bo.DeliveryExpressTemplateRespBO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-03T12:28:03+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class DeliveryExpressTemplateConvertImpl implements DeliveryExpressTemplateConvert {

    @Override
    public DeliveryExpressTemplateDO convert(DeliveryExpressTemplateCreateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        DeliveryExpressTemplateDO deliveryExpressTemplateDO = new DeliveryExpressTemplateDO();

        deliveryExpressTemplateDO.setName( bean.getName() );
        deliveryExpressTemplateDO.setChargeMode( bean.getChargeMode() );
        deliveryExpressTemplateDO.setSort( bean.getSort() );

        return deliveryExpressTemplateDO;
    }

    @Override
    public DeliveryExpressTemplateDO convert(DeliveryExpressTemplateUpdateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        DeliveryExpressTemplateDO deliveryExpressTemplateDO = new DeliveryExpressTemplateDO();

        deliveryExpressTemplateDO.setId( bean.getId() );
        deliveryExpressTemplateDO.setName( bean.getName() );
        deliveryExpressTemplateDO.setChargeMode( bean.getChargeMode() );
        deliveryExpressTemplateDO.setSort( bean.getSort() );

        return deliveryExpressTemplateDO;
    }

    @Override
    public DeliveryExpressTemplateRespVO convert(DeliveryExpressTemplateDO bean) {
        if ( bean == null ) {
            return null;
        }

        DeliveryExpressTemplateRespVO deliveryExpressTemplateRespVO = new DeliveryExpressTemplateRespVO();

        deliveryExpressTemplateRespVO.setName( bean.getName() );
        deliveryExpressTemplateRespVO.setChargeMode( bean.getChargeMode() );
        deliveryExpressTemplateRespVO.setSort( bean.getSort() );
        deliveryExpressTemplateRespVO.setId( bean.getId() );
        deliveryExpressTemplateRespVO.setCreateTime( bean.getCreateTime() );

        return deliveryExpressTemplateRespVO;
    }

    @Override
    public DeliveryExpressTemplateDetailRespVO convert2(DeliveryExpressTemplateDO bean) {
        if ( bean == null ) {
            return null;
        }

        DeliveryExpressTemplateDetailRespVO deliveryExpressTemplateDetailRespVO = new DeliveryExpressTemplateDetailRespVO();

        deliveryExpressTemplateDetailRespVO.setName( bean.getName() );
        deliveryExpressTemplateDetailRespVO.setChargeMode( bean.getChargeMode() );
        deliveryExpressTemplateDetailRespVO.setSort( bean.getSort() );
        deliveryExpressTemplateDetailRespVO.setId( bean.getId() );

        return deliveryExpressTemplateDetailRespVO;
    }

    @Override
    public List<DeliveryExpressTemplateRespVO> convertList(List<DeliveryExpressTemplateDO> list) {
        if ( list == null ) {
            return null;
        }

        List<DeliveryExpressTemplateRespVO> list1 = new ArrayList<DeliveryExpressTemplateRespVO>( list.size() );
        for ( DeliveryExpressTemplateDO deliveryExpressTemplateDO : list ) {
            list1.add( convert( deliveryExpressTemplateDO ) );
        }

        return list1;
    }

    @Override
    public List<DeliveryExpressTemplateSimpleRespVO> convertList1(List<DeliveryExpressTemplateDO> list) {
        if ( list == null ) {
            return null;
        }

        List<DeliveryExpressTemplateSimpleRespVO> list1 = new ArrayList<DeliveryExpressTemplateSimpleRespVO>( list.size() );
        for ( DeliveryExpressTemplateDO deliveryExpressTemplateDO : list ) {
            list1.add( deliveryExpressTemplateDOToDeliveryExpressTemplateSimpleRespVO( deliveryExpressTemplateDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<DeliveryExpressTemplateRespVO> convertPage(PageResult<DeliveryExpressTemplateDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<DeliveryExpressTemplateRespVO> pageResult = new PageResult<DeliveryExpressTemplateRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public DeliveryExpressTemplateChargeDO convertTemplateCharge(Long templateId, Integer chargeMode, DeliveryExpressTemplateChargeBaseVO vo) {
        if ( templateId == null && chargeMode == null && vo == null ) {
            return null;
        }

        DeliveryExpressTemplateChargeDO deliveryExpressTemplateChargeDO = new DeliveryExpressTemplateChargeDO();

        if ( vo != null ) {
            deliveryExpressTemplateChargeDO.setId( vo.getId() );
            List<Integer> list = vo.getAreaIds();
            if ( list != null ) {
                deliveryExpressTemplateChargeDO.setAreaIds( new ArrayList<Integer>( list ) );
            }
            deliveryExpressTemplateChargeDO.setStartCount( vo.getStartCount() );
            deliveryExpressTemplateChargeDO.setStartPrice( vo.getStartPrice() );
            deliveryExpressTemplateChargeDO.setExtraCount( vo.getExtraCount() );
            deliveryExpressTemplateChargeDO.setExtraPrice( vo.getExtraPrice() );
        }
        deliveryExpressTemplateChargeDO.setTemplateId( templateId );
        deliveryExpressTemplateChargeDO.setChargeMode( chargeMode );

        return deliveryExpressTemplateChargeDO;
    }

    @Override
    public DeliveryExpressTemplateRespBO.Charge convertTemplateCharge(DeliveryExpressTemplateChargeDO bean) {
        if ( bean == null ) {
            return null;
        }

        DeliveryExpressTemplateRespBO.Charge charge = new DeliveryExpressTemplateRespBO.Charge();

        charge.setStartCount( bean.getStartCount() );
        charge.setStartPrice( bean.getStartPrice() );
        charge.setExtraCount( bean.getExtraCount() );
        charge.setExtraPrice( bean.getExtraPrice() );

        return charge;
    }

    @Override
    public DeliveryExpressTemplateFreeDO convertTemplateFree(Long templateId, DeliveryExpressTemplateFreeBaseVO vo) {
        if ( templateId == null && vo == null ) {
            return null;
        }

        DeliveryExpressTemplateFreeDO deliveryExpressTemplateFreeDO = new DeliveryExpressTemplateFreeDO();

        if ( vo != null ) {
            List<Integer> list = vo.getAreaIds();
            if ( list != null ) {
                deliveryExpressTemplateFreeDO.setAreaIds( new ArrayList<Integer>( list ) );
            }
            deliveryExpressTemplateFreeDO.setFreePrice( vo.getFreePrice() );
            deliveryExpressTemplateFreeDO.setFreeCount( vo.getFreeCount() );
        }
        deliveryExpressTemplateFreeDO.setTemplateId( templateId );

        return deliveryExpressTemplateFreeDO;
    }

    @Override
    public DeliveryExpressTemplateRespBO.Free convertTemplateFree(DeliveryExpressTemplateFreeDO bean) {
        if ( bean == null ) {
            return null;
        }

        DeliveryExpressTemplateRespBO.Free free = new DeliveryExpressTemplateRespBO.Free();

        free.setFreePrice( bean.getFreePrice() );
        free.setFreeCount( bean.getFreeCount() );

        return free;
    }

    @Override
    public List<DeliveryExpressTemplateChargeBaseVO> convertTemplateChargeList(List<DeliveryExpressTemplateChargeDO> list) {
        if ( list == null ) {
            return null;
        }

        List<DeliveryExpressTemplateChargeBaseVO> list1 = new ArrayList<DeliveryExpressTemplateChargeBaseVO>( list.size() );
        for ( DeliveryExpressTemplateChargeDO deliveryExpressTemplateChargeDO : list ) {
            list1.add( deliveryExpressTemplateChargeDOToDeliveryExpressTemplateChargeBaseVO( deliveryExpressTemplateChargeDO ) );
        }

        return list1;
    }

    @Override
    public List<DeliveryExpressTemplateFreeBaseVO> convertTemplateFreeList(List<DeliveryExpressTemplateFreeDO> list) {
        if ( list == null ) {
            return null;
        }

        List<DeliveryExpressTemplateFreeBaseVO> list1 = new ArrayList<DeliveryExpressTemplateFreeBaseVO>( list.size() );
        for ( DeliveryExpressTemplateFreeDO deliveryExpressTemplateFreeDO : list ) {
            list1.add( deliveryExpressTemplateFreeDOToDeliveryExpressTemplateFreeBaseVO( deliveryExpressTemplateFreeDO ) );
        }

        return list1;
    }

    protected DeliveryExpressTemplateSimpleRespVO deliveryExpressTemplateDOToDeliveryExpressTemplateSimpleRespVO(DeliveryExpressTemplateDO deliveryExpressTemplateDO) {
        if ( deliveryExpressTemplateDO == null ) {
            return null;
        }

        DeliveryExpressTemplateSimpleRespVO deliveryExpressTemplateSimpleRespVO = new DeliveryExpressTemplateSimpleRespVO();

        deliveryExpressTemplateSimpleRespVO.setId( deliveryExpressTemplateDO.getId() );
        deliveryExpressTemplateSimpleRespVO.setName( deliveryExpressTemplateDO.getName() );

        return deliveryExpressTemplateSimpleRespVO;
    }

    protected DeliveryExpressTemplateChargeBaseVO deliveryExpressTemplateChargeDOToDeliveryExpressTemplateChargeBaseVO(DeliveryExpressTemplateChargeDO deliveryExpressTemplateChargeDO) {
        if ( deliveryExpressTemplateChargeDO == null ) {
            return null;
        }

        DeliveryExpressTemplateChargeBaseVO deliveryExpressTemplateChargeBaseVO = new DeliveryExpressTemplateChargeBaseVO();

        deliveryExpressTemplateChargeBaseVO.setId( deliveryExpressTemplateChargeDO.getId() );
        List<Integer> list = deliveryExpressTemplateChargeDO.getAreaIds();
        if ( list != null ) {
            deliveryExpressTemplateChargeBaseVO.setAreaIds( new ArrayList<Integer>( list ) );
        }
        deliveryExpressTemplateChargeBaseVO.setStartCount( deliveryExpressTemplateChargeDO.getStartCount() );
        deliveryExpressTemplateChargeBaseVO.setStartPrice( deliveryExpressTemplateChargeDO.getStartPrice() );
        deliveryExpressTemplateChargeBaseVO.setExtraCount( deliveryExpressTemplateChargeDO.getExtraCount() );
        deliveryExpressTemplateChargeBaseVO.setExtraPrice( deliveryExpressTemplateChargeDO.getExtraPrice() );

        return deliveryExpressTemplateChargeBaseVO;
    }

    protected DeliveryExpressTemplateFreeBaseVO deliveryExpressTemplateFreeDOToDeliveryExpressTemplateFreeBaseVO(DeliveryExpressTemplateFreeDO deliveryExpressTemplateFreeDO) {
        if ( deliveryExpressTemplateFreeDO == null ) {
            return null;
        }

        DeliveryExpressTemplateFreeBaseVO deliveryExpressTemplateFreeBaseVO = new DeliveryExpressTemplateFreeBaseVO();

        List<Integer> list = deliveryExpressTemplateFreeDO.getAreaIds();
        if ( list != null ) {
            deliveryExpressTemplateFreeBaseVO.setAreaIds( new ArrayList<Integer>( list ) );
        }
        deliveryExpressTemplateFreeBaseVO.setFreePrice( deliveryExpressTemplateFreeDO.getFreePrice() );
        deliveryExpressTemplateFreeBaseVO.setFreeCount( deliveryExpressTemplateFreeDO.getFreeCount() );

        return deliveryExpressTemplateFreeBaseVO;
    }
}
