package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存表
 */
@Data
@Schema(description = "库存表")
public class Stock implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "货品ID")
    private Long goodsId;

    @Schema(description = "库存数量")
    private BigDecimal quantity;

    @Schema(description = "预警数量")
    private BigDecimal warningQuantity;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
