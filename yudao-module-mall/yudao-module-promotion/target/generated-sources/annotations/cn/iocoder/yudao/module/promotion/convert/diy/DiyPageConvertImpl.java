package cn.iocoder.yudao.module.promotion.convert.diy;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.diy.vo.page.DiyPageCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.diy.vo.page.DiyPagePropertyRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.diy.vo.page.DiyPagePropertyUpdateRequestVO;
import cn.iocoder.yudao.module.promotion.controller.admin.diy.vo.page.DiyPageRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.diy.vo.page.DiyPageUpdateReqVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.diy.DiyPageDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T17:11:08+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class DiyPageConvertImpl implements DiyPageConvert {

    @Override
    public DiyPageDO convert(DiyPageCreateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        DiyPageDO.DiyPageDOBuilder diyPageDO = DiyPageDO.builder();

        diyPageDO.templateId( bean.getTemplateId() );
        diyPageDO.name( bean.getName() );
        diyPageDO.remark( bean.getRemark() );
        List<String> list = bean.getPreviewPicUrls();
        if ( list != null ) {
            diyPageDO.previewPicUrls( new ArrayList<String>( list ) );
        }

        return diyPageDO.build();
    }

    @Override
    public DiyPageDO convert(DiyPageUpdateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        DiyPageDO.DiyPageDOBuilder diyPageDO = DiyPageDO.builder();

        diyPageDO.id( bean.getId() );
        diyPageDO.templateId( bean.getTemplateId() );
        diyPageDO.name( bean.getName() );
        diyPageDO.remark( bean.getRemark() );
        List<String> list = bean.getPreviewPicUrls();
        if ( list != null ) {
            diyPageDO.previewPicUrls( new ArrayList<String>( list ) );
        }

        return diyPageDO.build();
    }

    @Override
    public DiyPageRespVO convert(DiyPageDO bean) {
        if ( bean == null ) {
            return null;
        }

        DiyPageRespVO diyPageRespVO = new DiyPageRespVO();

        diyPageRespVO.setTemplateId( bean.getTemplateId() );
        diyPageRespVO.setName( bean.getName() );
        diyPageRespVO.setRemark( bean.getRemark() );
        List<String> list = bean.getPreviewPicUrls();
        if ( list != null ) {
            diyPageRespVO.setPreviewPicUrls( new ArrayList<String>( list ) );
        }
        diyPageRespVO.setId( bean.getId() );
        diyPageRespVO.setCreateTime( bean.getCreateTime() );

        return diyPageRespVO;
    }

    @Override
    public List<DiyPageRespVO> convertList(List<DiyPageDO> list) {
        if ( list == null ) {
            return null;
        }

        List<DiyPageRespVO> list1 = new ArrayList<DiyPageRespVO>( list.size() );
        for ( DiyPageDO diyPageDO : list ) {
            list1.add( convert( diyPageDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<DiyPageRespVO> convertPage(PageResult<DiyPageDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<DiyPageRespVO> pageResult = new PageResult<DiyPageRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public DiyPageCreateReqVO convertCreateVo(Long templateId, String name, String remark) {
        if ( templateId == null && name == null && remark == null ) {
            return null;
        }

        DiyPageCreateReqVO diyPageCreateReqVO = new DiyPageCreateReqVO();

        diyPageCreateReqVO.setTemplateId( templateId );
        diyPageCreateReqVO.setName( name );
        diyPageCreateReqVO.setRemark( remark );

        return diyPageCreateReqVO;
    }

    @Override
    public DiyPagePropertyRespVO convertPropertyVo(DiyPageDO diyPage) {
        if ( diyPage == null ) {
            return null;
        }

        DiyPagePropertyRespVO diyPagePropertyRespVO = new DiyPagePropertyRespVO();

        diyPagePropertyRespVO.setTemplateId( diyPage.getTemplateId() );
        diyPagePropertyRespVO.setName( diyPage.getName() );
        diyPagePropertyRespVO.setRemark( diyPage.getRemark() );
        List<String> list = diyPage.getPreviewPicUrls();
        if ( list != null ) {
            diyPagePropertyRespVO.setPreviewPicUrls( new ArrayList<String>( list ) );
        }
        diyPagePropertyRespVO.setId( diyPage.getId() );
        diyPagePropertyRespVO.setProperty( diyPage.getProperty() );

        return diyPagePropertyRespVO;
    }

    @Override
    public DiyPageDO convert(DiyPagePropertyUpdateRequestVO updateReqVO) {
        if ( updateReqVO == null ) {
            return null;
        }

        DiyPageDO.DiyPageDOBuilder diyPageDO = DiyPageDO.builder();

        diyPageDO.id( updateReqVO.getId() );
        diyPageDO.property( updateReqVO.getProperty() );

        return diyPageDO.build();
    }
}
