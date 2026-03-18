package cn.xumob.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 注册请求 DTO
 */
@Data
@Schema(description = "注册请求")
public class RegisterDTO {

    @NotBlank(message = "账号不能为空")
    @Schema(description = "账号", required = true)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", required = true)
    private String password;

    @NotNull(message = "注册类型不能为空")
    @Schema(description = "注册类型: CUSTOMER(顾客), RIDER(骑手), EMPLOYEE(员工)", required = true)
    private String registerType;

    // 顾客/骑手/员工 通用字段
    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "姓名")
    private String name;

    // 员工特有字段
    @Schema(description = "员工-职位ID")
    private Long positionId;

    @Schema(description = "员工-身份证号")
    private String idCard;

    @Schema(description = "员工-入职日期")
    private String hireDate;

    @Schema(description = "员工-用工类型: 0-全职 1-兼职")
    private Integer employmentType;
}
