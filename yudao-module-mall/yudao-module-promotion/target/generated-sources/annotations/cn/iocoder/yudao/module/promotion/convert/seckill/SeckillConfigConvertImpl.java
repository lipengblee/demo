package cn.iocoder.yudao.module.promotion.convert.seckill;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.config.SeckillConfigCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.config.SeckillConfigRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.config.SeckillConfigSimpleRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.config.SeckillConfigUpdateReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.seckill.vo.config.AppSeckillConfigRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.seckill.SeckillConfigDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-18T17:22:15+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class SeckillConfigConvertImpl implements SeckillConfigConvert {

    @Override
    public SeckillConfigDO convert(SeckillConfigCreateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        SeckillConfigDO.SeckillConfigDOBuilder seckillConfigDO = SeckillConfigDO.builder();

        seckillConfigDO.name( bean.getName() );
        seckillConfigDO.startTime( bean.getStartTime() );
        seckillConfigDO.endTime( bean.getEndTime() );
        List<String> list = bean.getSliderPicUrls();
        if ( list != null ) {
            seckillConfigDO.sliderPicUrls( new ArrayList<String>( list ) );
        }
        seckillConfigDO.status( bean.getStatus() );

        return seckillConfigDO.build();
    }

    @Override
    public SeckillConfigDO convert(SeckillConfigUpdateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        SeckillConfigDO.SeckillConfigDOBuilder seckillConfigDO = SeckillConfigDO.builder();

        seckillConfigDO.id( bean.getId() );
        seckillConfigDO.name( bean.getName() );
        seckillConfigDO.startTime( bean.getStartTime() );
        seckillConfigDO.endTime( bean.getEndTime() );
        List<String> list = bean.getSliderPicUrls();
        if ( list != null ) {
            seckillConfigDO.sliderPicUrls( new ArrayList<String>( list ) );
        }
        seckillConfigDO.status( bean.getStatus() );

        return seckillConfigDO.build();
    }

    @Override
    public SeckillConfigRespVO convert(SeckillConfigDO bean) {
        if ( bean == null ) {
            return null;
        }

        SeckillConfigRespVO seckillConfigRespVO = new SeckillConfigRespVO();

        seckillConfigRespVO.setName( bean.getName() );
        seckillConfigRespVO.setStartTime( bean.getStartTime() );
        seckillConfigRespVO.setEndTime( bean.getEndTime() );
        List<String> list = bean.getSliderPicUrls();
        if ( list != null ) {
            seckillConfigRespVO.setSliderPicUrls( new ArrayList<String>( list ) );
        }
        seckillConfigRespVO.setStatus( bean.getStatus() );
        seckillConfigRespVO.setId( bean.getId() );
        seckillConfigRespVO.setCreateTime( bean.getCreateTime() );

        return seckillConfigRespVO;
    }

    @Override
    public List<SeckillConfigRespVO> convertList(List<SeckillConfigDO> list) {
        if ( list == null ) {
            return null;
        }

        List<SeckillConfigRespVO> list1 = new ArrayList<SeckillConfigRespVO>( list.size() );
        for ( SeckillConfigDO seckillConfigDO : list ) {
            list1.add( convert( seckillConfigDO ) );
        }

        return list1;
    }

    @Override
    public List<SeckillConfigSimpleRespVO> convertList1(List<SeckillConfigDO> list) {
        if ( list == null ) {
            return null;
        }

        List<SeckillConfigSimpleRespVO> list1 = new ArrayList<SeckillConfigSimpleRespVO>( list.size() );
        for ( SeckillConfigDO seckillConfigDO : list ) {
            list1.add( seckillConfigDOToSeckillConfigSimpleRespVO( seckillConfigDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<SeckillConfigRespVO> convertPage(PageResult<SeckillConfigDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<SeckillConfigRespVO> pageResult = new PageResult<SeckillConfigRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public List<AppSeckillConfigRespVO> convertList2(List<SeckillConfigDO> list) {
        if ( list == null ) {
            return null;
        }

        List<AppSeckillConfigRespVO> list1 = new ArrayList<AppSeckillConfigRespVO>( list.size() );
        for ( SeckillConfigDO seckillConfigDO : list ) {
            list1.add( convert1( seckillConfigDO ) );
        }

        return list1;
    }

    @Override
    public AppSeckillConfigRespVO convert1(SeckillConfigDO filteredConfig) {
        if ( filteredConfig == null ) {
            return null;
        }

        AppSeckillConfigRespVO appSeckillConfigRespVO = new AppSeckillConfigRespVO();

        appSeckillConfigRespVO.setId( filteredConfig.getId() );
        appSeckillConfigRespVO.setStartTime( filteredConfig.getStartTime() );
        appSeckillConfigRespVO.setEndTime( filteredConfig.getEndTime() );
        List<String> list = filteredConfig.getSliderPicUrls();
        if ( list != null ) {
            appSeckillConfigRespVO.setSliderPicUrls( new ArrayList<String>( list ) );
        }

        return appSeckillConfigRespVO;
    }

    protected SeckillConfigSimpleRespVO seckillConfigDOToSeckillConfigSimpleRespVO(SeckillConfigDO seckillConfigDO) {
        if ( seckillConfigDO == null ) {
            return null;
        }

        SeckillConfigSimpleRespVO seckillConfigSimpleRespVO = new SeckillConfigSimpleRespVO();

        seckillConfigSimpleRespVO.setId( seckillConfigDO.getId() );
        seckillConfigSimpleRespVO.setName( seckillConfigDO.getName() );
        seckillConfigSimpleRespVO.setStartTime( seckillConfigDO.getStartTime() );
        seckillConfigSimpleRespVO.setEndTime( seckillConfigDO.getEndTime() );

        return seckillConfigSimpleRespVO;
    }
}
