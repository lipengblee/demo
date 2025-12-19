package com.cloudprint.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TradeOrder {
    private Long id;
    private String no;
    private Integer type;
    private Integer terminal;
    private Long userId;
    private String userIp;
    private String userRemark;
    private Integer status;
    private Integer productCount;
    private Integer cancelType;
    private String remark;
    private Boolean commentStatus;
    private Long brokerageUserId;
    private Long payOrderId;
    private Boolean payStatus;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;
    
    private String payChannelCode;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cancelTime;
    
    private Integer totalPrice;
    private Integer discountPrice;
    private Integer deliveryPrice;
    private Integer adjustPrice;
    private Integer payPrice;
    private Integer deliveryType;
    private Long logisticsId;
    private String logisticsNo;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;
    
    private String receiverName;
    private String receiverMobile;
    private Integer receiverAreaId;
    private String receiverDetailAddress;
    private Long pickUpStoreId;
    private String pickUpVerifyCode;
    private Integer refundStatus;
    private Integer refundPrice;
    private Long couponId;
    private Integer couponPrice;
    private Integer usePoint;
    private Integer pointPrice;
    private Integer givePoint;
    private Integer refundPoint;
    private Integer vipPrice;
    private String giveCouponTemplateCounts;
    private String giveCouponIds;
    private Long seckillActivityId;
    private Long bargainActivityId;
    private Long bargainRecordId;
    private Long combinationActivityId;
    private Long combinationHeadId;
    private Long combinationRecordId;
    private Long pointActivityId;
    private String creator;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    private String updater;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    private Boolean deleted;
    private Long tenantId;
    private Integer appointStoreId;
    private String printStatus;
    private Integer orderType;
}