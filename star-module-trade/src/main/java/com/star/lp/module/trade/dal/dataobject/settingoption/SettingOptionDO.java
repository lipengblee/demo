package com.star.lp.module.trade.dal.dataobject.settingoption;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.star.lp.framework.mybatis.core.dataobject.BaseDO;

/**
 * 打印设置选项 DO
 *
 * @author LIPENG
 */
@TableName("print_setting_option")
@KeySequence("print_setting_option_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingOptionDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 选项名称（如：纸型、色彩）
     */
    private String name;
    /**
     * 排序（数字越小越靠前）
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;


}