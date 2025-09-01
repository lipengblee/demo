package com.star.lp.module.trade.dal.dataobject.print;


import com.baomidou.mybatisplus.annotation.TableName;

import com.star.lp.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@TableName("print_order_item")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrintOrderItemDO extends BaseDO {

    @TableId
    private Long id;

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long orderId;

    @Schema(description = "文档ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long documentId;

    @Schema(description = "打印配置ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long configId;

    @Schema(description = "打印份数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer copies;

    @Schema(description = "页数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageCount;

    @Schema(description = "单页价格", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal unitPrice;

    @Schema(description = "总价格", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalPrice;

    @Schema(description = "打印状态")
    private Integer printStatus;

    @Schema(description = "开始打印时间")
    private LocalDateTime printStartTime;

    @Schema(description = "完成打印时间")
    private LocalDateTime printEndTime;
}
