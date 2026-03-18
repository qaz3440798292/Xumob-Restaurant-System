package cn.xumob.restaurant.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.Collection;

/**
 * 安全用户详情 - 携带身份和职位信息
 */
@Getter
public class SecurityUser extends User {

    /**
     * 用户ID
     */
    private final Long userId;

    /**
     * 身份类型: customer(顾客), employee(员工), rider(骑手)
     */
    private final String identityType;

    /**
     * 员工ID（仅员工身份有值）
     */
    private final Long employeeId;

    /**
     * 职位ID（仅员工身份有值）
     */
    private final Long positionId;

    /**
     * 职位代码
     */
    private final String positionCode;

    /**
     * 职位名称
     */
    private final String positionName;

    /**
     * 职级（越大越高）
     */
    private final Integer positionLevel;

    public SecurityUser(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            Long userId,
            String identityType,
            Long employeeId,
            Long positionId,
            String positionCode,
            String positionName,
            Integer positionLevel
    ) {
        super(username, password, authorities);
        this.userId = userId;
        this.identityType = identityType;
        this.employeeId = employeeId;
        this.positionId = positionId;
        this.positionCode = positionCode;
        this.positionName = positionName;
        this.positionLevel = positionLevel;
    }

    /**
     * 判断是否是员工
     */
    public boolean isEmployee() {
        return "employee".equals(identityType);
    }

    /**
     * 判断是否是骑手
     */
    public boolean isRider() {
        return "rider".equals(identityType);
    }

    /**
     * 判断是否是顾客
     */
    public boolean isCustomer() {
        return "customer".equals(identityType);
    }

    /**
     * 判断是否拥有指定职位
     */
    public boolean hasPosition(String... positionCodes) {
        if (positionCode == null) {
            return false;
        }
        return Arrays.asList(positionCodes).contains(positionCode);
    }

    /**
     * 判断是否拥有指定角色
     */
    public boolean hasRole(String role) {
        return getAuthorities().contains(new org.springframework.security.core.authority.SimpleGrantedAuthority(role));
    }
}
