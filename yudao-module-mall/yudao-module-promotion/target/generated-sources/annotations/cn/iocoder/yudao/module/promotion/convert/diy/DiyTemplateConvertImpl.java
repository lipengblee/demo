package cn.iocoder.yudao.module.promotion.convert.diy;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.diy.vo.page.DiyPagePropertyRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.diy.vo.template.DiyTemplateCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.diy.vo.template.DiyTemplatePropertyRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.diy.vo.template.DiyTemplatePropertyUpdateRequestVO;
import cn.iocoder.yudao.module.promotion.controller.admin.diy.vo.template.DiyTemplateRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.diy.vo.template.DiyTemplateUpdateReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.diy.vo.AppDiyTemplatePropertyRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.diy.DiyPageDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.diy.DiyTemplateDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T17:11:08+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class DiyTemplateConvertImpl implements DiyTemplateConvert {

    @Override
    public DiyTemplateDO convert(DiyTemplateCreateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        DiyTemplateDO.DiyTemplateDOBuilder diyTemplateDO = DiyTemplateDO.builder();

        diyTemplateDO.name( bean.getName() );
        diyTemplateDO.remark( bean.getRemark() );
        List<String> list = bean.getPreviewPicUrls();
        if ( list != null ) {
            diyTemplateDO.previewPicUrls( new ArrayList<String>( list ) );
        }

        return diyTemplateDO.build();
    }

    @Override
    public DiyTemplateDO convert(DiyTemplateUpdateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        DiyTemplateDO.DiyTemplateDOBuilder diyTemplateDO = DiyTemplateDO.builder();

        diyTemplateDO.id( bean.getId() );
        diyTemplateDO.name( bean.getName() );
        diyTemplateDO.remark( bean.getRemark() );
        List<String> list = bean.getPreviewPicUrls();
        if ( list != null ) {
            diyTemplateDO.previewPicUrls( new ArrayList<String>( list ) );
        }

        return diyTemplateDO.build();
    }

    @Override
    public DiyTemplateRespVO convert(DiyTemplateDO bean) {
        if ( bean == null ) {
            return null;
        }

        DiyTemplateRespVO diyTemplateRespVO = new DiyTemplateRespVO();

        diyTemplateRespVO.setName( bean.getName() );
        diyTemplateRespVO.setRemark( bean.getRemark() );
        List<String> list = bean.getPreviewPicUrls();
        if ( list != null ) {
            diyTemplateRespVO.setPreviewPicUrls( new ArrayList<String>( list ) );
        }
        diyTemplateRespVO.setId( bean.getId() );
        diyTemplateRespVO.setCreateTime( bean.getCreateTime() );
        diyTemplateRespVO.setUsed( bean.getUsed() );
        diyTemplateRespVO.setUsedTime( bean.getUsedTime() );

        return diyTemplateRespVO;
    }

    @Override
    public List<DiyTemplateRespVO> convertList(List<DiyTemplateDO> list) {
        if ( list == null ) {
            return null;
        }

        List<DiyTemplateRespVO> list1 = new ArrayList<DiyTemplateRespVO>( list.size() );
        for ( DiyTemplateDO diyTemplateDO : list ) {
            list1.add( convert( diyTemplateDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<DiyTemplateRespVO> convertPage(PageResult<DiyTemplateDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<DiyTemplateRespVO> pageResult = new PageResult<DiyTemplateRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public DiyTemplatePropertyRespVO convertPropertyVo(DiyTemplateDO diyTemplate, List<DiyPageDO> pages) {
        if ( diyTemplate == null && pages == null ) {
            return null;
        }

        DiyTemplatePropertyRespVO diyTemplatePropertyRespVO = new DiyTemplatePropertyRespVO();

        if ( diyTemplate != null ) {
            diyTemplatePropertyRespVO.setName( diyTemplate.getName() );
            diyTemplatePropertyRespVO.setRemark( diyTemplate.getRemark() );
            List<String> list = diyTemplate.getPreviewPicUrls();
            if ( list != null ) {
                diyTemplatePropertyRespVO.setPreviewPicUrls( new ArrayList<String>( list ) );
            }
            diyTemplatePropertyRespVO.setId( diyTemplate.getId() );
            diyTemplatePropertyRespVO.setProperty( diyTemplate.getProperty() );
        }
        diyTemplatePropertyRespVO.setPages( diyPageDOListToDiyPagePropertyRespVOList( pages ) );

        return diyTemplatePropertyRespVO;
    }

    @Override
    public AppDiyTemplatePropertyRespVO convertPropertyVo2(DiyTemplateDO diyTemplate, String home, String user) {
        if ( diyTemplate == null && home == null && user == null ) {
            return null;
        }

        AppDiyTemplatePropertyRespVO appDiyTemplatePropertyRespVO = new AppDiyTemplatePropertyRespVO();

        if ( diyTemplate != null ) {
            appDiyTemplatePropertyRespVO.setId( diyTemplate.getId() );
            appDiyTemplatePropertyRespVO.setName( diyTemplate.getName() );
            appDiyTemplatePropertyRespVO.setProperty( diyTemplate.getProperty() );
        }
        appDiyTemplatePropertyRespVO.setHome( home );
        appDiyTemplatePropertyRespVO.setUser( user );

        return appDiyTemplatePropertyRespVO;
    }

    @Override
    public DiyTemplateDO convert(DiyTemplatePropertyUpdateRequestVO updateReqVO) {
        if ( updateReqVO == null ) {
            return null;
        }

        DiyTemplateDO.DiyTemplateDOBuilder diyTemplateDO = DiyTemplateDO.builder();

        diyTemplateDO.id( updateReqVO.getId() );
        diyTemplateDO.property( updateReqVO.getProperty() );

        return diyTemplateDO.build();
    }

    protected DiyPagePropertyRespVO diyPageDOToDiyPagePropertyRespVO(DiyPageDO diyPageDO) {
        if ( diyPageDO == null ) {
            return null;
        }

        DiyPagePropertyRespVO diyPagePropertyRespVO = new DiyPagePropertyRespVO();

        diyPagePropertyRespVO.setTemplateId( diyPageDO.getTemplateId() );
        diyPagePropertyRespVO.setName( diyPageDO.getName() );
        diyPagePropertyRespVO.setRemark( diyPageDO.getRemark() );
        List<String> list = diyPageDO.getPreviewPicUrls();
        if ( list != null ) {
            diyPagePropertyRespVO.setPreviewPicUrls( new ArrayList<String>( list ) );
        }
        diyPagePropertyRespVO.setId( diyPageDO.getId() );
        diyPagePropertyRespVO.setProperty( diyPageDO.getProperty() );

        return diyPagePropertyRespVO;
    }

    protected List<DiyPagePropertyRespVO> diyPageDOListToDiyPagePropertyRespVOList(List<DiyPageDO> list) {
        if ( list == null ) {
            return null;
        }

        List<DiyPagePropertyRespVO> list1 = new ArrayList<DiyPagePropertyRespVO>( list.size() );
        for ( DiyPageDO diyPageDO : list ) {
            list1.add( diyPageDOToDiyPagePropertyRespVO( diyPageDO ) );
        }

        return list1;
    }
}
