package com.star.lp.module.trade.framework.web.config;

import com.star.lp.framework.swagger.config.StarSpringDocAutoConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * trade 模块的 web 组件的 Configuration
 */
@Configuration(proxyBeanMethods = false)
public class TradeWebConfiguration {

    /**
     * trade 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi tradeGroupedOpenApi() {
        return StarSpringDocAutoConfiguration.buildGroupedOpenApi("trade");
    }

}
