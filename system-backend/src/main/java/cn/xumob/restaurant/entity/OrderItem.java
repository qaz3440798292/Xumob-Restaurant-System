package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情表
 */
@Data
@Schema(description = "订单详情表")
public class OrderItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "餐品ID")
    private Long foodId;

    @Schema(description = "餐品名称")
    private String foodName;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "单价")
    private BigDecimal unitPrice;

    @Schema(description = "小计")
    private BigDecimal subtotal;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
