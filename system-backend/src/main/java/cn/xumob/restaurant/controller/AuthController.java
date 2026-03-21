package cn.xumob.restaurant.controller;

import cn.xumob.restaurant.dto.LoginDTO;
import cn.xumob.restaurant.security.CustomUserDetailsService;
import cn.xumob.restaurant.security.SecurityUser;
import cn.xumob.restaurant.util.JwtUtil;
import cn.xumob.restaurant.vo.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
                            @Valid @RequestBody LoginDTO loginDTO) {
        try {
            // 1. 根据登录类型加载用户
            SecurityUser user = (SecurityUser) userDetailsService.loadUserByUsername(
                    loginDTO.getUsername(), loginType);

            // 2. 验证密码
            if (!new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
                    .matches(loginDTO.getPassword(), user.getPassword())) {
                return ResultVO.unauthorized("密码错误");
            }

            // 3. 生成 Token
            String accessToken = JwtUtil.generateAccessToken(user.getUserId(), user.getUsername());
            String refreshToken = JwtUtil.generateRefreshToken(user.getUserId(), user.getUsername());

            // 4. 返回 Token 和用户信息
            return ResultVO.success("登录成功", java.util.Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            ));
        } catch (AuthenticationException e) {
            return ResultVO.unauthorized("账号不存在或密码错误");
        }
    }

    /**
     * 刷新 Token
     * 
     * POST /api/v1/auth/refresh
     * Header: X-Login-Type: EMPLOYEE / RIDER / CUSTOMER
     * Body: {"refreshToken": "xxx"}
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新Token")
    public ResultVO<?> refresh(@RequestHeader("X-Login-Type") String loginType,
                              @RequestBody java.util.Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResultVO.paramError("refreshToken不能为空");
        }

        // 验证 refreshToken
        if (!JwtUtil.validateToken(refreshToken)) {
            return ResultVO.unauthorized("Token无效或已过期");
        }

        // 从 Token 中获取用户信息
        String username = JwtUtil.getUsernameFromToken(refreshToken);
        Long userId = JwtUtil.getUserIdFromToken(refreshToken);

        if (username == null) {
            return ResultVO.unauthorized("Token无效");
        }

        try {
            // 重新加载用户
            SecurityUser user = (SecurityUser) userDetailsService.loadUserByUsername(username, loginType);

            // 生成新 Token
            String newAccessToken = JwtUtil.generateAccessToken(user.getUserId(), user.getUsername());
            String newRefreshToken = JwtUtil.generateRefreshToken(user.getUserId(), user.getUsername());

            return ResultVO.success("刷新成功", java.util.Map.of(
                    "accessToken", newAccessToken,
                    "refreshToken", newRefreshToken
            ));
        } catch (AuthenticationException e) {
            return ResultVO.unauthorized("账号不存在");
        }
    }
}
