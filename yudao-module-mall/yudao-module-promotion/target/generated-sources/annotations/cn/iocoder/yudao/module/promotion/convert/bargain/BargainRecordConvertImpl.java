package cn.iocoder.yudao.module.promotion.convert.bargain;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.bargain.vo.recrod.BargainRecordPageItemRespVO;
import cn.iocoder.yudao.module.promotion.controller.app.bargain.vo.record.AppBargainRecordDetailRespVO;
import cn.iocoder.yudao.module.promotion.controller.app.bargain.vo.record.AppBargainRecordRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.bargain.BargainRecordDO;
import cn.iocoder.yudao.module.trade.api.order.dto.TradeOrderRespDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-12T15:51:56+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class BargainRecordConvertImpl implements BargainRecordConvert {

    @Override
    public PageResult<BargainRecordPageItemRespVO> convertPage(PageResult<BargainRecordDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<BargainRecordPageItemRespVO> pageResult = new PageResult<BargainRecordPageItemRespVO>();

        pageResult.setList( bargainRecordDOListToBargainRecordPageItemRespVOList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public PageResult<AppBargainRecordRespVO> convertPage02(PageResult<BargainRecordDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<AppBargainRecordRespVO> pageResult = new PageResult<AppBargainRecordRespVO>();

        pageResult.setList( bargainRecordDOListToAppBargainRecordRespVOList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public AppBargainRecordDetailRespVO convert02(BargainRecordDO record, Integer helpAction, TradeOrderRespDTO order) {
        if ( record == null && helpAction == null && order == null ) {
            return null;
        }

        AppBargainRecordDetailRespVO appBargainRecordDetailRespVO = new AppBargainRecordDetailRespVO();

        if ( record != null ) {
            appBargainRecordDetailRespVO.setId( record.getId() );
            appBargainRecordDetailRespVO.setUserId( record.getUserId() );
            appBargainRecordDetailRespVO.setStatus( record.getStatus() );
            appBargainRecordDetailRespVO.setSpuId( record.getSpuId() );
            appBargainRecordDetailRespVO.setSkuId( record.getSkuId() );
            appBargainRecordDetailRespVO.setActivityId( record.getActivityId() );
            appBargainRecordDetailRespVO.setBargainFirstPrice( record.getBargainFirstPrice() );
            appBargainRecordDetailRespVO.setBargainPrice( record.getBargainPrice() );
            appBargainRecordDetailRespVO.setOrderId( record.getOrderId() );
        }
        if ( order != null ) {
            appBargainRecordDetailRespVO.setPayStatus( order.getPayStatus() );
            appBargainRecordDetailRespVO.setPayOrderId( order.getPayOrderId() );
        }
        appBargainRecordDetailRespVO.setHelpAction( helpAction );

        return appBargainRecordDetailRespVO;
    }

    protected BargainRecordPageItemRespVO bargainRecordDOToBargainRecordPageItemRespVO(BargainRecordDO bargainRecordDO) {
        if ( bargainRecordDO == null ) {
            return null;
        }

        BargainRecordPageItemRespVO bargainRecordPageItemRespVO = new BargainRecordPageItemRespVO();

        bargainRecordPageItemRespVO.setActivityId( bargainRecordDO.getActivityId() );
        bargainRecordPageItemRespVO.setUserId( bargainRecordDO.getUserId() );
        bargainRecordPageItemRespVO.setSpuId( bargainRecordDO.getSpuId() );
        bargainRecordPageItemRespVO.setSkuId( bargainRecordDO.getSkuId() );
        bargainRecordPageItemRespVO.setBargainFirstPrice( bargainRecordDO.getBargainFirstPrice() );
        bargainRecordPageItemRespVO.setBargainPrice( bargainRecordDO.getBargainPrice() );
        bargainRecordPageItemRespVO.setStatus( bargainRecordDO.getStatus() );
        bargainRecordPageItemRespVO.setOrderId( bargainRecordDO.getOrderId() );
        bargainRecordPageItemRespVO.setEndTime( bargainRecordDO.getEndTime() );
        bargainRecordPageItemRespVO.setId( bargainRecordDO.getId() );
        bargainRecordPageItemRespVO.setCreateTime( bargainRecordDO.getCreateTime() );

        return bargainRecordPageItemRespVO;
    }

    protected List<BargainRecordPageItemRespVO> bargainRecordDOListToBargainRecordPageItemRespVOList(List<BargainRecordDO> list) {
        if ( list == null ) {
            return null;
        }

        List<BargainRecordPageItemRespVO> list1 = new ArrayList<BargainRecordPageItemRespVO>( list.size() );
        for ( BargainRecordDO bargainRecordDO : list ) {
            list1.add( bargainRecordDOToBargainRecordPageItemRespVO( bargainRecordDO ) );
        }

        return list1;
    }

    protected AppBargainRecordRespVO bargainRecordDOToAppBargainRecordRespVO(BargainRecordDO bargainRecordDO) {
        if ( bargainRecordDO == null ) {
            return null;
        }

        AppBargainRecordRespVO appBargainRecordRespVO = new AppBargainRecordRespVO();

        appBargainRecordRespVO.setId( bargainRecordDO.getId() );
        appBargainRecordRespVO.setSpuId( bargainRecordDO.getSpuId() );
        appBargainRecordRespVO.setSkuId( bargainRecordDO.getSkuId() );
        appBargainRecordRespVO.setActivityId( bargainRecordDO.getActivityId() );
        appBargainRecordRespVO.setStatus( bargainRecordDO.getStatus() );
        appBargainRecordRespVO.setBargainPrice( bargainRecordDO.getBargainPrice() );
        appBargainRecordRespVO.setEndTime( bargainRecordDO.getEndTime() );
        appBargainRecordRespVO.setOrderId( bargainRecordDO.getOrderId() );

        return appBargainRecordRespVO;
    }

    protected List<AppBargainRecordRespVO> bargainRecordDOListToAppBargainRecordRespVOList(List<BargainRecordDO> list) {
        if ( list == null ) {
            return null;
        }

        List<AppBargainRecordRespVO> list1 = new ArrayList<AppBargainRecordRespVO>( list.size() );
        for ( BargainRecordDO bargainRecordDO : list ) {
            list1.add( bargainRecordDOToAppBargainRecordRespVO( bargainRecordDO ) );
        }

        return list1;
    }
}
