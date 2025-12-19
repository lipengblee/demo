package com.cloudprint.model;

import lombok.Data;
import java.sql.Timestamp;

/**
 * 自提门店实体类
 */
@Data
public class TradeDeliveryPickUpStore {
    private Long id;
    private String name;
    private String introduction;
    private String phone;
    private Integer areaId;
    private String detailAddress;
    private String logo;
    private String openingTime;
    private String closingTime;
    private Double latitude;
    private Double longitude;
    private String verifyUserIds;
    private Integer status;
    private String creator;
    private Timestamp createTime;
    private String updater;
    private Timestamp updateTime;
    private Boolean deleted;
    private Long tenantId;
    private String storeOperationUserIds;
    private String contact;
}
