package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单配送状态表
 */
@Data
@Schema(description = "订单配送状态表")
public class OrderDelivery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "骑手到店时间")
    private LocalDateTime riderArriveTime;

    @Schema(description = "开始出餐时间")
    private LocalDateTime cookingStartTime;

    @Schema(description = "开始送餐时间")
    private LocalDateTime deliveryStartTime;

    @Schema(description = "送达时间")
    private LocalDateTime deliveryCompleteTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
