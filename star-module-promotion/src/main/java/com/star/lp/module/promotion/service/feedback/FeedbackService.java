package com.star.lp.module.promotion.service.feedback;

import java.util.*;
import jakarta.validation.*;
import com.star.lp.module.promotion.controller.admin.feedback.vo.*;
import com.star.lp.module.promotion.dal.dataobject.feedback.FeedbackDO;
import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.common.pojo.PageParam;

/**
 * 意见反馈 Service 接口
 *
 * @author LIPENG
 */
public interface FeedbackService {

    /**
     * 创建意见反馈
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFeedback(@Valid FeedbackSaveReqVO createReqVO);

    /**
     * 更新意见反馈
     *
     * @param updateReqVO 更新信息
     */
    void updateFeedback(@Valid FeedbackSaveReqVO updateReqVO);

    /**
     * 删除意见反馈
     *
     * @param id 编号
     */
    void deleteFeedback(Long id);

    /**
    * 批量删除意见反馈
    *
    * @param ids 编号
    */
    void deleteFeedbackListByIds(List<Long> ids);

    /**
     * 获得意见反馈
     *
     * @param id 编号
     * @return 意见反馈
     */
    FeedbackDO getFeedback(Long id);

    /**
     * 获得意见反馈分页
     *
     * @param pageReqVO 分页查询
     * @return 意见反馈分页
     */
    PageResult<FeedbackDO> getFeedbackPage(FeedbackPageReqVO pageReqVO);

}