package com.star.lp.module.trade.dal.dataobject.print;

import com.star.lp.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@TableName("print_document")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrintDocumentDO extends BaseDO {

    @TableId
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @Schema(description = "文件名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileName;

    @Schema(description = "文件存储路径", requiredMode = Schema.RequiredMode.REQUIRED)
    private String filePath;

    @Schema(description = "文件大小", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long fileSize;

    @Schema(description = "文件类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileType;

    @Schema(description = "页数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageCount;

    @Schema(description = "上传时间")
    private LocalDateTime uploadTime;

    @Schema(description = "状态")
    private Integer status;
}
