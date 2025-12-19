package com.cloudprint.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TradePrintDevice {
    private Long id;
    private Long storeId;
    private String name;
    private String type;
    private String model;
    private String status;
    private String location;
    private String address;
    private String connectionType;
    private Integer port;
    private String paperStatus;
    private Integer inkLevel;
    private Integer queueCount;
    private Integer todayCount;
    private Integer successRate;
    private Boolean isLocal;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastConnectTime;
    
    private String remark;
    private Boolean deleted;
    private String creator;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    private String updater;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}