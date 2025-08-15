package cn.iocoder.yudao.module.trade.dal.dataobject.settingoption;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 打印设置选项值 DO
 *
 * @author LIPENG
 */
@TableName("print_setting_option_value")
@KeySequence("print_setting_option_value_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingOptionValueDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 关联选项ID（print_setting_option.id）
     */
    private Long optionId;
    /**
     * 选项值（如：A4、激光黑白）
     */
    private String value;
    /**
     * 价格（单位：元）
     */
    private BigDecimal price;
    /**
     * 排序（数字越小越靠前）
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;

}