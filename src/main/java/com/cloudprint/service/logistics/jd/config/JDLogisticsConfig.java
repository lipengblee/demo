package com.star.lp.module.trade.service.logistics.jd.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 京东物流配置类
 *
 * @author star
 */
@Component
@ConfigurationProperties(prefix = "jd.logistics")
@Data
public class JDLogisticsConfig {

    /**
     * 当前使用的域名
     */
    private String currentDomain;

    /**
     * 连接超时时间（毫秒）
     */
    private Integer connectTimeout;

    /**
     * 读取超时时间（毫秒）
     */
    private Integer readTimeout;

    /**
     * 默认应用密钥（当门店无配置时使用）
     */
    private String appKey;

    /**
     * 默认应用密钥（当门店无配置时使用）
     */
    private String appSecret;

    /**
     * 默认访问令牌（当门店无配置时使用）
     */
    private String accessToken;

    /*
     * 默认京东物流默认客户编码
     */
    private String customerCode;
}
