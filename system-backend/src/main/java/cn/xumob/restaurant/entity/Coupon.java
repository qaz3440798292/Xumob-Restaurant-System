package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券表
 */
@Data
@Schema(description = "优惠券表")
public class Coupon implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "优惠券ID")
    private Long id;

    @Schema(description = "优惠券名称")
    private String name;

    @Schema(description = "优惠券码")
    private String code;

    @Schema(description = "类型: 0-满减券 1-折扣券")
    private Integer type;

    @Schema(description = "优惠值(满减金额或折扣率)")
    private BigDecimal value;

    @Schema(description = "最低消费金额")
    private BigDecimal minAmount;

    @Schema(description = "优惠范围: 0-全场 1-指定商品 2-指定分类")
    private Integer targetType;

    @Schema(description = "指定商品/分类ID")
    private Long targetId;

    @Schema(description = "发放总数")
    private Integer totalCount;

    @Schema(description = "剩余数量")
    private Integer remainCount;

    @Schema(description = "指定会员类型(为空则全体可用)")
    private Long memberTypeId;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "状态: 0-禁用 1-正常")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
