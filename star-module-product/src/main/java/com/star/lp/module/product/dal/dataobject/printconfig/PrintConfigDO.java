package com.star.lp.module.product.dal.dataobject.printconfig;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.star.lp.framework.mybatis.core.dataobject.BaseDO;

/**
 * 打印设置配置 DO
 *
 * @author LIPENG
 */
@TableName("product_print_config")
@KeySequence("product_print_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrintConfigDO extends BaseDO {

    /**
     * 配置编号
     */
    @TableId
    private Long id;
    /**
     * 配置键
     */
    private String configKey;
    /**
     * 配置值
     */
    private String configValue;
    /**
     * 配置描述
     */
    private String configDesc;
    /**
     * 状态：1-启用 0-禁用
     */
    private Integer status;


}