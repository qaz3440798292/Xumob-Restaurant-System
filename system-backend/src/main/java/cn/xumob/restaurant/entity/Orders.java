package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单表
 */
@Data
@Schema(description = "订单表")
public class Orders implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单号(18位数字英文组合)")
    private String orderNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "订单类型: 0-外带 1-堂食 2-外卖")
    private Integer type;

    @Schema(description = "订单状态: 0-待支付 1-已支付 2-进行中 3-已完成 4-已取消")
    private Integer status;

    @Schema(description = "订单总价")
    private BigDecimal totalPrice;

    @Schema(description = "实付金额")
    private BigDecimal actualPrice;

    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;

    @Schema(description = "优惠券ID")
    private Long couponId;

    @Schema(description = "骑手ID")
    private Long riderId;

    @Schema(description = "桌号(堂食用)")
    private String tableNumber;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "完成时间")
    private LocalDateTime completeTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
