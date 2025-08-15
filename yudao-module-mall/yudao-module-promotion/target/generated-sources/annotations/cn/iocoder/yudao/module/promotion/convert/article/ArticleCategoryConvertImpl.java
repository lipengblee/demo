package cn.iocoder.yudao.module.promotion.convert.article;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.category.ArticleCategoryCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.category.ArticleCategoryRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.category.ArticleCategorySimpleRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.category.ArticleCategoryUpdateReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.article.vo.category.AppArticleCategoryRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleCategoryDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-12T15:51:56+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class ArticleCategoryConvertImpl implements ArticleCategoryConvert {

    @Override
    public ArticleCategoryDO convert(ArticleCategoryCreateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        ArticleCategoryDO.ArticleCategoryDOBuilder articleCategoryDO = ArticleCategoryDO.builder();

        articleCategoryDO.name( bean.getName() );
        articleCategoryDO.picUrl( bean.getPicUrl() );
        articleCategoryDO.status( bean.getStatus() );
        articleCategoryDO.sort( bean.getSort() );

        return articleCategoryDO.build();
    }

    @Override
    public ArticleCategoryDO convert(ArticleCategoryUpdateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        ArticleCategoryDO.ArticleCategoryDOBuilder articleCategoryDO = ArticleCategoryDO.builder();

        articleCategoryDO.id( bean.getId() );
        articleCategoryDO.name( bean.getName() );
        articleCategoryDO.picUrl( bean.getPicUrl() );
        articleCategoryDO.status( bean.getStatus() );
        articleCategoryDO.sort( bean.getSort() );

        return articleCategoryDO.build();
    }

    @Override
    public ArticleCategoryRespVO convert(ArticleCategoryDO bean) {
        if ( bean == null ) {
            return null;
        }

        ArticleCategoryRespVO articleCategoryRespVO = new ArticleCategoryRespVO();

        articleCategoryRespVO.setName( bean.getName() );
        articleCategoryRespVO.setPicUrl( bean.getPicUrl() );
        articleCategoryRespVO.setStatus( bean.getStatus() );
        articleCategoryRespVO.setSort( bean.getSort() );
        articleCategoryRespVO.setId( bean.getId() );
        articleCategoryRespVO.setCreateTime( bean.getCreateTime() );

        return articleCategoryRespVO;
    }

    @Override
    public List<ArticleCategoryRespVO> convertList(List<ArticleCategoryDO> list) {
        if ( list == null ) {
            return null;
        }

        List<ArticleCategoryRespVO> list1 = new ArrayList<ArticleCategoryRespVO>( list.size() );
        for ( ArticleCategoryDO articleCategoryDO : list ) {
            list1.add( convert( articleCategoryDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<ArticleCategoryRespVO> convertPage(PageResult<ArticleCategoryDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<ArticleCategoryRespVO> pageResult = new PageResult<ArticleCategoryRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public List<ArticleCategorySimpleRespVO> convertList03(List<ArticleCategoryDO> list) {
        if ( list == null ) {
            return null;
        }

        List<ArticleCategorySimpleRespVO> list1 = new ArrayList<ArticleCategorySimpleRespVO>( list.size() );
        for ( ArticleCategoryDO articleCategoryDO : list ) {
            list1.add( articleCategoryDOToArticleCategorySimpleRespVO( articleCategoryDO ) );
        }

        return list1;
    }

    @Override
    public List<AppArticleCategoryRespVO> convertList04(List<ArticleCategoryDO> categoryList) {
        if ( categoryList == null ) {
            return null;
        }

        List<AppArticleCategoryRespVO> list = new ArrayList<AppArticleCategoryRespVO>( categoryList.size() );
        for ( ArticleCategoryDO articleCategoryDO : categoryList ) {
            list.add( articleCategoryDOToAppArticleCategoryRespVO( articleCategoryDO ) );
        }

        return list;
    }

    protected ArticleCategorySimpleRespVO articleCategoryDOToArticleCategorySimpleRespVO(ArticleCategoryDO articleCategoryDO) {
        if ( articleCategoryDO == null ) {
            return null;
        }

        ArticleCategorySimpleRespVO articleCategorySimpleRespVO = new ArticleCategorySimpleRespVO();

        articleCategorySimpleRespVO.setId( articleCategoryDO.getId() );
        articleCategorySimpleRespVO.setName( articleCategoryDO.getName() );

        return articleCategorySimpleRespVO;
    }

    protected AppArticleCategoryRespVO articleCategoryDOToAppArticleCategoryRespVO(ArticleCategoryDO articleCategoryDO) {
        if ( articleCategoryDO == null ) {
            return null;
        }

        AppArticleCategoryRespVO appArticleCategoryRespVO = new AppArticleCategoryRespVO();

        appArticleCategoryRespVO.setId( articleCategoryDO.getId() );
        appArticleCategoryRespVO.setName( articleCategoryDO.getName() );
        appArticleCategoryRespVO.setPicUrl( articleCategoryDO.getPicUrl() );

        return appArticleCategoryRespVO;
    }
}
