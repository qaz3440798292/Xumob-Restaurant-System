package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 餐品配方表（半成品组成）
 */
@Data
@Schema(description = "餐品配方表")
public class FoodRecipe implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "餐品ID")
    private Long foodId;

    @Schema(description = "货品ID(半成品)")
    private Long ingredientId;

    @Schema(description = "用量")
    private BigDecimal quantity;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
