package com.star.lp.module.trade.controller.admin.order.vo;

import lombok.Data;

import java.util.Date;

@Data
public class JDLogisticsOrderParamReqVo {

    // 寄件人信息
    private String senderName;
    private String senderPhone;
    private String senderAddress;

    private String orderId;

    // 收件人信息
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;

    // 产品信息
    private String productCode; // 产品编码，例如：ed-m-0001（京东标快）

    // 货品信息
    private String cargoName; // 货品名称
    private Integer cargoQuantity; // 货品数量
    private String cargoWeight; // 货品重量（kg）
    private String cargoLength; // 货品长度（cm）
    private String cargoWidth; // 货品宽度（cm）
    private String cargoHeight; // 货品高度（cm）

    // 取件时间
    private Date pickupStartTime;
    private Date pickupEndTime;

    // 门店ID
    private Long storeId;

    // getter and setter methods
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public Integer getCargoQuantity() {
        return cargoQuantity;
    }

    public void setCargoQuantity(Integer cargoQuantity) {
        this.cargoQuantity = cargoQuantity;
    }

    public String getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(String cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public String getCargoLength() {
        return cargoLength;
    }

    public void setCargoLength(String cargoLength) {
        this.cargoLength = cargoLength;
    }

    public String getCargoWidth() {
        return cargoWidth;
    }

    public void setCargoWidth(String cargoWidth) {
        this.cargoWidth = cargoWidth;
    }

    public String getCargoHeight() {
        return cargoHeight;
    }

    public void setCargoHeight(String cargoHeight) {
        this.cargoHeight = cargoHeight;
    }

    public Date getPickupStartTime() {
        return pickupStartTime;
    }

    public void setPickupStartTime(Date pickupStartTime) {
        this.pickupStartTime = pickupStartTime;
    }

    public Date getPickupEndTime() {
        return pickupEndTime;
    }

    public void setPickupEndTime(Date pickupEndTime) {
        this.pickupEndTime = pickupEndTime;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
