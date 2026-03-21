package cn.xumob.restaurant.security.annotation;

import java.lang.annotation.*;

/**
 * 职位级别要求注解
 * 
 * 标记接口需要的最低职位级别
 * 高级别职位自动包含低级别职位的权限
 * 
 * @example
 * @RequiresPositionLevel(level = 3)  // 需要值班主管(level=3)及以上
 * public void manageShift() { }
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPositionLevel {
    
    /**
     * 需要的最低职位级别
     * 值越小级别越低，值越大级别越高
     */
    int level() default 0;
    
    /**
     * 职位代码（可选，精确匹配）
     * 如果设置了 code，则同时检查职位代码
     */
    String code() default "";
}
