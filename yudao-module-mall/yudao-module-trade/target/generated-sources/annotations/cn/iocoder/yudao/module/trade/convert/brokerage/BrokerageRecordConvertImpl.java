package cn.iocoder.yudao.module.trade.convert.brokerage;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.trade.controller.admin.brokerage.vo.record.BrokerageRecordRespVO;
import cn.iocoder.yudao.module.trade.controller.app.brokerage.vo.user.AppBrokerageUserRankByPriceRespVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.brokerage.BrokerageRecordDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-18T17:52:03+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class BrokerageRecordConvertImpl implements BrokerageRecordConvert {

    @Override
    public BrokerageRecordRespVO convert(BrokerageRecordDO bean) {
        if ( bean == null ) {
            return null;
        }

        BrokerageRecordRespVO brokerageRecordRespVO = new BrokerageRecordRespVO();

        brokerageRecordRespVO.setUserId( bean.getUserId() );
        brokerageRecordRespVO.setBizId( bean.getBizId() );
        brokerageRecordRespVO.setBizType( bean.getBizType() );
        brokerageRecordRespVO.setTitle( bean.getTitle() );
        brokerageRecordRespVO.setPrice( bean.getPrice() );
        brokerageRecordRespVO.setTotalPrice( bean.getTotalPrice() );
        brokerageRecordRespVO.setDescription( bean.getDescription() );
        brokerageRecordRespVO.setStatus( bean.getStatus() );
        brokerageRecordRespVO.setFrozenDays( bean.getFrozenDays() );
        brokerageRecordRespVO.setUnfreezeTime( bean.getUnfreezeTime() );
        brokerageRecordRespVO.setSourceUserLevel( bean.getSourceUserLevel() );
        brokerageRecordRespVO.setSourceUserId( bean.getSourceUserId() );
        if ( bean.getId() != null ) {
            brokerageRecordRespVO.setId( bean.getId().longValue() );
        }
        brokerageRecordRespVO.setCreateTime( bean.getCreateTime() );

        return brokerageRecordRespVO;
    }

    @Override
    public List<BrokerageRecordRespVO> convertList(List<BrokerageRecordDO> list) {
        if ( list == null ) {
            return null;
        }

        List<BrokerageRecordRespVO> list1 = new ArrayList<BrokerageRecordRespVO>( list.size() );
        for ( BrokerageRecordDO brokerageRecordDO : list ) {
            list1.add( convert( brokerageRecordDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<BrokerageRecordRespVO> convertPage(PageResult<BrokerageRecordDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<BrokerageRecordRespVO> pageResult = new PageResult<BrokerageRecordRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public void copyTo(MemberUserRespDTO from, AppBrokerageUserRankByPriceRespVO to) {
        if ( from == null ) {
            return;
        }

        to.setId( from.getId() );
        to.setNickname( from.getNickname() );
        to.setAvatar( from.getAvatar() );
    }
}
