package cn.iocoder.yudao.module.trade.convert.delivery;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.trade.controller.admin.delivery.vo.express.DeliveryExpressCreateReqVO;
import cn.iocoder.yudao.module.trade.controller.admin.delivery.vo.express.DeliveryExpressExcelVO;
import cn.iocoder.yudao.module.trade.controller.admin.delivery.vo.express.DeliveryExpressRespVO;
import cn.iocoder.yudao.module.trade.controller.admin.delivery.vo.express.DeliveryExpressSimpleRespVO;
import cn.iocoder.yudao.module.trade.controller.admin.delivery.vo.express.DeliveryExpressUpdateReqVO;
import cn.iocoder.yudao.module.trade.controller.app.delivery.vo.express.AppDeliveryExpressRespVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.delivery.DeliveryExpressDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T17:11:17+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class DeliveryExpressConvertImpl implements DeliveryExpressConvert {

    @Override
    public DeliveryExpressDO convert(DeliveryExpressCreateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        DeliveryExpressDO deliveryExpressDO = new DeliveryExpressDO();

        deliveryExpressDO.setCode( bean.getCode() );
        deliveryExpressDO.setName( bean.getName() );
        deliveryExpressDO.setLogo( bean.getLogo() );
        deliveryExpressDO.setSort( bean.getSort() );
        deliveryExpressDO.setStatus( bean.getStatus() );

        return deliveryExpressDO;
    }

    @Override
    public DeliveryExpressDO convert(DeliveryExpressUpdateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        DeliveryExpressDO deliveryExpressDO = new DeliveryExpressDO();

        deliveryExpressDO.setId( bean.getId() );
        deliveryExpressDO.setCode( bean.getCode() );
        deliveryExpressDO.setName( bean.getName() );
        deliveryExpressDO.setLogo( bean.getLogo() );
        deliveryExpressDO.setSort( bean.getSort() );
        deliveryExpressDO.setStatus( bean.getStatus() );

        return deliveryExpressDO;
    }

    @Override
    public DeliveryExpressRespVO convert(DeliveryExpressDO bean) {
        if ( bean == null ) {
            return null;
        }

        DeliveryExpressRespVO deliveryExpressRespVO = new DeliveryExpressRespVO();

        deliveryExpressRespVO.setCode( bean.getCode() );
        deliveryExpressRespVO.setName( bean.getName() );
        deliveryExpressRespVO.setLogo( bean.getLogo() );
        deliveryExpressRespVO.setSort( bean.getSort() );
        deliveryExpressRespVO.setStatus( bean.getStatus() );
        deliveryExpressRespVO.setId( bean.getId() );
        deliveryExpressRespVO.setCreateTime( bean.getCreateTime() );

        return deliveryExpressRespVO;
    }

    @Override
    public List<DeliveryExpressRespVO> convertList(List<DeliveryExpressDO> list) {
        if ( list == null ) {
            return null;
        }

        List<DeliveryExpressRespVO> list1 = new ArrayList<DeliveryExpressRespVO>( list.size() );
        for ( DeliveryExpressDO deliveryExpressDO : list ) {
            list1.add( convert( deliveryExpressDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<DeliveryExpressRespVO> convertPage(PageResult<DeliveryExpressDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<DeliveryExpressRespVO> pageResult = new PageResult<DeliveryExpressRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public List<DeliveryExpressExcelVO> convertList02(List<DeliveryExpressDO> list) {
        if ( list == null ) {
            return null;
        }

        List<DeliveryExpressExcelVO> list1 = new ArrayList<DeliveryExpressExcelVO>( list.size() );
        for ( DeliveryExpressDO deliveryExpressDO : list ) {
            list1.add( deliveryExpressDOToDeliveryExpressExcelVO( deliveryExpressDO ) );
        }

        return list1;
    }

    @Override
    public List<DeliveryExpressSimpleRespVO> convertList1(List<DeliveryExpressDO> list) {
        if ( list == null ) {
            return null;
        }

        List<DeliveryExpressSimpleRespVO> list1 = new ArrayList<DeliveryExpressSimpleRespVO>( list.size() );
        for ( DeliveryExpressDO deliveryExpressDO : list ) {
            list1.add( deliveryExpressDOToDeliveryExpressSimpleRespVO( deliveryExpressDO ) );
        }

        return list1;
    }

    @Override
    public List<AppDeliveryExpressRespVO> convertList03(List<DeliveryExpressDO> list) {
        if ( list == null ) {
            return null;
        }

        List<AppDeliveryExpressRespVO> list1 = new ArrayList<AppDeliveryExpressRespVO>( list.size() );
        for ( DeliveryExpressDO deliveryExpressDO : list ) {
            list1.add( deliveryExpressDOToAppDeliveryExpressRespVO( deliveryExpressDO ) );
        }

        return list1;
    }

    protected DeliveryExpressExcelVO deliveryExpressDOToDeliveryExpressExcelVO(DeliveryExpressDO deliveryExpressDO) {
        if ( deliveryExpressDO == null ) {
            return null;
        }

        DeliveryExpressExcelVO deliveryExpressExcelVO = new DeliveryExpressExcelVO();

        deliveryExpressExcelVO.setId( deliveryExpressDO.getId() );
        deliveryExpressExcelVO.setCode( deliveryExpressDO.getCode() );
        deliveryExpressExcelVO.setName( deliveryExpressDO.getName() );
        deliveryExpressExcelVO.setLogo( deliveryExpressDO.getLogo() );
        deliveryExpressExcelVO.setSort( deliveryExpressDO.getSort() );
        deliveryExpressExcelVO.setStatus( deliveryExpressDO.getStatus() );
        deliveryExpressExcelVO.setCreateTime( deliveryExpressDO.getCreateTime() );

        return deliveryExpressExcelVO;
    }

    protected DeliveryExpressSimpleRespVO deliveryExpressDOToDeliveryExpressSimpleRespVO(DeliveryExpressDO deliveryExpressDO) {
        if ( deliveryExpressDO == null ) {
            return null;
        }

        DeliveryExpressSimpleRespVO deliveryExpressSimpleRespVO = new DeliveryExpressSimpleRespVO();

        deliveryExpressSimpleRespVO.setId( deliveryExpressDO.getId() );
        deliveryExpressSimpleRespVO.setName( deliveryExpressDO.getName() );

        return deliveryExpressSimpleRespVO;
    }

    protected AppDeliveryExpressRespVO deliveryExpressDOToAppDeliveryExpressRespVO(DeliveryExpressDO deliveryExpressDO) {
        if ( deliveryExpressDO == null ) {
            return null;
        }

        AppDeliveryExpressRespVO appDeliveryExpressRespVO = new AppDeliveryExpressRespVO();

        appDeliveryExpressRespVO.setId( deliveryExpressDO.getId() );
        appDeliveryExpressRespVO.setName( deliveryExpressDO.getName() );

        return appDeliveryExpressRespVO;
    }
}
