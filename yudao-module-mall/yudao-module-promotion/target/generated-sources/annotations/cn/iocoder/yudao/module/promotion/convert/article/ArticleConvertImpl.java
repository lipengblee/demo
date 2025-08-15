package cn.iocoder.yudao.module.promotion.convert.article;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticleCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticleRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticleUpdateReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.article.vo.article.AppArticleRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-12T15:51:56+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class ArticleConvertImpl implements ArticleConvert {

    @Override
    public ArticleDO convert(ArticleCreateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        ArticleDO.ArticleDOBuilder articleDO = ArticleDO.builder();

        articleDO.categoryId( bean.getCategoryId() );
        articleDO.spuId( bean.getSpuId() );
        articleDO.title( bean.getTitle() );
        articleDO.author( bean.getAuthor() );
        articleDO.picUrl( bean.getPicUrl() );
        articleDO.introduction( bean.getIntroduction() );
        articleDO.sort( bean.getSort() );
        articleDO.status( bean.getStatus() );
        articleDO.recommendHot( bean.getRecommendHot() );
        articleDO.recommendBanner( bean.getRecommendBanner() );
        articleDO.content( bean.getContent() );

        return articleDO.build();
    }

    @Override
    public ArticleDO convert(ArticleUpdateReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        ArticleDO.ArticleDOBuilder articleDO = ArticleDO.builder();

        articleDO.id( bean.getId() );
        articleDO.categoryId( bean.getCategoryId() );
        articleDO.spuId( bean.getSpuId() );
        articleDO.title( bean.getTitle() );
        articleDO.author( bean.getAuthor() );
        articleDO.picUrl( bean.getPicUrl() );
        articleDO.introduction( bean.getIntroduction() );
        articleDO.sort( bean.getSort() );
        articleDO.status( bean.getStatus() );
        articleDO.recommendHot( bean.getRecommendHot() );
        articleDO.recommendBanner( bean.getRecommendBanner() );
        articleDO.content( bean.getContent() );

        return articleDO.build();
    }

    @Override
    public ArticleRespVO convert(ArticleDO bean) {
        if ( bean == null ) {
            return null;
        }

        ArticleRespVO articleRespVO = new ArticleRespVO();

        articleRespVO.setCategoryId( bean.getCategoryId() );
        articleRespVO.setSpuId( bean.getSpuId() );
        articleRespVO.setTitle( bean.getTitle() );
        articleRespVO.setAuthor( bean.getAuthor() );
        articleRespVO.setPicUrl( bean.getPicUrl() );
        articleRespVO.setIntroduction( bean.getIntroduction() );
        articleRespVO.setSort( bean.getSort() );
        articleRespVO.setStatus( bean.getStatus() );
        articleRespVO.setRecommendHot( bean.getRecommendHot() );
        articleRespVO.setRecommendBanner( bean.getRecommendBanner() );
        articleRespVO.setContent( bean.getContent() );
        articleRespVO.setId( bean.getId() );
        articleRespVO.setBrowseCount( bean.getBrowseCount() );
        articleRespVO.setCreateTime( bean.getCreateTime() );

        return articleRespVO;
    }

    @Override
    public List<ArticleRespVO> convertList(List<ArticleDO> list) {
        if ( list == null ) {
            return null;
        }

        List<ArticleRespVO> list1 = new ArrayList<ArticleRespVO>( list.size() );
        for ( ArticleDO articleDO : list ) {
            list1.add( convert( articleDO ) );
        }

        return list1;
    }

    @Override
    public PageResult<ArticleRespVO> convertPage(PageResult<ArticleDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<ArticleRespVO> pageResult = new PageResult<ArticleRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public AppArticleRespVO convert01(ArticleDO article) {
        if ( article == null ) {
            return null;
        }

        AppArticleRespVO appArticleRespVO = new AppArticleRespVO();

        appArticleRespVO.setId( article.getId() );
        appArticleRespVO.setTitle( article.getTitle() );
        appArticleRespVO.setAuthor( article.getAuthor() );
        appArticleRespVO.setCategoryId( article.getCategoryId() );
        appArticleRespVO.setPicUrl( article.getPicUrl() );
        appArticleRespVO.setIntroduction( article.getIntroduction() );
        appArticleRespVO.setContent( article.getContent() );
        appArticleRespVO.setCreateTime( article.getCreateTime() );
        appArticleRespVO.setBrowseCount( article.getBrowseCount() );
        appArticleRespVO.setSpuId( article.getSpuId() );

        return appArticleRespVO;
    }

    @Override
    public PageResult<AppArticleRespVO> convertPage02(PageResult<ArticleDO> articlePage) {
        if ( articlePage == null ) {
            return null;
        }

        PageResult<AppArticleRespVO> pageResult = new PageResult<AppArticleRespVO>();

        pageResult.setList( convertList03( articlePage.getList() ) );
        pageResult.setTotal( articlePage.getTotal() );

        return pageResult;
    }

    @Override
    public List<AppArticleRespVO> convertList03(List<ArticleDO> articleCategoryListByRecommendHotAndRecommendBanner) {
        if ( articleCategoryListByRecommendHotAndRecommendBanner == null ) {
            return null;
        }

        List<AppArticleRespVO> list = new ArrayList<AppArticleRespVO>( articleCategoryListByRecommendHotAndRecommendBanner.size() );
        for ( ArticleDO articleDO : articleCategoryListByRecommendHotAndRecommendBanner ) {
            list.add( convert01( articleDO ) );
        }

        return list;
    }
}
