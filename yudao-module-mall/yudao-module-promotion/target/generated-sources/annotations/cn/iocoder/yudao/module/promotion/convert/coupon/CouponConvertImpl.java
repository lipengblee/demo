package cn.iocoder.yudao.module.promotion.convert.coupon;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.api.coupon.dto.CouponRespDTO;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.coupon.CouponPageItemRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.coupon.CouponPageReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.coupon.vo.coupon.AppCouponPageReqVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.coupon.CouponDO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-12T15:51:56+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class CouponConvertImpl implements CouponConvert {

    @Override
    public PageResult<CouponPageItemRespVO> convertPage(PageResult<CouponDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<CouponPageItemRespVO> pageResult = new PageResult<CouponPageItemRespVO>();

        pageResult.setList( couponDOListToCouponPageItemRespVOList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public CouponRespDTO convert(CouponDO bean) {
        if ( bean == null ) {
            return null;
        }

        CouponRespDTO couponRespDTO = new CouponRespDTO();

        couponRespDTO.setId( bean.getId() );
        if ( bean.getTemplateId() != null ) {
            couponRespDTO.setTemplateId( bean.getTemplateId().intValue() );
        }
        couponRespDTO.setName( bean.getName() );
        couponRespDTO.setStatus( bean.getStatus() );
        couponRespDTO.setUserId( bean.getUserId() );
        couponRespDTO.setTakeType( bean.getTakeType() );
        couponRespDTO.setUsePrice( bean.getUsePrice() );
        couponRespDTO.setValidStartTime( bean.getValidStartTime() );
        couponRespDTO.setValidEndTime( bean.getValidEndTime() );
        couponRespDTO.setProductScope( bean.getProductScope() );
        List<Long> list = bean.getProductScopeValues();
        if ( list != null ) {
            couponRespDTO.setProductScopeValues( new ArrayList<Long>( list ) );
        }
        couponRespDTO.setDiscountType( bean.getDiscountType() );
        couponRespDTO.setDiscountPercent( bean.getDiscountPercent() );
        couponRespDTO.setDiscountPrice( bean.getDiscountPrice() );
        couponRespDTO.setDiscountLimitPrice( bean.getDiscountLimitPrice() );
        couponRespDTO.setUseOrderId( bean.getUseOrderId() );
        couponRespDTO.setUseTime( bean.getUseTime() );

        return couponRespDTO;
    }

    @Override
    public CouponPageReqVO convert(AppCouponPageReqVO pageReqVO, Collection<Long> userIds) {
        if ( pageReqVO == null && userIds == null ) {
            return null;
        }

        CouponPageReqVO couponPageReqVO = new CouponPageReqVO();

        if ( pageReqVO != null ) {
            couponPageReqVO.setPageNo( pageReqVO.getPageNo() );
            couponPageReqVO.setPageSize( pageReqVO.getPageSize() );
            couponPageReqVO.setStatus( pageReqVO.getStatus() );
        }
        Collection<Long> collection = userIds;
        if ( collection != null ) {
            couponPageReqVO.setUserIds( new ArrayList<Long>( collection ) );
        }

        return couponPageReqVO;
    }

    protected CouponPageItemRespVO couponDOToCouponPageItemRespVO(CouponDO couponDO) {
        if ( couponDO == null ) {
            return null;
        }

        CouponPageItemRespVO couponPageItemRespVO = new CouponPageItemRespVO();

        couponPageItemRespVO.setTemplateId( couponDO.getTemplateId() );
        couponPageItemRespVO.setName( couponDO.getName() );
        couponPageItemRespVO.setStatus( couponDO.getStatus() );
        couponPageItemRespVO.setUserId( couponDO.getUserId() );
        couponPageItemRespVO.setTakeType( couponDO.getTakeType() );
        couponPageItemRespVO.setUsePrice( couponDO.getUsePrice() );
        couponPageItemRespVO.setValidStartTime( couponDO.getValidStartTime() );
        couponPageItemRespVO.setValidEndTime( couponDO.getValidEndTime() );
        couponPageItemRespVO.setProductScope( couponDO.getProductScope() );
        List<Long> list = couponDO.getProductScopeValues();
        if ( list != null ) {
            couponPageItemRespVO.setProductScopeValues( new ArrayList<Long>( list ) );
        }
        couponPageItemRespVO.setDiscountType( couponDO.getDiscountType() );
        couponPageItemRespVO.setDiscountPercent( couponDO.getDiscountPercent() );
        couponPageItemRespVO.setDiscountPrice( couponDO.getDiscountPrice() );
        couponPageItemRespVO.setDiscountLimitPrice( couponDO.getDiscountLimitPrice() );
        couponPageItemRespVO.setUseOrderId( couponDO.getUseOrderId() );
        couponPageItemRespVO.setUseTime( couponDO.getUseTime() );
        couponPageItemRespVO.setId( couponDO.getId() );
        couponPageItemRespVO.setCreateTime( couponDO.getCreateTime() );

        return couponPageItemRespVO;
    }

    protected List<CouponPageItemRespVO> couponDOListToCouponPageItemRespVOList(List<CouponDO> list) {
        if ( list == null ) {
            return null;
        }

        List<CouponPageItemRespVO> list1 = new ArrayList<CouponPageItemRespVO>( list.size() );
        for ( CouponDO couponDO : list ) {
            list1.add( couponDOToCouponPageItemRespVO( couponDO ) );
        }

        return list1;
    }
}
