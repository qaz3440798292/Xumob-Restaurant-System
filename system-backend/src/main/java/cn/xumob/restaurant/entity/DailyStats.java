package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 每日销售统计表
 */
@Data
@Schema(description = "每日销售统计表")
public class DailyStats implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "统计日期")
    private LocalDate statDate;

    @Schema(description = "订单数")
    private Integer totalOrders;

    @Schema(description = "营业额")
    private BigDecimal totalSales;

    @Schema(description = "会员销售额")
    private BigDecimal memberSales;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
