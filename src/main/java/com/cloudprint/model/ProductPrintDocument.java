package com.cloudprint.model;

import lombok.Data;

import java.util.Date;

@Data
public class ProductPrintDocument {
    private Long id;
    private Long userId;
    private String name;
    private String originalName;
    private String fileType;
    private Long fileSize;
    private String fileUrl;
    private String transferPdfFileUrl;
    private String thumbnailUrl;
    private Integer pageCount;
    private Integer status;
    private String fileHash;
    private String remark;
    private String creator;
    private Date createTime;
    private String updater;
    private Date updateTime;
    private Boolean deleted;
    private Long tenantId;
    private Integer userSoftDelete;
}