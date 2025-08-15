package cn.iocoder.yudao.module.promotion.convert.seckill;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.api.seckill.dto.SeckillValidateJoinRespDTO;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.activity.SeckillActivityCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.activity.SeckillActivityDetailRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.activity.SeckillActivityRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.activity.SeckillActivityUpdateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.product.SeckillProductBaseVO;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.product.SeckillProductRespVO;
import cn.iocoder.yudao.module.promotion.controller.app.seckill.vo.activity.AppSeckillActivityDetailRespVO;
import cn.iocoder.yudao.module.promotion.controller.app.seckill.vo.activity.AppSeckillActivityRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.seckill.SeckillActivityDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.seckill.SeckillProductDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-12T15:51:56+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class SeckillActivityConvertImpl implements SeckillActivityConvert {

    @Override
    public SeckillActivityDO convert(SeckillActivityCreateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        SeckillActivityDO seckillActivityDO = new SeckillActivityDO();

        seckillActivityDO.setSpuId( bean.getSpuId() );
        seckillActivityDO.setName( bean.getName() );
        seckillActivityDO.setRemark( bean.getRemark() );
        seckillActivityDO.setStartTime( bean.getStartTime() );
        seckillActivityDO.setEndTime( bean.getEndTime() );
        seckillActivityDO.setSort( bean.getSort() );
        List<Long> list = bean.getConfigIds();
        if ( list != null ) {
            seckillActivityDO.setConfigIds( new ArrayList<Long>( list ) );
        }
        seckillActivityDO.setTotalLimitCount( bean.getTotalLimitCount() );
        seckillActivityDO.setSingleLimitCount( bean.getSingleLimitCount() );

        return seckillActivityDO;
    }

    @Override
    public SeckillActivityDO convert(SeckillActivityUpdateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        SeckillActivityDO seckillActivityDO = new SeckillActivityDO();

        seckillActivityDO.setId( bean.getId() );
        seckillActivityDO.setSpuId( bean.getSpuId() );
        seckillActivityDO.setName( bean.getName() );
        seckillActivityDO.setRemark( bean.getRemark() );
        seckillActivityDO.setStartTime( bean.getStartTime() );
        seckillActivityDO.setEndTime( bean.getEndTime() );
        seckillActivityDO.setSort( bean.getSort() );
        List<Long> list = bean.getConfigIds();
        if ( list != null ) {
            seckillActivityDO.setConfigIds( new ArrayList<Long>( list ) );
        }
        seckillActivityDO.setTotalLimitCount( bean.getTotalLimitCount() );
        seckillActivityDO.setSingleLimitCount( bean.getSingleLimitCount() );

        return seckillActivityDO;
    }

    @Override
    public SeckillActivityRespVO convert(SeckillActivityDO bean) {
        if ( bean == null ) {
            return null;
        }

        SeckillActivityRespVO seckillActivityRespVO = new SeckillActivityRespVO();

        seckillActivityRespVO.setSpuId( bean.getSpuId() );
        seckillActivityRespVO.setName( bean.getName() );
        seckillActivityRespVO.setRemark( bean.getRemark() );
        seckillActivityRespVO.setStartTime( bean.getStartTime() );
        seckillActivityRespVO.setEndTime( bean.getEndTime() );
        seckillActivityRespVO.setSort( bean.getSort() );
        List<Long> list = bean.getConfigIds();
        if ( list != null ) {
            seckillActivityRespVO.setConfigIds( new ArrayList<Long>( list ) );
        }
        seckillActivityRespVO.setTotalLimitCount( bean.getTotalLimitCount() );
        seckillActivityRespVO.setSingleLimitCount( bean.getSingleLimitCount() );
        seckillActivityRespVO.setId( bean.getId() );
        seckillActivityRespVO.setStatus( bean.getStatus() );
        seckillActivityRespVO.setStock( bean.getStock() );
        seckillActivityRespVO.setTotalStock( bean.getTotalStock() );
        seckillActivityRespVO.setCreateTime( bean.getCreateTime() );

        return seckillActivityRespVO;
    }

    @Override
    public List<SeckillActivityRespVO> convertList(List<SeckillActivityDO> list) {
        if ( list == null ) {
            return null;
        }

        List<SeckillActivityRespVO> list1 = new ArrayList<SeckillActivityRespVO>( list.size() );
        for ( SeckillActivityDO seckillActivityDO : list ) {
            list1.add( convert( seckillActivityDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<SeckillActivityRespVO> convertPage(PageResult<SeckillActivityDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<SeckillActivityRespVO> pageResult = new PageResult<SeckillActivityRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public SeckillActivityDetailRespVO convert1(SeckillActivityDO activity) {
        if ( activity == null ) {
            return null;
        }

        SeckillActivityDetailRespVO seckillActivityDetailRespVO = new SeckillActivityDetailRespVO();

        seckillActivityDetailRespVO.setSpuId( activity.getSpuId() );
        seckillActivityDetailRespVO.setName( activity.getName() );
        seckillActivityDetailRespVO.setRemark( activity.getRemark() );
        seckillActivityDetailRespVO.setStartTime( activity.getStartTime() );
        seckillActivityDetailRespVO.setEndTime( activity.getEndTime() );
        seckillActivityDetailRespVO.setSort( activity.getSort() );
        List<Long> list = activity.getConfigIds();
        if ( list != null ) {
            seckillActivityDetailRespVO.setConfigIds( new ArrayList<Long>( list ) );
        }
        seckillActivityDetailRespVO.setTotalLimitCount( activity.getTotalLimitCount() );
        seckillActivityDetailRespVO.setSingleLimitCount( activity.getSingleLimitCount() );
        seckillActivityDetailRespVO.setId( activity.getId() );

        return seckillActivityDetailRespVO;
    }

    @Override
    public SeckillProductDO convert(SeckillActivityDO activity, SeckillProductBaseVO product) {
        if ( activity == null && product == null ) {
            return null;
        }

        SeckillProductDO.SeckillProductDOBuilder seckillProductDO = SeckillProductDO.builder();

        if ( activity != null ) {
            seckillProductDO.activityId( activity.getId() );
            List<Long> list = activity.getConfigIds();
            if ( list != null ) {
                seckillProductDO.configIds( new ArrayList<Long>( list ) );
            }
            seckillProductDO.spuId( activity.getSpuId() );
            seckillProductDO.activityStartTime( activity.getStartTime() );
            seckillProductDO.activityEndTime( activity.getEndTime() );
        }
        if ( product != null ) {
            seckillProductDO.skuId( product.getSkuId() );
            seckillProductDO.seckillPrice( product.getSeckillPrice() );
            seckillProductDO.stock( product.getStock() );
        }

        return seckillProductDO.build();
    }

    @Override
    public List<SeckillProductRespVO> convertList2(List<SeckillProductDO> list) {
        if ( list == null ) {
            return null;
        }

        List<SeckillProductRespVO> list1 = new ArrayList<SeckillProductRespVO>( list.size() );
        for ( SeckillProductDO seckillProductDO : list ) {
            list1.add( seckillProductDOToSeckillProductRespVO( seckillProductDO ) );
        }

        return list1;
    }

    @Override
    public List<AppSeckillActivityRespVO> convertList3(List<SeckillActivityDO> activityList) {
        if ( activityList == null ) {
            return null;
        }

        List<AppSeckillActivityRespVO> list = new ArrayList<AppSeckillActivityRespVO>( activityList.size() );
        for ( SeckillActivityDO seckillActivityDO : activityList ) {
            list.add( seckillActivityDOToAppSeckillActivityRespVO( seckillActivityDO ) );
        }

        return list;
    }

    @Override
    public PageResult<AppSeckillActivityRespVO> convertPage1(PageResult<SeckillActivityDO> pageResult) {
        if ( pageResult == null ) {
            return null;
        }

        PageResult<AppSeckillActivityRespVO> pageResult1 = new PageResult<AppSeckillActivityRespVO>();

        pageResult1.setList( convertList3( pageResult.getList() ) );
        pageResult1.setTotal( pageResult.getTotal() );

        return pageResult1;
    }

    @Override
    public AppSeckillActivityDetailRespVO convert2(SeckillActivityDO seckillActivity) {
        if ( seckillActivity == null ) {
            return null;
        }

        AppSeckillActivityDetailRespVO appSeckillActivityDetailRespVO = new AppSeckillActivityDetailRespVO();

        appSeckillActivityDetailRespVO.setId( seckillActivity.getId() );
        appSeckillActivityDetailRespVO.setName( seckillActivity.getName() );
        appSeckillActivityDetailRespVO.setStatus( seckillActivity.getStatus() );
        appSeckillActivityDetailRespVO.setStartTime( seckillActivity.getStartTime() );
        appSeckillActivityDetailRespVO.setEndTime( seckillActivity.getEndTime() );
        appSeckillActivityDetailRespVO.setSpuId( seckillActivity.getSpuId() );
        appSeckillActivityDetailRespVO.setTotalLimitCount( seckillActivity.getTotalLimitCount() );
        appSeckillActivityDetailRespVO.setSingleLimitCount( seckillActivity.getSingleLimitCount() );
        appSeckillActivityDetailRespVO.setStock( seckillActivity.getStock() );
        appSeckillActivityDetailRespVO.setTotalStock( seckillActivity.getTotalStock() );

        return appSeckillActivityDetailRespVO;
    }

    @Override
    public List<AppSeckillActivityDetailRespVO.Product> convertList1(List<SeckillProductDO> products) {
        if ( products == null ) {
            return null;
        }

        List<AppSeckillActivityDetailRespVO.Product> list = new ArrayList<AppSeckillActivityDetailRespVO.Product>( products.size() );
        for ( SeckillProductDO seckillProductDO : products ) {
            list.add( seckillProductDOToProduct( seckillProductDO ) );
        }

        return list;
    }

    @Override
    public SeckillValidateJoinRespDTO convert02(SeckillActivityDO activity, SeckillProductDO product) {
        if ( activity == null && product == null ) {
            return null;
        }

        SeckillValidateJoinRespDTO seckillValidateJoinRespDTO = new SeckillValidateJoinRespDTO();

        if ( activity != null ) {
            seckillValidateJoinRespDTO.setName( activity.getName() );
            seckillValidateJoinRespDTO.setTotalLimitCount( activity.getTotalLimitCount() );
        }
        if ( product != null ) {
            seckillValidateJoinRespDTO.setSeckillPrice( product.getSeckillPrice() );
        }

        return seckillValidateJoinRespDTO;
    }

    protected SeckillProductRespVO seckillProductDOToSeckillProductRespVO(SeckillProductDO seckillProductDO) {
        if ( seckillProductDO == null ) {
            return null;
        }

        SeckillProductRespVO seckillProductRespVO = new SeckillProductRespVO();

        seckillProductRespVO.setSkuId( seckillProductDO.getSkuId() );
        seckillProductRespVO.setSeckillPrice( seckillProductDO.getSeckillPrice() );
        seckillProductRespVO.setStock( seckillProductDO.getStock() );
        seckillProductRespVO.setId( seckillProductDO.getId() );
        seckillProductRespVO.setCreateTime( seckillProductDO.getCreateTime() );

        return seckillProductRespVO;
    }

    protected AppSeckillActivityRespVO seckillActivityDOToAppSeckillActivityRespVO(SeckillActivityDO seckillActivityDO) {
        if ( seckillActivityDO == null ) {
            return null;
        }

        AppSeckillActivityRespVO appSeckillActivityRespVO = new AppSeckillActivityRespVO();

        appSeckillActivityRespVO.setId( seckillActivityDO.getId() );
        appSeckillActivityRespVO.setName( seckillActivityDO.getName() );
        appSeckillActivityRespVO.setSpuId( seckillActivityDO.getSpuId() );
        appSeckillActivityRespVO.setStatus( seckillActivityDO.getStatus() );
        appSeckillActivityRespVO.setStock( seckillActivityDO.getStock() );
        appSeckillActivityRespVO.setTotalStock( seckillActivityDO.getTotalStock() );

        return appSeckillActivityRespVO;
    }

    protected AppSeckillActivityDetailRespVO.Product seckillProductDOToProduct(SeckillProductDO seckillProductDO) {
        if ( seckillProductDO == null ) {
            return null;
        }

        AppSeckillActivityDetailRespVO.Product product = new AppSeckillActivityDetailRespVO.Product();

        product.setSkuId( seckillProductDO.getSkuId() );
        product.setSeckillPrice( seckillProductDO.getSeckillPrice() );
        product.setStock( seckillProductDO.getStock() );

        return product;
    }
}
