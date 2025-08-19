package cn.iocoder.yudao.module.trade.convert.brokerage;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.trade.controller.admin.brokerage.vo.withdraw.BrokerageWithdrawRespVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.brokerage.BrokerageWithdrawDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-18T17:52:03+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class BrokerageWithdrawConvertImpl implements BrokerageWithdrawConvert {

    @Override
    public BrokerageWithdrawRespVO convert(BrokerageWithdrawDO bean) {
        if ( bean == null ) {
            return null;
        }

        BrokerageWithdrawRespVO brokerageWithdrawRespVO = new BrokerageWithdrawRespVO();

        brokerageWithdrawRespVO.setId( bean.getId() );
        brokerageWithdrawRespVO.setUserId( bean.getUserId() );
        brokerageWithdrawRespVO.setPrice( bean.getPrice() );
        brokerageWithdrawRespVO.setFeePrice( bean.getFeePrice() );
        brokerageWithdrawRespVO.setTotalPrice( bean.getTotalPrice() );
        brokerageWithdrawRespVO.setType( bean.getType() );
        brokerageWithdrawRespVO.setUserName( bean.getUserName() );
        brokerageWithdrawRespVO.setUserAccount( bean.getUserAccount() );
        brokerageWithdrawRespVO.setBankName( bean.getBankName() );
        brokerageWithdrawRespVO.setBankAddress( bean.getBankAddress() );
        brokerageWithdrawRespVO.setQrCodeUrl( bean.getQrCodeUrl() );
        brokerageWithdrawRespVO.setStatus( bean.getStatus() );
        brokerageWithdrawRespVO.setAuditReason( bean.getAuditReason() );
        brokerageWithdrawRespVO.setAuditTime( bean.getAuditTime() );
        brokerageWithdrawRespVO.setRemark( bean.getRemark() );
        brokerageWithdrawRespVO.setPayTransferId( bean.getPayTransferId() );
        brokerageWithdrawRespVO.setTransferErrorMsg( bean.getTransferErrorMsg() );
        brokerageWithdrawRespVO.setCreateTime( bean.getCreateTime() );

        return brokerageWithdrawRespVO;
    }

    @Override
    public List<BrokerageWithdrawRespVO> convertList(List<BrokerageWithdrawDO> list) {
        if ( list == null ) {
            return null;
        }

        List<BrokerageWithdrawRespVO> list1 = new ArrayList<BrokerageWithdrawRespVO>( list.size() );
        for ( BrokerageWithdrawDO brokerageWithdrawDO : list ) {
            list1.add( convert( brokerageWithdrawDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<BrokerageWithdrawRespVO> convertPage(PageResult<BrokerageWithdrawDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<BrokerageWithdrawRespVO> pageResult = new PageResult<BrokerageWithdrawRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }
}
