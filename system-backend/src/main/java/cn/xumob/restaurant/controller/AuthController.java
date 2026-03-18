package cn.xumob.restaurant.controller;

import cn.xumob.restaurant.dto.LoginDTO;
import cn.xumob.restaurant.security.CustomUserDetailsService;
import cn.xumob.restaurant.security.SecurityUser;
import cn.xumob.restaurant.vo.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 认证接口
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "认证管理")
@RequiredArgsConstructor
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 登录
     * 
     * POST /api/v1/auth/login
     * Header: X-Login-Type: EMPLOYEE / RIDER / CUSTOMER
     * Body: {"username": "xxx", "password": "xxx"}
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public ResultVO<?> login(@RequestHeader("X-Login-Type") String loginType,
                            @Valid @RequestBody LoginDTO loginDTO,
                            HttpServletRequest request) {
        try {
            // 1. 根据登录类型加载用户
            SecurityUser user = (SecurityUser) userDetailsService.loadUserByUsername(
                    loginDTO.getUsername(), loginType);

            // 2. 验证密码
            if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                return ResultVO.unauthorized("密码错误");
            }

            // 3. 设置 SecurityContext（用于后续权限验证）
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    user.getPassword(),
                    user.getAuthorities()
            );
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);
            request.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", context);

            // 4. 返回用户信息
            return ResultVO.success("登录成功");
        } catch (AuthenticationException e) {
            return ResultVO.unauthorized("账号不存在或密码错误");
        }
    }
}
