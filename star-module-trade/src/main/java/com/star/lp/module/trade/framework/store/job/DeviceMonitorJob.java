package com.star.lp.module.trade.framework.store.job;

import cn.hutool.core.collection.CollUtil;
import com.star.lp.framework.quartz.core.handler.JobHandler;
import com.star.lp.framework.tenant.core.job.TenantJob;
import com.star.lp.module.trade.dal.dataobject.print.PrintDeviceDO;
import com.star.lp.module.trade.dal.mysql.print.PrintDeviceMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备状态监控任务
 */
@Component
@Slf4j
public class DeviceMonitorJob implements JobHandler {

    @Resource
    private PrintDeviceMapper printDeviceMapper;

    @Override
    public String execute(String param) throws Exception {
        log.info("[DeviceMonitorJob] 开始执行设备状态监控");

        // 查询所有设备
        List<PrintDeviceDO> devices = printDeviceMapper.selectList();
        if (CollUtil.isEmpty(devices)) {
            log.info("[DeviceMonitorJob] 没有发现设备");
            return "没有发现设备";
        }

        int offlineCount = 0;
        LocalDateTime offlineThreshold = LocalDateTime.now().minusMinutes(10);

        for (PrintDeviceDO device : devices) {
            // 检查设备是否离线
            if (device.getLastConnectTime() != null &&
                    device.getLastConnectTime().isBefore(offlineThreshold) &&
                    !"offline".equals(device.getStatus())) {

                // 更新设备状态为离线
                printDeviceMapper.updateById(
                        new PrintDeviceDO().setId(device.getId()).setStatus("offline")
                );
                offlineCount++;
                log.warn("[DeviceMonitorJob] 设备 {} 已离线", device.getName());
            }
        }

        String result = String.format("设备状态监控完成，发现 %d 台离线设备", offlineCount);
        log.info("[DeviceMonitorJob] {}", result);
        return result;
    }
}