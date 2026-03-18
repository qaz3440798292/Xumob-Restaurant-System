package cn.xumob.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工表
 */
@Data
@Schema(description = "员工表")
public class Employee implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "员工ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "职位ID")
    private Long positionId;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "薪资")
    private BigDecimal salary;

    @Schema(description = "入职日期")
    private LocalDate hireDate;

    @Schema(description = "用工类型: 0-全职 1-兼职")
    private Integer employmentType;

    @Schema(description = "状态: 0-离职 1-在职")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
