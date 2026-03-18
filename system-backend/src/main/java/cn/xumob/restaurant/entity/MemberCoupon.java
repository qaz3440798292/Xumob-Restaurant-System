package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员优惠券表
 */
@Data
@Schema(description = "会员优惠券表")
public class MemberCoupon implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "优惠券ID")
    private Long couponId;

    @Schema(description = "状态: 0-未使用 1-已使用 2-已过期")
    private Integer status;

    @Schema(description = "使用时间")
    private LocalDateTime useTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
