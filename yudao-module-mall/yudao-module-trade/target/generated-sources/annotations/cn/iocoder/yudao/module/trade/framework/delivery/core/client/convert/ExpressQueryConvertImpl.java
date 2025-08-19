package cn.iocoder.yudao.module.trade.framework.delivery.core.client.convert;

import cn.iocoder.yudao.module.trade.framework.delivery.core.client.dto.ExpressTrackQueryReqDTO;
import cn.iocoder.yudao.module.trade.framework.delivery.core.client.dto.ExpressTrackRespDTO;
import cn.iocoder.yudao.module.trade.framework.delivery.core.client.dto.kd100.Kd100ExpressQueryReqDTO;
import cn.iocoder.yudao.module.trade.framework.delivery.core.client.dto.kd100.Kd100ExpressQueryRespDTO;
import cn.iocoder.yudao.module.trade.framework.delivery.core.client.dto.kdniao.KdNiaoExpressQueryReqDTO;
import cn.iocoder.yudao.module.trade.framework.delivery.core.client.dto.kdniao.KdNiaoExpressQueryRespDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-18T17:52:03+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class ExpressQueryConvertImpl implements ExpressQueryConvert {

    @Override
    public List<ExpressTrackRespDTO> convertList(List<KdNiaoExpressQueryRespDTO.ExpressTrack> list) {
        if ( list == null ) {
            return null;
        }

        List<ExpressTrackRespDTO> list1 = new ArrayList<ExpressTrackRespDTO>( list.size() );
        for ( KdNiaoExpressQueryRespDTO.ExpressTrack expressTrack : list ) {
            list1.add( convert( expressTrack ) );
        }

        return list1;
    }

    @Override
    public ExpressTrackRespDTO convert(KdNiaoExpressQueryRespDTO.ExpressTrack track) {
        if ( track == null ) {
            return null;
        }

        ExpressTrackRespDTO expressTrackRespDTO = new ExpressTrackRespDTO();

        expressTrackRespDTO.setTime( track.getAcceptTime() );
        expressTrackRespDTO.setContent( track.getAcceptStation() );

        return expressTrackRespDTO;
    }

    @Override
    public List<ExpressTrackRespDTO> convertList2(List<Kd100ExpressQueryRespDTO.ExpressTrack> list) {
        if ( list == null ) {
            return null;
        }

        List<ExpressTrackRespDTO> list1 = new ArrayList<ExpressTrackRespDTO>( list.size() );
        for ( Kd100ExpressQueryRespDTO.ExpressTrack expressTrack : list ) {
            list1.add( convert( expressTrack ) );
        }

        return list1;
    }

    @Override
    public ExpressTrackRespDTO convert(Kd100ExpressQueryRespDTO.ExpressTrack track) {
        if ( track == null ) {
            return null;
        }

        ExpressTrackRespDTO expressTrackRespDTO = new ExpressTrackRespDTO();

        expressTrackRespDTO.setContent( track.getContext() );
        expressTrackRespDTO.setTime( track.getTime() );

        return expressTrackRespDTO;
    }

    @Override
    public KdNiaoExpressQueryReqDTO convert(ExpressTrackQueryReqDTO dto) {
        if ( dto == null ) {
            return null;
        }

        KdNiaoExpressQueryReqDTO kdNiaoExpressQueryReqDTO = new KdNiaoExpressQueryReqDTO();

        kdNiaoExpressQueryReqDTO.setExpressCode( dto.getExpressCode() );
        kdNiaoExpressQueryReqDTO.setLogisticsNo( dto.getLogisticsNo() );
        kdNiaoExpressQueryReqDTO.setCustomerName( dto.getCustomerName() );

        return kdNiaoExpressQueryReqDTO;
    }

    @Override
    public Kd100ExpressQueryReqDTO convert2(ExpressTrackQueryReqDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Kd100ExpressQueryReqDTO kd100ExpressQueryReqDTO = new Kd100ExpressQueryReqDTO();

        kd100ExpressQueryReqDTO.setExpressCode( dto.getExpressCode() );
        kd100ExpressQueryReqDTO.setLogisticsNo( dto.getLogisticsNo() );
        kd100ExpressQueryReqDTO.setPhone( dto.getPhone() );

        return kd100ExpressQueryReqDTO;
    }
}
