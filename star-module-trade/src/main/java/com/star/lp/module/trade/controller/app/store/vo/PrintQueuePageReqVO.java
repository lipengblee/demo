package com.star.lp.module.trade.controller.app.store.vo;

import com.star.lp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 打印队列分页请求
 */
@Data
@Schema(description = "打印队列分页请求")
public class PrintQueuePageReqVO extends PageParam {

    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "队列状态")
    private String status;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;
}
