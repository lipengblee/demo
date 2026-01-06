package com.star.lp.module.trade.dal.dataobject.print;

import com.star.lp.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 打印队列 DO
 */
@TableName("trade_print_queue")
@KeySequence("trade_print_queue_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrintQueueDO extends BaseDO {

    /**
     * 队列ID
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 队列索引
     */
    private Integer queueIndex;

    /**
     * 任务状态
     * waiting-等待, printing-打印中, completed-完成, failed-失败, paused-暂停, cancelled-取消
     */
    private String status;

    /**
     * 优先级 (1-高, 2-中, 3-低)
     */
    private Integer priority;

    /**
     * 文档标题
     */
    private String documentTitle;

    /**
     * 文档URL
     */
    private String documentUrl;

    /**
     * 文档类型
     */
    private String documentType;

    /**
     * 页数
     */
    private Integer pages;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 份数
     */
    private Integer copies;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 打印进度 (0-100)
     */
    private Integer progress;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 预计完成时间
     */
    private LocalDateTime estimatedCompleteTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除
     */
    private Boolean deleted;
}