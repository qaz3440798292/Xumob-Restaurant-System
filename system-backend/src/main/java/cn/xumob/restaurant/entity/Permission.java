package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限表
 */
@Data
@Schema(description = "权限表")
public class Permission implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "权限ID")
    private Long id;

    @Schema(description = "权限名称")
    private String name;

    @Schema(description = "权限代码")
    private String code;

    @Schema(description = "权限描述")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
