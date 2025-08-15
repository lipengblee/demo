package cn.iocoder.yudao.module.promotion.convert.combination;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.api.combination.dto.CombinationRecordCreateReqDTO;
import cn.iocoder.yudao.module.promotion.controller.admin.combination.vo.activity.CombinationActivityCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.combination.vo.activity.CombinationActivityPageItemRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.combination.vo.activity.CombinationActivityRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.combination.vo.activity.CombinationActivityUpdateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.combination.vo.product.CombinationProductBaseVO;
import cn.iocoder.yudao.module.promotion.controller.admin.combination.vo.product.CombinationProductRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.combination.vo.recrod.CombinationRecordPageItemRespVO;
import cn.iocoder.yudao.module.promotion.controller.app.combination.vo.activity.AppCombinationActivityDetailRespVO;
import cn.iocoder.yudao.module.promotion.controller.app.combination.vo.record.AppCombinationRecordRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.combination.CombinationActivityDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.combination.CombinationProductDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.combination.CombinationRecordDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-12T15:51:55+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class CombinationActivityConvertImpl implements CombinationActivityConvert {

    @Override
    public CombinationActivityDO convert(CombinationActivityCreateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        CombinationActivityDO.CombinationActivityDOBuilder combinationActivityDO = CombinationActivityDO.builder();

        combinationActivityDO.name( bean.getName() );
        combinationActivityDO.spuId( bean.getSpuId() );
        combinationActivityDO.totalLimitCount( bean.getTotalLimitCount() );
        combinationActivityDO.singleLimitCount( bean.getSingleLimitCount() );
        combinationActivityDO.startTime( bean.getStartTime() );
        combinationActivityDO.endTime( bean.getEndTime() );
        combinationActivityDO.userSize( bean.getUserSize() );
        combinationActivityDO.virtualGroup( bean.getVirtualGroup() );
        combinationActivityDO.limitDuration( bean.getLimitDuration() );

        return combinationActivityDO.build();
    }

    @Override
    public CombinationActivityDO convert(CombinationActivityUpdateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        CombinationActivityDO.CombinationActivityDOBuilder combinationActivityDO = CombinationActivityDO.builder();

        combinationActivityDO.id( bean.getId() );
        combinationActivityDO.name( bean.getName() );
        combinationActivityDO.spuId( bean.getSpuId() );
        combinationActivityDO.totalLimitCount( bean.getTotalLimitCount() );
        combinationActivityDO.singleLimitCount( bean.getSingleLimitCount() );
        combinationActivityDO.startTime( bean.getStartTime() );
        combinationActivityDO.endTime( bean.getEndTime() );
        combinationActivityDO.userSize( bean.getUserSize() );
        combinationActivityDO.virtualGroup( bean.getVirtualGroup() );
        combinationActivityDO.limitDuration( bean.getLimitDuration() );

        return combinationActivityDO.build();
    }

    @Override
    public CombinationActivityRespVO convert(CombinationActivityDO bean) {
        if ( bean == null ) {
            return null;
        }

        CombinationActivityRespVO combinationActivityRespVO = new CombinationActivityRespVO();

        combinationActivityRespVO.setName( bean.getName() );
        combinationActivityRespVO.setSpuId( bean.getSpuId() );
        combinationActivityRespVO.setTotalLimitCount( bean.getTotalLimitCount() );
        combinationActivityRespVO.setSingleLimitCount( bean.getSingleLimitCount() );
        combinationActivityRespVO.setStartTime( bean.getStartTime() );
        combinationActivityRespVO.setEndTime( bean.getEndTime() );
        combinationActivityRespVO.setVirtualGroup( bean.getVirtualGroup() );
        combinationActivityRespVO.setLimitDuration( bean.getLimitDuration() );
        combinationActivityRespVO.setId( bean.getId() );
        combinationActivityRespVO.setCreateTime( bean.getCreateTime() );
        combinationActivityRespVO.setUserSize( bean.getUserSize() );

        return combinationActivityRespVO;
    }

    @Override
    public CombinationProductRespVO convert(CombinationProductDO bean) {
        if ( bean == null ) {
            return null;
        }

        CombinationProductRespVO combinationProductRespVO = new CombinationProductRespVO();

        combinationProductRespVO.setSpuId( bean.getSpuId() );
        combinationProductRespVO.setSkuId( bean.getSkuId() );
        combinationProductRespVO.setCombinationPrice( bean.getCombinationPrice() );
        combinationProductRespVO.setId( bean.getId() );
        combinationProductRespVO.setCreateTime( bean.getCreateTime() );

        return combinationProductRespVO;
    }

    @Override
    public List<CombinationActivityRespVO> convertList(List<CombinationActivityDO> list) {
        if ( list == null ) {
            return null;
        }

        List<CombinationActivityRespVO> list1 = new ArrayList<CombinationActivityRespVO>( list.size() );
        for ( CombinationActivityDO combinationActivityDO : list ) {
            list1.add( convert( combinationActivityDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<CombinationActivityPageItemRespVO> convertPage(PageResult<CombinationActivityDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<CombinationActivityPageItemRespVO> pageResult = new PageResult<CombinationActivityPageItemRespVO>();

        pageResult.setList( combinationActivityDOListToCombinationActivityPageItemRespVOList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public List<CombinationProductRespVO> convertList2(List<CombinationProductDO> productDOs) {
        if ( productDOs == null ) {
            return null;
        }

        List<CombinationProductRespVO> list = new ArrayList<CombinationProductRespVO>( productDOs.size() );
        for ( CombinationProductDO combinationProductDO : productDOs ) {
            list.add( convert( combinationProductDO ) );
        }

        return list;
    }

    @Override
    public CombinationProductDO convert(CombinationActivityDO activity, CombinationProductBaseVO product) {
        if ( activity == null && product == null ) {
            return null;
        }

        CombinationProductDO.CombinationProductDOBuilder combinationProductDO = CombinationProductDO.builder();

        if ( activity != null ) {
            combinationProductDO.activityId( activity.getId() );
            combinationProductDO.spuId( activity.getSpuId() );
            combinationProductDO.activityStartTime( activity.getStartTime() );
            combinationProductDO.activityEndTime( activity.getEndTime() );
        }
        if ( product != null ) {
            combinationProductDO.skuId( product.getSkuId() );
            combinationProductDO.combinationPrice( product.getCombinationPrice() );
        }

        return combinationProductDO.build();
    }

    @Override
    public CombinationRecordDO convert(CombinationRecordCreateReqDTO reqDTO) {
        if ( reqDTO == null ) {
            return null;
        }

        CombinationRecordDO.CombinationRecordDOBuilder combinationRecordDO = CombinationRecordDO.builder();

        combinationRecordDO.activityId( reqDTO.getActivityId() );
        combinationRecordDO.combinationPrice( reqDTO.getCombinationPrice() );
        combinationRecordDO.spuId( reqDTO.getSpuId() );
        combinationRecordDO.skuId( reqDTO.getSkuId() );
        combinationRecordDO.count( reqDTO.getCount() );
        combinationRecordDO.userId( reqDTO.getUserId() );
        combinationRecordDO.headId( reqDTO.getHeadId() );
        combinationRecordDO.orderId( reqDTO.getOrderId() );

        return combinationRecordDO.build();
    }

    @Override
    public AppCombinationActivityDetailRespVO convert2(CombinationActivityDO combinationActivity) {
        if ( combinationActivity == null ) {
            return null;
        }

        AppCombinationActivityDetailRespVO appCombinationActivityDetailRespVO = new AppCombinationActivityDetailRespVO();

        appCombinationActivityDetailRespVO.setId( combinationActivity.getId() );
        appCombinationActivityDetailRespVO.setName( combinationActivity.getName() );
        appCombinationActivityDetailRespVO.setStatus( combinationActivity.getStatus() );
        appCombinationActivityDetailRespVO.setStartTime( combinationActivity.getStartTime() );
        appCombinationActivityDetailRespVO.setEndTime( combinationActivity.getEndTime() );
        appCombinationActivityDetailRespVO.setUserSize( combinationActivity.getUserSize() );
        appCombinationActivityDetailRespVO.setSpuId( combinationActivity.getSpuId() );
        appCombinationActivityDetailRespVO.setTotalLimitCount( combinationActivity.getTotalLimitCount() );
        appCombinationActivityDetailRespVO.setSingleLimitCount( combinationActivity.getSingleLimitCount() );

        return appCombinationActivityDetailRespVO;
    }

    @Override
    public List<AppCombinationActivityDetailRespVO.Product> convertList1(List<CombinationProductDO> products) {
        if ( products == null ) {
            return null;
        }

        List<AppCombinationActivityDetailRespVO.Product> list = new ArrayList<AppCombinationActivityDetailRespVO.Product>( products.size() );
        for ( CombinationProductDO combinationProductDO : products ) {
            list.add( combinationProductDOToProduct( combinationProductDO ) );
        }

        return list;
    }

    @Override
    public List<AppCombinationRecordRespVO> convertList3(List<CombinationRecordDO> records) {
        if ( records == null ) {
            return null;
        }

        List<AppCombinationRecordRespVO> list = new ArrayList<AppCombinationRecordRespVO>( records.size() );
        for ( CombinationRecordDO combinationRecordDO : records ) {
            list.add( convert( combinationRecordDO ) );
        }

        return list;
    }

    @Override
    public AppCombinationRecordRespVO convert(CombinationRecordDO record) {
        if ( record == null ) {
            return null;
        }

        AppCombinationRecordRespVO appCombinationRecordRespVO = new AppCombinationRecordRespVO();

        appCombinationRecordRespVO.setId( record.getId() );
        appCombinationRecordRespVO.setActivityId( record.getActivityId() );
        appCombinationRecordRespVO.setNickname( record.getNickname() );
        appCombinationRecordRespVO.setAvatar( record.getAvatar() );
        appCombinationRecordRespVO.setExpireTime( record.getExpireTime() );
        appCombinationRecordRespVO.setUserSize( record.getUserSize() );
        appCombinationRecordRespVO.setUserCount( record.getUserCount() );
        appCombinationRecordRespVO.setStatus( record.getStatus() );
        appCombinationRecordRespVO.setOrderId( record.getOrderId() );
        appCombinationRecordRespVO.setSpuName( record.getSpuName() );
        appCombinationRecordRespVO.setPicUrl( record.getPicUrl() );
        appCombinationRecordRespVO.setCount( record.getCount() );
        appCombinationRecordRespVO.setCombinationPrice( record.getCombinationPrice() );

        return appCombinationRecordRespVO;
    }

    @Override
    public PageResult<CombinationRecordPageItemRespVO> convert(PageResult<CombinationRecordDO> result) {
        if ( result == null ) {
            return null;
        }

        PageResult<CombinationRecordPageItemRespVO> pageResult = new PageResult<CombinationRecordPageItemRespVO>();

        pageResult.setList( combinationRecordDOListToCombinationRecordPageItemRespVOList( result.getList() ) );
        pageResult.setTotal( result.getTotal() );

        return pageResult;
    }

    @Override
    public CombinationRecordDO convert5(CombinationRecordDO headRecord) {
        if ( headRecord == null ) {
            return null;
        }

        CombinationRecordDO.CombinationRecordDOBuilder combinationRecordDO = CombinationRecordDO.builder();

        combinationRecordDO.activityId( headRecord.getActivityId() );
        combinationRecordDO.combinationPrice( headRecord.getCombinationPrice() );
        combinationRecordDO.spuId( headRecord.getSpuId() );
        combinationRecordDO.spuName( headRecord.getSpuName() );
        combinationRecordDO.picUrl( headRecord.getPicUrl() );
        combinationRecordDO.skuId( headRecord.getSkuId() );
        combinationRecordDO.count( headRecord.getCount() );
        combinationRecordDO.userId( headRecord.getUserId() );
        combinationRecordDO.nickname( headRecord.getNickname() );
        combinationRecordDO.avatar( headRecord.getAvatar() );
        combinationRecordDO.headId( headRecord.getHeadId() );
        combinationRecordDO.status( headRecord.getStatus() );
        combinationRecordDO.orderId( headRecord.getOrderId() );
        combinationRecordDO.userSize( headRecord.getUserSize() );
        combinationRecordDO.userCount( headRecord.getUserCount() );
        combinationRecordDO.virtualGroup( headRecord.getVirtualGroup() );
        combinationRecordDO.expireTime( headRecord.getExpireTime() );
        combinationRecordDO.startTime( headRecord.getStartTime() );
        combinationRecordDO.endTime( headRecord.getEndTime() );

        return combinationRecordDO.build();
    }

    protected CombinationActivityPageItemRespVO combinationActivityDOToCombinationActivityPageItemRespVO(CombinationActivityDO combinationActivityDO) {
        if ( combinationActivityDO == null ) {
            return null;
        }

        CombinationActivityPageItemRespVO combinationActivityPageItemRespVO = new CombinationActivityPageItemRespVO();

        combinationActivityPageItemRespVO.setName( combinationActivityDO.getName() );
        combinationActivityPageItemRespVO.setSpuId( combinationActivityDO.getSpuId() );
        combinationActivityPageItemRespVO.setTotalLimitCount( combinationActivityDO.getTotalLimitCount() );
        combinationActivityPageItemRespVO.setSingleLimitCount( combinationActivityDO.getSingleLimitCount() );
        combinationActivityPageItemRespVO.setStartTime( combinationActivityDO.getStartTime() );
        combinationActivityPageItemRespVO.setEndTime( combinationActivityDO.getEndTime() );
        combinationActivityPageItemRespVO.setUserSize( combinationActivityDO.getUserSize() );
        combinationActivityPageItemRespVO.setVirtualGroup( combinationActivityDO.getVirtualGroup() );
        combinationActivityPageItemRespVO.setLimitDuration( combinationActivityDO.getLimitDuration() );
        combinationActivityPageItemRespVO.setId( combinationActivityDO.getId() );
        combinationActivityPageItemRespVO.setCreateTime( combinationActivityDO.getCreateTime() );
        combinationActivityPageItemRespVO.setStatus( combinationActivityDO.getStatus() );

        return combinationActivityPageItemRespVO;
    }

    protected List<CombinationActivityPageItemRespVO> combinationActivityDOListToCombinationActivityPageItemRespVOList(List<CombinationActivityDO> list) {
        if ( list == null ) {
            return null;
        }

        List<CombinationActivityPageItemRespVO> list1 = new ArrayList<CombinationActivityPageItemRespVO>( list.size() );
        for ( CombinationActivityDO combinationActivityDO : list ) {
            list1.add( combinationActivityDOToCombinationActivityPageItemRespVO( combinationActivityDO ) );
        }

        return list1;
    }

    protected AppCombinationActivityDetailRespVO.Product combinationProductDOToProduct(CombinationProductDO combinationProductDO) {
        if ( combinationProductDO == null ) {
            return null;
        }

        AppCombinationActivityDetailRespVO.Product product = new AppCombinationActivityDetailRespVO.Product();

        product.setSkuId( combinationProductDO.getSkuId() );
        product.setCombinationPrice( combinationProductDO.getCombinationPrice() );

        return product;
    }

    protected CombinationRecordPageItemRespVO combinationRecordDOToCombinationRecordPageItemRespVO(CombinationRecordDO combinationRecordDO) {
        if ( combinationRecordDO == null ) {
            return null;
        }

        CombinationRecordPageItemRespVO combinationRecordPageItemRespVO = new CombinationRecordPageItemRespVO();

        combinationRecordPageItemRespVO.setId( combinationRecordDO.getId() );
        combinationRecordPageItemRespVO.setActivityId( combinationRecordDO.getActivityId() );
        combinationRecordPageItemRespVO.setHeadId( combinationRecordDO.getHeadId() );
        combinationRecordPageItemRespVO.setUserId( combinationRecordDO.getUserId() );
        combinationRecordPageItemRespVO.setNickname( combinationRecordDO.getNickname() );
        combinationRecordPageItemRespVO.setAvatar( combinationRecordDO.getAvatar() );
        combinationRecordPageItemRespVO.setSpuId( combinationRecordDO.getSpuId() );
        combinationRecordPageItemRespVO.setSkuId( combinationRecordDO.getSkuId() );
        combinationRecordPageItemRespVO.setSpuName( combinationRecordDO.getSpuName() );
        combinationRecordPageItemRespVO.setPicUrl( combinationRecordDO.getPicUrl() );
        combinationRecordPageItemRespVO.setExpireTime( combinationRecordDO.getExpireTime() );
        combinationRecordPageItemRespVO.setUserSize( combinationRecordDO.getUserSize() );
        combinationRecordPageItemRespVO.setUserCount( combinationRecordDO.getUserCount() );
        combinationRecordPageItemRespVO.setStatus( combinationRecordDO.getStatus() );
        combinationRecordPageItemRespVO.setVirtualGroup( combinationRecordDO.getVirtualGroup() );
        combinationRecordPageItemRespVO.setStartTime( combinationRecordDO.getStartTime() );
        combinationRecordPageItemRespVO.setEndTime( combinationRecordDO.getEndTime() );

        return combinationRecordPageItemRespVO;
    }

    protected List<CombinationRecordPageItemRespVO> combinationRecordDOListToCombinationRecordPageItemRespVOList(List<CombinationRecordDO> list) {
        if ( list == null ) {
            return null;
        }

        List<CombinationRecordPageItemRespVO> list1 = new ArrayList<CombinationRecordPageItemRespVO>( list.size() );
        for ( CombinationRecordDO combinationRecordDO : list ) {
            list1.add( combinationRecordDOToCombinationRecordPageItemRespVO( combinationRecordDO ) );
        }

        return list1;
    }
}
