package cn.iocoder.yudao.module.promotion.convert.coupon;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.template.CouponTemplateCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.template.CouponTemplatePageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.template.CouponTemplateRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.template.CouponTemplateUpdateReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.coupon.vo.template.AppCouponTemplatePageReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.coupon.vo.template.AppCouponTemplateRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.coupon.CouponTemplateDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-18T17:22:15+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class CouponTemplateConvertImpl implements CouponTemplateConvert {

    @Override
    public CouponTemplateDO convert(CouponTemplateCreateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        CouponTemplateDO couponTemplateDO = new CouponTemplateDO();

        couponTemplateDO.setName( bean.getName() );
        couponTemplateDO.setDescription( bean.getDescription() );
        couponTemplateDO.setTotalCount( bean.getTotalCount() );
        couponTemplateDO.setTakeLimitCount( bean.getTakeLimitCount() );
        couponTemplateDO.setTakeType( bean.getTakeType() );
        couponTemplateDO.setUsePrice( bean.getUsePrice() );
        couponTemplateDO.setProductScope( bean.getProductScope() );
        List<Long> list = bean.getProductScopeValues();
        if ( list != null ) {
            couponTemplateDO.setProductScopeValues( new ArrayList<Long>( list ) );
        }
        couponTemplateDO.setValidityType( bean.getValidityType() );
        couponTemplateDO.setValidStartTime( bean.getValidStartTime() );
        couponTemplateDO.setValidEndTime( bean.getValidEndTime() );
        couponTemplateDO.setFixedStartTerm( bean.getFixedStartTerm() );
        couponTemplateDO.setFixedEndTerm( bean.getFixedEndTerm() );
        couponTemplateDO.setDiscountType( bean.getDiscountType() );
        couponTemplateDO.setDiscountPercent( bean.getDiscountPercent() );
        couponTemplateDO.setDiscountPrice( bean.getDiscountPrice() );
        couponTemplateDO.setDiscountLimitPrice( bean.getDiscountLimitPrice() );

        return couponTemplateDO;
    }

    @Override
    public CouponTemplateDO convert(CouponTemplateUpdateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        CouponTemplateDO couponTemplateDO = new CouponTemplateDO();

        couponTemplateDO.setId( bean.getId() );
        couponTemplateDO.setName( bean.getName() );
        couponTemplateDO.setDescription( bean.getDescription() );
        couponTemplateDO.setTotalCount( bean.getTotalCount() );
        couponTemplateDO.setTakeLimitCount( bean.getTakeLimitCount() );
        couponTemplateDO.setTakeType( bean.getTakeType() );
        couponTemplateDO.setUsePrice( bean.getUsePrice() );
        couponTemplateDO.setProductScope( bean.getProductScope() );
        List<Long> list = bean.getProductScopeValues();
        if ( list != null ) {
            couponTemplateDO.setProductScopeValues( new ArrayList<Long>( list ) );
        }
        couponTemplateDO.setValidityType( bean.getValidityType() );
        couponTemplateDO.setValidStartTime( bean.getValidStartTime() );
        couponTemplateDO.setValidEndTime( bean.getValidEndTime() );
        couponTemplateDO.setFixedStartTerm( bean.getFixedStartTerm() );
        couponTemplateDO.setFixedEndTerm( bean.getFixedEndTerm() );
        couponTemplateDO.setDiscountType( bean.getDiscountType() );
        couponTemplateDO.setDiscountPercent( bean.getDiscountPercent() );
        couponTemplateDO.setDiscountPrice( bean.getDiscountPrice() );
        couponTemplateDO.setDiscountLimitPrice( bean.getDiscountLimitPrice() );

        return couponTemplateDO;
    }

    @Override
    public CouponTemplateRespVO convert(CouponTemplateDO bean) {
        if ( bean == null ) {
            return null;
        }

        CouponTemplateRespVO couponTemplateRespVO = new CouponTemplateRespVO();

        couponTemplateRespVO.setName( bean.getName() );
        couponTemplateRespVO.setDescription( bean.getDescription() );
        couponTemplateRespVO.setTotalCount( bean.getTotalCount() );
        couponTemplateRespVO.setTakeLimitCount( bean.getTakeLimitCount() );
        couponTemplateRespVO.setTakeType( bean.getTakeType() );
        couponTemplateRespVO.setUsePrice( bean.getUsePrice() );
        couponTemplateRespVO.setProductScope( bean.getProductScope() );
        List<Long> list = bean.getProductScopeValues();
        if ( list != null ) {
            couponTemplateRespVO.setProductScopeValues( new ArrayList<Long>( list ) );
        }
        couponTemplateRespVO.setValidityType( bean.getValidityType() );
        couponTemplateRespVO.setValidStartTime( bean.getValidStartTime() );
        couponTemplateRespVO.setValidEndTime( bean.getValidEndTime() );
        couponTemplateRespVO.setFixedStartTerm( bean.getFixedStartTerm() );
        couponTemplateRespVO.setFixedEndTerm( bean.getFixedEndTerm() );
        couponTemplateRespVO.setDiscountType( bean.getDiscountType() );
        couponTemplateRespVO.setDiscountPercent( bean.getDiscountPercent() );
        couponTemplateRespVO.setDiscountPrice( bean.getDiscountPrice() );
        couponTemplateRespVO.setDiscountLimitPrice( bean.getDiscountLimitPrice() );
        couponTemplateRespVO.setId( bean.getId() );
        couponTemplateRespVO.setStatus( bean.getStatus() );
        couponTemplateRespVO.setTakeCount( bean.getTakeCount() );
        couponTemplateRespVO.setUseCount( bean.getUseCount() );
        couponTemplateRespVO.setCreateTime( bean.getCreateTime() );

        return couponTemplateRespVO;
    }

    @Override
    public PageResult<CouponTemplateRespVO> convertPage(PageResult<CouponTemplateDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<CouponTemplateRespVO> pageResult = new PageResult<CouponTemplateRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public CouponTemplatePageReqVO convert(AppCouponTemplatePageReqVO pageReqVO, List<Integer> canTakeTypes, Integer productScope, Long productScopeValue) {
        if ( pageReqVO == null && canTakeTypes == null && productScope == null && productScopeValue == null ) {
            return null;
        }

        CouponTemplatePageReqVO couponTemplatePageReqVO = new CouponTemplatePageReqVO();

        if ( pageReqVO != null ) {
            couponTemplatePageReqVO.setPageNo( pageReqVO.getPageNo() );
            couponTemplatePageReqVO.setPageSize( pageReqVO.getPageSize() );
            couponTemplatePageReqVO.setProductScope( pageReqVO.getProductScope() );
        }
        List<Integer> list = canTakeTypes;
        if ( list != null ) {
            couponTemplatePageReqVO.setCanTakeTypes( new ArrayList<Integer>( list ) );
        }
        couponTemplatePageReqVO.setProductScopeValue( productScopeValue );

        return couponTemplatePageReqVO;
    }

    @Override
    public PageResult<AppCouponTemplateRespVO> convertAppPage(PageResult<CouponTemplateDO> pageResult) {
        if ( pageResult == null ) {
            return null;
        }

        PageResult<AppCouponTemplateRespVO> pageResult1 = new PageResult<AppCouponTemplateRespVO>();

        pageResult1.setList( convertAppList( pageResult.getList() ) );
        pageResult1.setTotal( pageResult.getTotal() );

        return pageResult1;
    }

    @Override
    public List<AppCouponTemplateRespVO> convertAppList(List<CouponTemplateDO> list) {
        if ( list == null ) {
            return null;
        }

        List<AppCouponTemplateRespVO> list1 = new ArrayList<AppCouponTemplateRespVO>( list.size() );
        for ( CouponTemplateDO couponTemplateDO : list ) {
            list1.add( couponTemplateDOToAppCouponTemplateRespVO( couponTemplateDO ) );
        }

        return list1;
    }

    @Override
    public List<CouponTemplateRespVO> convertList(List<CouponTemplateDO> list) {
        if ( list == null ) {
            return null;
        }

        List<CouponTemplateRespVO> list1 = new ArrayList<CouponTemplateRespVO>( list.size() );
        for ( CouponTemplateDO couponTemplateDO : list ) {
            list1.add( convert( couponTemplateDO ) );
        }

        return list1;
    }

    protected AppCouponTemplateRespVO couponTemplateDOToAppCouponTemplateRespVO(CouponTemplateDO couponTemplateDO) {
        if ( couponTemplateDO == null ) {
            return null;
        }

        AppCouponTemplateRespVO appCouponTemplateRespVO = new AppCouponTemplateRespVO();

        appCouponTemplateRespVO.setId( couponTemplateDO.getId() );
        appCouponTemplateRespVO.setName( couponTemplateDO.getName() );
        appCouponTemplateRespVO.setDescription( couponTemplateDO.getDescription() );
        appCouponTemplateRespVO.setTotalCount( couponTemplateDO.getTotalCount() );
        appCouponTemplateRespVO.setTakeLimitCount( couponTemplateDO.getTakeLimitCount() );
        appCouponTemplateRespVO.setUsePrice( couponTemplateDO.getUsePrice() );
        appCouponTemplateRespVO.setProductScope( couponTemplateDO.getProductScope() );
        List<Long> list = couponTemplateDO.getProductScopeValues();
        if ( list != null ) {
            appCouponTemplateRespVO.setProductScopeValues( new ArrayList<Long>( list ) );
        }
        appCouponTemplateRespVO.setValidityType( couponTemplateDO.getValidityType() );
        appCouponTemplateRespVO.setValidStartTime( couponTemplateDO.getValidStartTime() );
        appCouponTemplateRespVO.setValidEndTime( couponTemplateDO.getValidEndTime() );
        appCouponTemplateRespVO.setFixedStartTerm( couponTemplateDO.getFixedStartTerm() );
        appCouponTemplateRespVO.setFixedEndTerm( couponTemplateDO.getFixedEndTerm() );
        appCouponTemplateRespVO.setDiscountType( couponTemplateDO.getDiscountType() );
        appCouponTemplateRespVO.setDiscountPercent( couponTemplateDO.getDiscountPercent() );
        appCouponTemplateRespVO.setDiscountPrice( couponTemplateDO.getDiscountPrice() );
        appCouponTemplateRespVO.setDiscountLimitPrice( couponTemplateDO.getDiscountLimitPrice() );
        appCouponTemplateRespVO.setTakeCount( couponTemplateDO.getTakeCount() );

        return appCouponTemplateRespVO;
    }
}
