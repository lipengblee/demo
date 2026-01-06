package com.star.lp.module.trade.dal.dataobject.delivery;

import com.star.lp.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 自提门店物流配置 DO
 *
 * @author star
 */
@TableName("trade_delivery_pick_up_store_logistics_config")
@KeySequence("trade_delivery_pick_up_store_logistics_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
public class DeliveryPickUpStoreLogisticsConfigDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 自提门店ID，外键关联trade_delivery_pick_up_store.id
     */
    private Long storeId;

    /**
     * 物流名称
     */
    private String logisticsName;

    /**
     * 应用密钥
     */
    private String appKey;

    /**
     * 应用密钥
     */
    private String appSecret;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 寄件人姓名
     */
    private String senderName;

    /**
     * 寄件人电话
     */
    private String senderPhone;

    /**
     * 寄件人详细地址
     */
    private String senderAddress;

    /**
     * 寄件人省份
     */
    private String senderProvince;

    /**
     * 寄件人城市
     */
    private String senderCity;

    /**
     * 寄件人区县
     */
    private String senderDistrict;

}
