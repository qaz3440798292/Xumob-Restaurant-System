package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 餐品表
 */
@Data
@Schema(description = "餐品表")
public class Food implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "餐品ID")
    private Long id;

    @Schema(description = "餐品名称")
    private String name;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "图片URL")
    private String image;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "状态: 0-锁键 1-解锁 2-上架 3-下架")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
