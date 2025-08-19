package cn.iocoder.yudao.module.statistics.convert.member;

import cn.iocoder.yudao.module.statistics.controller.admin.common.vo.DataComparisonRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.member.vo.MemberAnalyseDataRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.member.vo.MemberAnalyseRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.member.vo.MemberSummaryRespVO;
import cn.iocoder.yudao.module.statistics.service.pay.bo.RechargeSummaryRespBO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T17:11:23+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class MemberStatisticsConvertImpl implements MemberStatisticsConvert {

    @Override
    public MemberSummaryRespVO convert(RechargeSummaryRespBO rechargeSummary, Integer expensePrice, Integer userCount) {
        if ( rechargeSummary == null && expensePrice == null && userCount == null ) {
            return null;
        }

        MemberSummaryRespVO memberSummaryRespVO = new MemberSummaryRespVO();

        if ( rechargeSummary != null ) {
            memberSummaryRespVO.setRechargeUserCount( rechargeSummary.getRechargeUserCount() );
            memberSummaryRespVO.setRechargePrice( rechargeSummary.getRechargePrice() );
        }
        memberSummaryRespVO.setExpensePrice( expensePrice );
        memberSummaryRespVO.setUserCount( userCount );

        return memberSummaryRespVO;
    }

    @Override
    public MemberAnalyseRespVO convert(Integer visitUserCount, Integer orderUserCount, Integer payUserCount, int atv, DataComparisonRespVO<MemberAnalyseDataRespVO> comparison) {
        if ( visitUserCount == null && orderUserCount == null && payUserCount == null && comparison == null ) {
            return null;
        }

        MemberAnalyseRespVO memberAnalyseRespVO = new MemberAnalyseRespVO();

        memberAnalyseRespVO.setVisitUserCount( visitUserCount );
        memberAnalyseRespVO.setOrderUserCount( orderUserCount );
        memberAnalyseRespVO.setPayUserCount( payUserCount );
        memberAnalyseRespVO.setAtv( atv );
        memberAnalyseRespVO.setComparison( comparison );

        return memberAnalyseRespVO;
    }
}
