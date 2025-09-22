package com.star.lp.module.promotion.service.feedback;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import com.star.lp.module.promotion.controller.admin.feedback.vo.*;
import com.star.lp.module.promotion.dal.dataobject.feedback.FeedbackDO;
import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.common.pojo.PageParam;
import com.star.lp.framework.common.util.object.BeanUtils;

import com.star.lp.module.promotion.dal.mysql.feedback.FeedbackMapper;

import static com.star.lp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.star.lp.framework.common.util.collection.CollectionUtils.convertList;
import static com.star.lp.framework.common.util.collection.CollectionUtils.diffList;
import static com.star.lp.module.promotion.enums.ErrorCodeConstants.*;

/**
 * 意见反馈 Service 实现类
 *
 * @author LIPENG
 */
@Service
@Validated
public class FeedbackServiceImpl implements FeedbackService {

    @Resource
    private FeedbackMapper feedbackMapper;

    @Override
    public Long createFeedback(FeedbackSaveReqVO createReqVO) {
        // 插入
        FeedbackDO feedback = BeanUtils.toBean(createReqVO, FeedbackDO.class);
        feedbackMapper.insert(feedback);

        // 返回
        return feedback.getId();
    }

    @Override
    public void updateFeedback(FeedbackSaveReqVO updateReqVO) {
        // 校验存在
        validateFeedbackExists(updateReqVO.getId());
        // 更新
        updateReqVO.setReplyTime(LocalDateTime.now());
        FeedbackDO updateObj = BeanUtils.toBean(updateReqVO, FeedbackDO.class);
        feedbackMapper.updateById(updateObj);
    }

    @Override
    public void deleteFeedback(Long id) {
        // 校验存在
        validateFeedbackExists(id);
        // 删除
        feedbackMapper.deleteById(id);
    }

    @Override
        public void deleteFeedbackListByIds(List<Long> ids) {
        // 删除
        feedbackMapper.deleteByIds(ids);
        }


    private void validateFeedbackExists(Long id) {
        if (feedbackMapper.selectById(id) == null) {
            throw exception(FEEDBACK_NOT_EXISTS);
        }
    }

    @Override
    public FeedbackDO getFeedback(Long id) {
        return feedbackMapper.selectById(id);
    }

    @Override
    public PageResult<FeedbackDO> getFeedbackPage(FeedbackPageReqVO pageReqVO) {
        return feedbackMapper.selectPage(pageReqVO);
    }

}