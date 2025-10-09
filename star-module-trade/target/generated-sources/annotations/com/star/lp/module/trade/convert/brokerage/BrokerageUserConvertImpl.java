package com.star.lp.module.trade.convert.brokerage;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.member.api.user.dto.MemberUserRespDTO;
import com.star.lp.module.trade.controller.admin.brokerage.vo.user.BrokerageUserRespVO;
import com.star.lp.module.trade.controller.app.brokerage.vo.user.AppBrokerageUserRankByUserCountRespVO;
import com.star.lp.module.trade.dal.dataobject.brokerage.BrokerageUserDO;
import com.star.lp.module.trade.service.brokerage.bo.UserBrokerageSummaryRespBO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-03T12:28:04+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class BrokerageUserConvertImpl implements BrokerageUserConvert {

    @Override
    public BrokerageUserRespVO convert(BrokerageUserDO bean) {
        if ( bean == null ) {
            return null;
        }

        BrokerageUserRespVO brokerageUserRespVO = new BrokerageUserRespVO();

        brokerageUserRespVO.setBindUserId( bean.getBindUserId() );
        brokerageUserRespVO.setBindUserTime( bean.getBindUserTime() );
        brokerageUserRespVO.setBrokerageEnabled( bean.getBrokerageEnabled() );
        brokerageUserRespVO.setBrokerageTime( bean.getBrokerageTime() );
        brokerageUserRespVO.setFrozenPrice( bean.getFrozenPrice() );
        brokerageUserRespVO.setId( bean.getId() );
        brokerageUserRespVO.setCreateTime( bean.getCreateTime() );

        return brokerageUserRespVO;
    }

    @Override
    public List<BrokerageUserRespVO> convertList(List<BrokerageUserDO> list) {
        if ( list == null ) {
            return null;
        }

        List<BrokerageUserRespVO> list1 = new ArrayList<BrokerageUserRespVO>( list.size() );
        for ( BrokerageUserDO brokerageUserDO : list ) {
            list1.add( convert( brokerageUserDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<BrokerageUserRespVO> convertPage(PageResult<BrokerageUserDO> page, Map<Long, MemberUserRespDTO> userMap, Map<Long, Long> brokerageUserCountMap, Map<Long, UserBrokerageSummaryRespBO> userOrderSummaryMap) {
        if ( page == null && userMap == null && brokerageUserCountMap == null && userOrderSummaryMap == null ) {
            return null;
        }

        PageResult<BrokerageUserRespVO> pageResult = new PageResult<BrokerageUserRespVO>();

        if ( page != null ) {
            pageResult.setList( convertList( page.getList() ) );
            pageResult.setTotal( page.getTotal() );
        }

        return pageResult;
    }

    @Override
    public void copyTo(MemberUserRespDTO from, AppBrokerageUserRankByUserCountRespVO to) {
        if ( from == null ) {
            return;
        }

        to.setId( from.getId() );
        to.setNickname( from.getNickname() );
        to.setAvatar( from.getAvatar() );
    }
}
