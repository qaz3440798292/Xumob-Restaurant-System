package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员类型表
 */
@Data
@Schema(description = "会员类型表")
public class MemberType implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "会员类型ID")
    private Long id;

    @Schema(description = "类型名称")
    private String name;

    @Schema(description = "折扣率")
    private BigDecimal discount;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
