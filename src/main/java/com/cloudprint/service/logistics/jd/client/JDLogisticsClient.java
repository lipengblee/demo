package com.star.lp.module.trade.service.logistics.jd.client;

import com.lop.open.api.sdk.DefaultDomainApiClient;
import com.lop.open.api.sdk.LopException;
import com.lop.open.api.sdk.request.DomainAbstractRequest;
import com.lop.open.api.sdk.response.AbstractResponse;
import com.lop.open.api.sdk.plugin.LopPlugin;
import com.lop.open.api.sdk.plugin.factory.OAuth2PluginFactory;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreLogisticsConfigDO;
import com.star.lp.module.trade.service.delivery.DeliveryPickUpStoreLogisticsConfigService;
import com.star.lp.module.trade.service.logistics.jd.config.JDLogisticsConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 京东物流客户端工具类
 *
 * @author star
 */
@Slf4j
public class JDLogisticsClient {
    
    private final JDLogisticsConfig config;
    private final DeliveryPickUpStoreLogisticsConfigService logisticsConfigService;
    private DefaultDomainApiClient client;
    
    /**
     * 物流名称 - 京东物流
     */
    private static final String LOGISTICS_NAME_JD = "京东快递";
    
    /**
     * 构造方法
     */
    public JDLogisticsClient(JDLogisticsConfig config, DeliveryPickUpStoreLogisticsConfigService logisticsConfigService) {
        this.config = config;
        this.logisticsConfigService = logisticsConfigService;
        this.initClient();
    }
    
    /**
     * 初始化API客户端
     */
    private void initClient() {
        // DefaultDomainApiClient对象全局只需要创建一次
        this.client = new DefaultDomainApiClient(
                config.getCurrentDomain(),
                config.getConnectTimeout(),
                config.getReadTimeout());
    }
    
    /**
     * 重新初始化客户端（用于切换环境）
     */
    public void reinitClient() {
        this.initClient();
    }
    
    /**
     * 执行API请求
     *
     * @param request 请求对象
     * @param <T>     响应类型
     * @return 响应结果
     * @throws LopException 京东物流API异常
     */
    public <T extends AbstractResponse> T execute(DomainAbstractRequest<T> request) throws LopException {
        // 调用新的execute方法，传入null作为storeId，使用默认配置
        return this.execute(request, null);
    }
    
    /**
     * 执行API请求
     *
     * @param request 请求对象
     * @param storeId 门店ID，用于从数据库获取物流配置
     * @param <T>     响应类型
     * @return 响应结果
     * @throws LopException 京东物流API异常
     */
    public <T extends AbstractResponse> T execute(DomainAbstractRequest<T> request, Long storeId) throws LopException {
        // 获取物流配置（只请求一次数据库）
        DeliveryPickUpStoreLogisticsConfigDO logisticsConfig = this.getLogisticsConfigByStoreId(storeId);

        // 创建OAuth2认证插件
        LopPlugin lopPlugin = OAuth2PluginFactory.produceLopPlugin(
                this.getAppKey(logisticsConfig),
                this.getAppSecret(logisticsConfig),
                this.getAccessToken(logisticsConfig));
        
        // 添加认证插件到请求
        request.addLopPlugin(lopPlugin);
        
        // 执行请求
        return client.execute(request);
    }
    
    /**
     * 根据门店ID获取物流配置
     * @param storeId 门店ID
     * @return 物流配置对象
     */
    private DeliveryPickUpStoreLogisticsConfigDO getLogisticsConfigByStoreId(Long storeId) {
        if (storeId == null) {
            return null;
        }
        return logisticsConfigService.getLogisticsConfigByStoreIdAndLogisticsName(storeId, LOGISTICS_NAME_JD);
    }
    
    /**
     * 根据物流配置获取AppKey
     * @param logisticsConfig 物流配置对象
     * @return AppKey
     */
    private String getAppKey(DeliveryPickUpStoreLogisticsConfigDO logisticsConfig) {
        return logisticsConfig != null ? logisticsConfig.getAppKey() : config.getAppKey();
    }
    
    /**
     * 根据物流配置获取AppSecret
     * @param logisticsConfig 物流配置对象
     * @return AppSecret
     */
    private String getAppSecret(DeliveryPickUpStoreLogisticsConfigDO logisticsConfig) {
        return logisticsConfig != null ? logisticsConfig.getAppSecret() : config.getAppSecret();
    }
    
    /**
     * 根据物流配置获取AccessToken
     * @param logisticsConfig 物流配置对象
     * @return AccessToken
     */
    private String getAccessToken(DeliveryPickUpStoreLogisticsConfigDO logisticsConfig) {
        return logisticsConfig != null ? logisticsConfig.getAccessToken() : config.getAccessToken();
    }
    
    /**
     * 获取当前客户端实例
     *
     * @return DefaultDomainApiClient实例
     */
    public DefaultDomainApiClient getClient() {
        return client;
    }
}
