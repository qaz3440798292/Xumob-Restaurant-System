package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 骑手表
 */
@Data
@Schema(description = "骑手表")
public class Rider implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "骑手ID")
    private Long id;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "状态: 0-下线 1-上线")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
