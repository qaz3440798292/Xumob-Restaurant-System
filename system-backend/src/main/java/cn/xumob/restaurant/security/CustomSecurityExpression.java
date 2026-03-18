package cn.xumob.restaurant.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 自定义权限表达式 - 用于 @PreAuthorize 注解
 * 
 * 使用方式: 
 * - @PreAuthorize("@ss.hasPosition('QSC_SHIFT_LEADER')")  // 必须是值班主管
 * - @PreAuthorize("@ss.hasPositionAbove('QSC_SHIFT_LEADER')")  // 值班主管及以上
 * - @PreAuthorize("@ss.hasPositionLevel(3)")  // 职级>=3
 */
@Component("ss")
public class CustomSecurityExpression {

    /**
     * 判断是否拥有指定职位
     * 
     * @param positionCodes 职位代码
     * @return true 表示拥有指定职位之一
     */
    public boolean hasPosition(String... positionCodes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof SecurityUser)) {
            return false;
        }

        SecurityUser user = (SecurityUser) auth.getPrincipal();
        return user.hasPosition(positionCodes);
    }

    /**
     * 判断是否拥有指定职位或更高职级
     * 例如: 传入 QSC_SHIFT_LEADER (3级)，则 CREW_TRAINER (2级) 返回 false，QSC_SHIFT_LEADER (3级) 及以上返回 true
     * 
     * @param positionCode 职位代码
     * @return true 表示拥有指定职位或更高职级
     */
    public boolean hasPositionAbove(String positionCode) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof SecurityUser)) {
            return false;
        }

        SecurityUser user = (SecurityUser) auth.getPrincipal();
        Integer currentLevel = user.getPositionLevel();
        Integer requiredLevel = getPositionLevel(positionCode);
        
        if (currentLevel == null || requiredLevel == null) {
            return false;
        }
        
        return currentLevel >= requiredLevel;
    }

    /**
     * 判断职级是否大于等于指定值
     * 
     * @param level 职级
     * @return true 表示职级大于等于指定值
     */
    public boolean hasPositionLevel(int level) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof SecurityUser)) {
            return false;
        }

        SecurityUser user = (SecurityUser) auth.getPrincipal();
        Integer currentLevel = user.getPositionLevel();
        
        return currentLevel != null && currentLevel >= level;
    }

    /**
     * 判断是否是员工
     */
    public boolean isEmployee() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof SecurityUser)) {
            return false;
        }
        return ((SecurityUser) auth.getPrincipal()).isEmployee();
    }

    /**
     * 判断是否是骑手
     */
    public boolean isRider() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof SecurityUser)) {
            return false;
        }
        return ((SecurityUser) auth.getPrincipal()).isRider();
    }

    /**
     * 判断是否是顾客
     */
    public boolean isCustomer() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof SecurityUser)) {
            return false;
        }
        return ((SecurityUser) auth.getPrincipal()).isCustomer();
    }

    /**
     * 判断是否拥有指定角色
     */
    public boolean hasAnyRole(String... roles) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof SecurityUser)) {
            return false;
        }

        SecurityUser user = (SecurityUser) auth.getPrincipal();
        for (String role : roles) {
            if (user.hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取职级（供内部使用）
     */
    private Integer getPositionLevel(String positionCode) {
        return switch (positionCode) {
            case "CREW" -> 1;
            case "CREW_TRAINER" -> 2;
            case "QSC_SHIFT_LEADER" -> 3;
            case "CUSTOMER_EXPERIENCE_MANAGER" -> 4;
            case "RESTAURANT_GENERAL_MANAGER" -> 5;
            default -> null;
        };
    }
}
