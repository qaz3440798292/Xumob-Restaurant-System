package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 职位表
 */
@Data
@Schema(description = "职位表")
public class Position implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "职位ID")
    private Long id;

    @Schema(description = "职位名称")
    private String name;

    @Schema(description = "职位描述")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
