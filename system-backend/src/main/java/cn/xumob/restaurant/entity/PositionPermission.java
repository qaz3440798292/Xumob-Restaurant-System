package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 职位权限关联表
 */
@Data
@Schema(description = "职位权限关联表")
public class PositionPermission implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "职位ID")
    private Long positionId;

    @Schema(description = "权限ID")
    private Long permissionId;
}
