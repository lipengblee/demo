package com.star.lp.module.trade.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门店设备响应
 */
@Data
@Schema(description = "门店设备响应")
public class StoreDeviceRespVO {

    @Schema(description = "设备ID", example = "1")
    private Long id;

    @Schema(description = "设备名称", example = "打印机1号")
    private String name;

    @Schema(description = "设备类型", example = "激光打印机")
    private String type;

    @Schema(description = "设备型号", example = "HP LaserJet Pro")
    private String model;

    @Schema(description = "设备状态", example = "online")
    private String status; // online, offline, busy, error

    @Schema(description = "设备位置")
    private String location;

    @Schema(description = "连接地址")
    private String address;

    @Schema(description = "连接类型")
    private String connectionType;

    @Schema(description = "队列数量", example = "3")
    private Integer queueCount;

    @Schema(description = "今日完成数", example = "25")
    private Integer todayCount;

    @Schema(description = "成功率", example = "95")
    private Integer successRate;

    @Schema(description = "纸张状态", example = "sufficient")
    private String paperStatus; // sufficient, low, empty

    @Schema(description = "墨水水平", example = "75")
    private Integer inkLevel;

    @Schema(description = "最后连接时间")
    private LocalDateTime lastConnectTime;

    @Schema(description = "端口", example = "9100")
    private Integer port;

    @Schema(description = "备注", example = "备注")
    private String remark;
}
