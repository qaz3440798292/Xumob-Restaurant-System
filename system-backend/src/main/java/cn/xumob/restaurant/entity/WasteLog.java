package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报废上报日志表
 */
@Data
@Schema(description = "报废上报日志表")
public class WasteLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "货品ID")
    private Long goodsId;

    @Schema(description = "报废数量")
    private BigDecimal quantity;

    @Schema(description = "报废原因")
    private String reason;

    @Schema(description = "操作人ID")
    private Long operatorId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
