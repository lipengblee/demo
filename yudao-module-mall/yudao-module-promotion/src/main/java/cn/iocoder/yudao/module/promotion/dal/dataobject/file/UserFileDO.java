package cn.iocoder.yudao.module.promotion.dal.dataobject.file;

import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 文件 DO
 *
 * @author LIPENG
 */
@TableName("infra_file")
@KeySequence("infra_file_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TenantIgnore
public class UserFileDO extends BaseDO {

    /**
     * 文件编号
     */
    @TableId
    private Long id;
    /**
     * 配置编号
     */
    private Long configId;
    /**
     * 文件名
     */
    private String name;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 文件 URL
     */
    private String url;
    /**
     * 文件类型
     */
    private String type;
    /**
     * 文件大小
     */
    private Integer size;
    /**
     * 文件类型
     */
    private String creator;


}