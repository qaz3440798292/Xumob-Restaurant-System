package cn.xumob.restaurant.security.filter;

import cn.xumob.restaurant.security.CustomUserDetailsService;
import cn.xumob.restaurant.service.RedisTokenService;
import cn.xumob.restaurant.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器
 * 
 * 验证流程：
 * 1. 从请求头提取 Token
 * 2. 检查 X-Login-Type 是否存在，不存在则抛出异常
 * 3. JWT 格式验证 + Redis 有效性验证（双重验证）
 * 4. 设置认证信息到 SecurityContext
 * 
 * 所有异常由全局异常处理器统一处理
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final RedisTokenService redisTokenService;

    // 不进行 Token 验证的路径
    private static final String[] EXCLUDE_PATHS = {
        "/api/v1/auth/login",
        "/api/v1/auth/register",
        "/api/v1/auth/public"
    };

    public JwtAuthenticationFilter(CustomUserDetailsService userDetailsService, RedisTokenService redisTokenService) {
        this.userDetailsService = userDetailsService;
        this.redisTokenService = redisTokenService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        for (String excludePath : EXCLUDE_PATHS) {
            if (path.startsWith(excludePath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. 获取 Token
        String token = getTokenFromRequest(request);

        // 有 Token 才进行验证
        if (StringUtils.hasText(token)) {
            // 2. 获取登录类型，必须存在
            String loginType = request.getHeader("X-Login-Type");
            if (!StringUtils.hasText(loginType)) {
                throw new BadCredentialsException("账号验证类型不是有效的");
            }

            // 3. 验证 Token
            if (!JwtUtil.validateToken(token)) {
                throw new BadCredentialsException("Token无效或已过期");
            }

            if (!redisTokenService.validateAccessToken(token)) {
                throw new BadCredentialsException("Token已失效，请重新登录");
            }

            // 4. 获取用户名并加载用户
            String username = JwtUtil.getUsernameFromToken(token);
            if (username == null) {
                throw new BadCredentialsException("Token无效，无法获取用户信息");
            }

            // 5. 根据登录类型加载用户
            UserDetails userDetails = userDetailsService.loadUserByUsername(username, loginType);

            // 6. 创建认证令牌并设置到 SecurityContext
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
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
