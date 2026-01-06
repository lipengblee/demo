package com.star.lp.module.trade.service.logistics.jd.config;

import com.star.lp.module.trade.service.delivery.DeliveryPickUpStoreLogisticsConfigService;
import com.star.lp.module.trade.service.logistics.jd.client.JDLogisticsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 京东物流配置类，用于注册Spring Bean
 *
 * @author star
 */
@Configuration
public class JDLogisticsConfigurer {

    /**
     * 注册京东物流客户端Bean
     *
     * @param jdLogisticsConfig 京东物流配置
     * @param logisticsConfigService 自提门店物流配置服务
     * @return 京东物流客户端实例
     */
    @Bean
    public JDLogisticsClient jdLogisticsClient(
            JDLogisticsConfig jdLogisticsConfig,
            DeliveryPickUpStoreLogisticsConfigService logisticsConfigService) {
        return new JDLogisticsClient(jdLogisticsConfig, logisticsConfigService);
    }

}
