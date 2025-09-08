package com.star.lp.module.trade.dal.dataobject.print;

import com.star.lp.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 打印设备 DO
 */
@TableName("trade_print_device")
@KeySequence("trade_print_device_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrintDeviceDO extends BaseDO {

    /**
     * 设备ID
     */
    private Long id;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备类型
     */
    private String type;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 设备状态
     * online-在线, offline-离线, busy-忙碌, paused-暂停, error-故障
     */
    private String status;

    /**
     * 设备位置
     */
    private String location;

    /**
     * 连接地址
     */
    private String address;

    /**
     * 连接类型
     * TCP, USB, Bluetooth
     */
    private String connectionType;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 纸张状态
     * sufficient-充足, low-不足, empty-缺纸
     */
    private String paperStatus;

    /**
     * 墨水水平 (0-100)
     */
    private Integer inkLevel;

    /**
     * 队列数量
     */
    private Integer queueCount;

    /**
     * 今日完成数
     */
    private Integer todayCount;

    /**
     * 成功率
     */
    private Integer successRate;

    /**
     * 最后连接时间
     */
    private LocalDateTime lastConnectTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除
     */
    private Boolean deleted;
}
