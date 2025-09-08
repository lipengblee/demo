package com.star.lp.module.trade.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 添加设备请求
 */
@Data
@Schema(description = "添加设备请求")
public class AddDeviceReqVO {

    @Schema(description = "设备名称", required = true, example = "打印机1号")
    @NotBlank(message = "设备名称不能为空")
    private String name;

    @Schema(description = "设备类型", required = true, example = "激光打印机")
    @NotBlank(message = "设备类型不能为空")
    private String type;

    @Schema(description = "设备型号", example = "HP LaserJet Pro")
    private String model;

    @Schema(description = "设备位置", example = "二楼前台")
    private String location;

    @Schema(description = "连接地址", required = true, example = "192.168.1.100")
    @NotBlank(message = "连接地址不能为空")
    private String address;

    @Schema(description = "连接类型", example = "TCP")
    private String connectionType; // TCP, USB, Bluetooth

    @Schema(description = "端口", example = "9100")
    private Integer port;

    @Schema(description = "备注")
    private String remark;
}
