package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 货品表
 */
@Data
@Schema(description = "货品表")
public class Goods implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "货品ID")
    private Long id;

    @Schema(description = "货品名称")
    private String name;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "成本价")
    private BigDecimal costPrice;

    @Schema(description = "供应商")
    private String supplier;

    @Schema(description = "类型: 0-原料 1-半成品 2-包装")
    private Integer type;

    @Schema(description = "状态: 0-禁用 1-正常")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
