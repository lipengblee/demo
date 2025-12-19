package com.cloudprint.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TradePrintQueue {
    private Long id;
    private Long orderId;
    private String orderNo;
    private Long deviceId;
    private Integer queueIndex;
    private String status;
    private Integer priority;
    private String documentTitle;
    private String documentUrl;
    private String documentType;
    private Integer pages;
    private Integer totalPages;
    private Integer currentPage;
    private Integer copies;
    private String customerName;
    private Integer progress;
    private String errorMessage;
    private Integer retryCount;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date estimatedCompleteTime;
    
    private String remark;
    private Boolean deleted;
    private String creator;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    private String updater;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}