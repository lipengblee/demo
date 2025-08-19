package cn.iocoder.yudao.module.promotion.convert.bargain;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.bargain.vo.activity.BargainActivityBaseVO;
import cn.iocoder.yudao.module.promotion.controller.admin.bargain.vo.activity.BargainActivityPageItemRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.bargain.vo.activity.BargainActivityRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.bargain.vo.activity.BargainActivityUpdateReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.bargain.vo.activity.AppBargainActivityDetailRespVO;
import cn.iocoder.yudao.module.promotion.controller.app.bargain.vo.activity.AppBargainActivityRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.bargain.BargainActivityDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-18T17:22:15+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class BargainActivityConvertImpl implements BargainActivityConvert {

    @Override
    public BargainActivityDO convert(BargainActivityBaseVO bean) {
        if ( bean == null ) {
            return null;
        }

        BargainActivityDO.BargainActivityDOBuilder bargainActivityDO = BargainActivityDO.builder();

        bargainActivityDO.name( bean.getName() );
        bargainActivityDO.startTime( bean.getStartTime() );
        bargainActivityDO.endTime( bean.getEndTime() );
        bargainActivityDO.spuId( bean.getSpuId() );
        bargainActivityDO.skuId( bean.getSkuId() );
        bargainActivityDO.bargainFirstPrice( bean.getBargainFirstPrice() );
        bargainActivityDO.bargainMinPrice( bean.getBargainMinPrice() );
        bargainActivityDO.stock( bean.getStock() );
        bargainActivityDO.helpMaxCount( bean.getHelpMaxCount() );
        bargainActivityDO.bargainCount( bean.getBargainCount() );
        bargainActivityDO.totalLimitCount( bean.getTotalLimitCount() );
        bargainActivityDO.randomMinPrice( bean.getRandomMinPrice() );
        bargainActivityDO.randomMaxPrice( bean.getRandomMaxPrice() );

        return bargainActivityDO.build();
    }

    @Override
    public BargainActivityDO convert(BargainActivityUpdateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        BargainActivityDO.BargainActivityDOBuilder bargainActivityDO = BargainActivityDO.builder();

        bargainActivityDO.id( bean.getId() );
        bargainActivityDO.name( bean.getName() );
        bargainActivityDO.startTime( bean.getStartTime() );
        bargainActivityDO.endTime( bean.getEndTime() );
        bargainActivityDO.spuId( bean.getSpuId() );
        bargainActivityDO.skuId( bean.getSkuId() );
        bargainActivityDO.bargainFirstPrice( bean.getBargainFirstPrice() );
        bargainActivityDO.bargainMinPrice( bean.getBargainMinPrice() );
        bargainActivityDO.stock( bean.getStock() );
        bargainActivityDO.helpMaxCount( bean.getHelpMaxCount() );
        bargainActivityDO.bargainCount( bean.getBargainCount() );
        bargainActivityDO.totalLimitCount( bean.getTotalLimitCount() );
        bargainActivityDO.randomMinPrice( bean.getRandomMinPrice() );
        bargainActivityDO.randomMaxPrice( bean.getRandomMaxPrice() );

        return bargainActivityDO.build();
    }

    @Override
    public BargainActivityRespVO convert(BargainActivityDO bean) {
        if ( bean == null ) {
            return null;
        }

        BargainActivityRespVO bargainActivityRespVO = new BargainActivityRespVO();

        bargainActivityRespVO.setName( bean.getName() );
        bargainActivityRespVO.setSpuId( bean.getSpuId() );
        bargainActivityRespVO.setSkuId( bean.getSkuId() );
        bargainActivityRespVO.setBargainFirstPrice( bean.getBargainFirstPrice() );
        bargainActivityRespVO.setBargainMinPrice( bean.getBargainMinPrice() );
        bargainActivityRespVO.setStock( bean.getStock() );
        bargainActivityRespVO.setTotalLimitCount( bean.getTotalLimitCount() );
        bargainActivityRespVO.setStartTime( bean.getStartTime() );
        bargainActivityRespVO.setEndTime( bean.getEndTime() );
        bargainActivityRespVO.setHelpMaxCount( bean.getHelpMaxCount() );
        bargainActivityRespVO.setBargainCount( bean.getBargainCount() );
        bargainActivityRespVO.setRandomMinPrice( bean.getRandomMinPrice() );
        bargainActivityRespVO.setRandomMaxPrice( bean.getRandomMaxPrice() );
        bargainActivityRespVO.setId( bean.getId() );
        bargainActivityRespVO.setStatus( bean.getStatus() );
        bargainActivityRespVO.setCreateTime( bean.getCreateTime() );

        return bargainActivityRespVO;
    }

    @Override
    public List<BargainActivityRespVO> convertList(List<BargainActivityDO> list) {
        if ( list == null ) {
            return null;
        }

        List<BargainActivityRespVO> list1 = new ArrayList<BargainActivityRespVO>( list.size() );
        for ( BargainActivityDO bargainActivityDO : list ) {
            list1.add( convert( bargainActivityDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<BargainActivityPageItemRespVO> convertPage(PageResult<BargainActivityDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<BargainActivityPageItemRespVO> pageResult = new PageResult<BargainActivityPageItemRespVO>();

        pageResult.setList( bargainActivityDOListToBargainActivityPageItemRespVOList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public AppBargainActivityDetailRespVO convert1(BargainActivityDO bean) {
        if ( bean == null ) {
            return null;
        }

        AppBargainActivityDetailRespVO appBargainActivityDetailRespVO = new AppBargainActivityDetailRespVO();

        appBargainActivityDetailRespVO.setId( bean.getId() );
        appBargainActivityDetailRespVO.setName( bean.getName() );
        appBargainActivityDetailRespVO.setStartTime( bean.getStartTime() );
        appBargainActivityDetailRespVO.setEndTime( bean.getEndTime() );
        appBargainActivityDetailRespVO.setSpuId( bean.getSpuId() );
        appBargainActivityDetailRespVO.setSkuId( bean.getSkuId() );
        appBargainActivityDetailRespVO.setStock( bean.getStock() );
        appBargainActivityDetailRespVO.setBargainFirstPrice( bean.getBargainFirstPrice() );
        appBargainActivityDetailRespVO.setBargainMinPrice( bean.getBargainMinPrice() );

        return appBargainActivityDetailRespVO;
    }

    @Override
    public PageResult<AppBargainActivityRespVO> convertAppPage(PageResult<BargainActivityDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<AppBargainActivityRespVO> pageResult = new PageResult<AppBargainActivityRespVO>();

        pageResult.setList( convertAppList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public List<AppBargainActivityRespVO> convertAppList(List<BargainActivityDO> list) {
        if ( list == null ) {
            return null;
        }

        List<AppBargainActivityRespVO> list1 = new ArrayList<AppBargainActivityRespVO>( list.size() );
        for ( BargainActivityDO bargainActivityDO : list ) {
            list1.add( bargainActivityDOToAppBargainActivityRespVO( bargainActivityDO ) );
        }

        return list1;
    }

    protected BargainActivityPageItemRespVO bargainActivityDOToBargainActivityPageItemRespVO(BargainActivityDO bargainActivityDO) {
        if ( bargainActivityDO == null ) {
            return null;
        }

        BargainActivityPageItemRespVO bargainActivityPageItemRespVO = new BargainActivityPageItemRespVO();

        bargainActivityPageItemRespVO.setName( bargainActivityDO.getName() );
        bargainActivityPageItemRespVO.setSpuId( bargainActivityDO.getSpuId() );
        bargainActivityPageItemRespVO.setSkuId( bargainActivityDO.getSkuId() );
        bargainActivityPageItemRespVO.setBargainFirstPrice( bargainActivityDO.getBargainFirstPrice() );
        bargainActivityPageItemRespVO.setBargainMinPrice( bargainActivityDO.getBargainMinPrice() );
        bargainActivityPageItemRespVO.setStock( bargainActivityDO.getStock() );
        bargainActivityPageItemRespVO.setTotalLimitCount( bargainActivityDO.getTotalLimitCount() );
        bargainActivityPageItemRespVO.setStartTime( bargainActivityDO.getStartTime() );
        bargainActivityPageItemRespVO.setEndTime( bargainActivityDO.getEndTime() );
        bargainActivityPageItemRespVO.setHelpMaxCount( bargainActivityDO.getHelpMaxCount() );
        bargainActivityPageItemRespVO.setBargainCount( bargainActivityDO.getBargainCount() );
        bargainActivityPageItemRespVO.setRandomMinPrice( bargainActivityDO.getRandomMinPrice() );
        bargainActivityPageItemRespVO.setRandomMaxPrice( bargainActivityDO.getRandomMaxPrice() );
        bargainActivityPageItemRespVO.setId( bargainActivityDO.getId() );
        bargainActivityPageItemRespVO.setStatus( bargainActivityDO.getStatus() );
        bargainActivityPageItemRespVO.setTotalStock( bargainActivityDO.getTotalStock() );
        bargainActivityPageItemRespVO.setCreateTime( bargainActivityDO.getCreateTime() );

        return bargainActivityPageItemRespVO;
    }

    protected List<BargainActivityPageItemRespVO> bargainActivityDOListToBargainActivityPageItemRespVOList(List<BargainActivityDO> list) {
        if ( list == null ) {
            return null;
        }

        List<BargainActivityPageItemRespVO> list1 = new ArrayList<BargainActivityPageItemRespVO>( list.size() );
        for ( BargainActivityDO bargainActivityDO : list ) {
            list1.add( bargainActivityDOToBargainActivityPageItemRespVO( bargainActivityDO ) );
        }

        return list1;
    }

    protected AppBargainActivityRespVO bargainActivityDOToAppBargainActivityRespVO(BargainActivityDO bargainActivityDO) {
        if ( bargainActivityDO == null ) {
            return null;
        }

        AppBargainActivityRespVO appBargainActivityRespVO = new AppBargainActivityRespVO();

        appBargainActivityRespVO.setId( bargainActivityDO.getId() );
        appBargainActivityRespVO.setName( bargainActivityDO.getName() );
        appBargainActivityRespVO.setStartTime( bargainActivityDO.getStartTime() );
        appBargainActivityRespVO.setEndTime( bargainActivityDO.getEndTime() );
        appBargainActivityRespVO.setSpuId( bargainActivityDO.getSpuId() );
        appBargainActivityRespVO.setSkuId( bargainActivityDO.getSkuId() );
        appBargainActivityRespVO.setStock( bargainActivityDO.getStock() );
        appBargainActivityRespVO.setBargainMinPrice( bargainActivityDO.getBargainMinPrice() );

        return appBargainActivityRespVO;
    }
}
