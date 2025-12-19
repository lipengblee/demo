package com.cloudprint.util;

/**
 * 区域类型枚举
 */
public enum AreaTypeEnum {
    COUNTRY(1, "国家"),
    PROVINCE(2, "省份"),
    CITY(3, "城市"),
    DISTRICT(4, "区县");
    
    private final Integer type;
    private final String description;
    
    AreaTypeEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }
    
    public Integer getType() {
        return type;
    }
    
    public String getDescription() {
        return description;
    }
}
