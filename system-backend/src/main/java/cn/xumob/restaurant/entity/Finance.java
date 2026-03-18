package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 财务记录表
 */
@Data
@Schema(description = "财务记录表")
public class Finance implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "财务ID")
    private Long id;

    @Schema(description = "类型: 0-收入 1-支出")
    private Integer type;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "来源")
    private String source;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
