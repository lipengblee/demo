package com.star.lp.module.trade.framework.store.config;

import com.star.lp.module.trade.framework.store.core.interceptor.StoreManagementInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 门店管理Web配置
 */
@Configuration
public class StoreManagementWebConfig implements WebMvcConfigurer {

    @Resource
    private StoreManagementInterceptor storeManagementInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(storeManagementInterceptor)
                .addPathPatterns("/trade/store/**")
                .excludePathPatterns(
                        "/trade/store/login",
                        "/trade/store/logout"
                );
    }
}