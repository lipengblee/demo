package com.star.lp.module.promotion.dal.mysql.feedback;

import java.util.*;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.star.lp.framework.mybatis.core.mapper.BaseMapperX;
import com.star.lp.module.promotion.dal.dataobject.feedback.FeedbackDO;
import org.apache.ibatis.annotations.Mapper;
import com.star.lp.module.promotion.controller.admin.feedback.vo.*;

/**
 * 意见反馈 Mapper
 *
 * @author LIPENG
 */
@Mapper
public interface FeedbackMapper extends BaseMapperX<FeedbackDO> {

    default PageResult<FeedbackDO> selectPage(FeedbackPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FeedbackDO>()
                .eqIfPresent(FeedbackDO::getUserId, reqVO.getUserId())
                .eqIfPresent(FeedbackDO::getType, reqVO.getType())
                .eqIfPresent(FeedbackDO::getContactPhone, reqVO.getContactPhone())
                .eqIfPresent(FeedbackDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(FeedbackDO::getUpdateTime, reqVO.getUpdateTime())
                .orderByDesc(FeedbackDO::getId));
    }

}