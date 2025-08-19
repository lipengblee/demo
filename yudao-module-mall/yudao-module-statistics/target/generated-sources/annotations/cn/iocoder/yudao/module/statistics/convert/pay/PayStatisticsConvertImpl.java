package cn.iocoder.yudao.module.statistics.convert.pay;

import cn.iocoder.yudao.module.statistics.controller.admin.pay.vo.PaySummaryRespVO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-18T17:52:11+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class PayStatisticsConvertImpl implements PayStatisticsConvert {

    @Override
    public PaySummaryRespVO convert(Integer rechargePrice) {
        if ( rechargePrice == null ) {
            return null;
        }

        PaySummaryRespVO paySummaryRespVO = new PaySummaryRespVO();

        paySummaryRespVO.setRechargePrice( rechargePrice );

        return paySummaryRespVO;
    }
}
