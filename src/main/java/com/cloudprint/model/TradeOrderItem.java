package com.cloudprint.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TradeOrderItem {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long cartId;
    private Long spuId;
    private String spuName;
    private Long skuId;
    private String properties;
    private String picUrl;
    private Integer count;
    private Boolean commentStatus;
    private Integer price;
    private Integer discountPrice;
    private Integer deliveryPrice;
    private Integer adjustPrice;
    private Integer payPrice;
    private Integer couponPrice;
    private Integer pointPrice;
    private Integer usePoint;
    private Integer givePoint;
    private Integer vipPrice;
    private Long afterSaleId;
    private Integer afterSaleStatus;
    private String creator;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    private String updater;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    private Boolean deleted;
    private Long tenantId;
    private Integer pageStart;
    private Integer pageEnd;
}