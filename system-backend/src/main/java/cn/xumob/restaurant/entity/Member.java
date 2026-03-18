package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员表
 */
@Data
@Schema(description = "会员表")
public class Member implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "会员ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "会员类型ID")
    private Long memberTypeId;

    @Schema(description = "会员卡号")
    private String cardNumber;

    @Schema(description = "账户余额")
    private BigDecimal balance;

    @Schema(description = "积分")
    private Integer points;

    @Schema(description = "状态: 0-禁用 1-正常")
    private Integer status;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
