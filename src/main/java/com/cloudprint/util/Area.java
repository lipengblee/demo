package com.cloudprint.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 区域实体类
 */
@Data
@AllArgsConstructor
public class Area {
    public static final Integer ID_GLOBAL = 0;
    public static final Integer ID_CHINA = 1;
    
    private Integer id;
    private String name;
    private Integer type;
    private Area parent;
    private List<Area> children;
}
