package cn.iocoder.yudao.module.promotion.convert.bargain;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.bargain.vo.help.BargainHelpRespVO;
import cn.iocoder.yudao.module.promotion.controller.app.bargain.vo.help.AppBargainHelpRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.bargain.BargainHelpDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-12T15:51:56+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class BargainHelpConvertImpl implements BargainHelpConvert {

    @Override
    public PageResult<BargainHelpRespVO> convertPage(PageResult<BargainHelpDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<BargainHelpRespVO> pageResult = new PageResult<BargainHelpRespVO>();

        pageResult.setList( bargainHelpDOListToBargainHelpRespVOList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public List<AppBargainHelpRespVO> convertList02(List<BargainHelpDO> helps) {
        if ( helps == null ) {
            return null;
        }

        List<AppBargainHelpRespVO> list = new ArrayList<AppBargainHelpRespVO>( helps.size() );
        for ( BargainHelpDO bargainHelpDO : helps ) {
            list.add( bargainHelpDOToAppBargainHelpRespVO( bargainHelpDO ) );
        }

        return list;
    }

    protected BargainHelpRespVO bargainHelpDOToBargainHelpRespVO(BargainHelpDO bargainHelpDO) {
        if ( bargainHelpDO == null ) {
            return null;
        }

        BargainHelpRespVO bargainHelpRespVO = new BargainHelpRespVO();

        bargainHelpRespVO.setUserId( bargainHelpDO.getUserId() );
        bargainHelpRespVO.setActivityId( bargainHelpDO.getActivityId() );
        bargainHelpRespVO.setRecordId( bargainHelpDO.getRecordId() );
        bargainHelpRespVO.setReducePrice( bargainHelpDO.getReducePrice() );
        bargainHelpRespVO.setId( bargainHelpDO.getId() );
        bargainHelpRespVO.setCreateTime( bargainHelpDO.getCreateTime() );

        return bargainHelpRespVO;
    }

    protected List<BargainHelpRespVO> bargainHelpDOListToBargainHelpRespVOList(List<BargainHelpDO> list) {
        if ( list == null ) {
            return null;
        }

        List<BargainHelpRespVO> list1 = new ArrayList<BargainHelpRespVO>( list.size() );
        for ( BargainHelpDO bargainHelpDO : list ) {
            list1.add( bargainHelpDOToBargainHelpRespVO( bargainHelpDO ) );
        }

        return list1;
    }

    protected AppBargainHelpRespVO bargainHelpDOToAppBargainHelpRespVO(BargainHelpDO bargainHelpDO) {
        if ( bargainHelpDO == null ) {
            return null;
        }

        AppBargainHelpRespVO appBargainHelpRespVO = new AppBargainHelpRespVO();

        appBargainHelpRespVO.setUserId( bargainHelpDO.getUserId() );
        appBargainHelpRespVO.setReducePrice( bargainHelpDO.getReducePrice() );
        appBargainHelpRespVO.setCreateTime( bargainHelpDO.getCreateTime() );

        return appBargainHelpRespVO;
    }
}
