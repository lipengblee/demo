package com.star.lp.module.promotion.dal.dataobject.feedback;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.star.lp.framework.mybatis.core.dataobject.BaseDO;

/**
 * 意见反馈 DO
 *
 * @author LIPENG
 */
@TableName("member_feedback")
@KeySequence("member_feedback_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDO extends BaseDO {

    /**
     * 反馈ID
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 反馈类型：bug-功能异常，suggestion-功能建议，ui-界面问题，performance-性能问题，other-其他问题
     *
     * 枚举 {@link TODO feedback_types 对应的类}
     */
    private String type;
    /**
     * 问题描述
     */
    private String description;
    /**
     * 联系手机号
     */
    private String contactPhone;
    /**
     * 联系邮箱
     */
    private String contactEmail;
    /**
     * 图片列表JSON格式：["url1", "url2"]
     */
    private String images;
    /**
     * 处理状态：1-待处理，2-已处理，3-已关闭
     *
     * 枚举 {@link TODO feedback_status 对应的类}
     */
    private Integer status;
    /**
     * 管理员回复
     */
    private String adminReply;
    /**
     * 回复时间
     */
    private LocalDateTime replyTime;

}