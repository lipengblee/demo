package com.star.lp.module.trade.framework.store.init;

import com.star.lp.module.trade.dal.mysql.print.PrintDeviceMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 门店管理数据初始化
 */
@Component
@ConditionalOnProperty(name = "star.trade.store.init-data", havingValue = "true")
@Slf4j
public class StoreManagementDataInitializer {

    @Resource
    private PrintDeviceMapper printDeviceMapper;

    @PostConstruct
    public void initData() {
        log.info("[StoreManagementDataInitializer] 开始初始化门店管理数据");

        // 检查是否已有设备数据
        Long deviceCount = printDeviceMapper.selectCount();
        if (deviceCount > 0) {
            log.info("[StoreManagementDataInitializer] 设备数据已存在，跳过初始化");
            return;
        }

        // 初始化示例设备数据
        initSampleDevices();

        log.info("[StoreManagementDataInitializer] 门店管理数据初始化完成");
    }

    private void initSampleDevices() {
        // 可以在这里添加一些示例设备数据
        log.info("[StoreManagementDataInitializer] 设备数据初始化完成");
    }
}