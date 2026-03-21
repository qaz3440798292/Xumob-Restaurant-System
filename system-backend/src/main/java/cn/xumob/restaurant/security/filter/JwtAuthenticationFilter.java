package cn.xumob.restaurant.security.filter;

import cn.xumob.restaurant.security.CustomUserDetailsService;
import cn.xumob.restaurant.security.SecurityUser;
import cn.xumob.restaurant.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器 - 从请求头中提取 Token 并验证
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. 从请求头获取 Token
        String token = getTokenFromRequest(request);

        // 2. 验证 Token 并设置认证信息
        if (StringUtils.hasText(token) && JwtUtil.validateToken(token)) {
            // 获取用户名
            String username = JwtUtil.getUsernameFromToken(token);
            
            if (username != null) {
                // 获取登录类型（从请求头）
                String loginType = request.getHeader("X-Login-Type");
                if (loginType == null || loginType.isEmpty()) {
                    loginType = "CUSTOMER";
                }

                try {
                    // 根据登录类型加载用户
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username, loginType);

                    // 创建认证令牌
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 设置到 SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception e) {
                    // 用户不存在或 Token 无效，不设置认证
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头获取 Token
     * 格式: Authorization: Bearer <token>
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
