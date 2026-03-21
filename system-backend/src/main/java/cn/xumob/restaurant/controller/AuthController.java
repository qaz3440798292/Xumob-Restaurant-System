package cn.xumob.restaurant.controller;

import cn.xumob.restaurant.config.RsaKeyPair;
import cn.xumob.restaurant.dto.LoginDTO;
import cn.xumob.restaurant.dto.RegisterDTO;
import cn.xumob.restaurant.security.CustomUserDetailsService;
import cn.xumob.restaurant.security.SecurityUser;
import cn.xumob.restaurant.service.AuthService;
import cn.xumob.restaurant.service.RedisTokenService;
import cn.xumob.restaurant.util.JwtUtil;
import cn.xumob.restaurant.vo.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 认证接口
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "认证管理")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTokenService redisTokenService;
    private final RsaKeyPair rsaKeyPair;
    private final AuthService authService;

    // 是否需要加密（生产模式需要，开发模式不需要）
    @Value("${app.encryption.required:true}")
    private boolean encryptionRequired;

    /**
     * 获取 RSA 公钥
     * 
     * GET /api/v1/auth/public-key
     */
    @GetMapping("/public-key")
    @Operation(summary = "获取RSA公钥")
    public ResultVO<String> getPublicKey() {
        return ResultVO.success("获取成功", rsaKeyPair.getPublicKey());
    }

    /**
     * 登录
     * 
     * POST /api/v1/auth/login
     * Header: X-Login-Type: EMPLOYEE / RIDER / CUSTOMER
     * Body: {"username": "xxx", "password": "xxx"}
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public ResultVO<?> login(
            @RequestHeader("X-Login-Type") String loginType,
            @Valid @RequestBody LoginDTO loginDTO) {

        // 1. 根据登录类型加载用户
        SecurityUser user = (SecurityUser) userDetailsService.loadUserByUsername(
                loginDTO.getUsername(), loginType);

        // 2. 处理密码
        String passwordToVerify = loginDTO.getPassword();
        
        if (encryptionRequired) {
            // 生产模式：密码必须是加密的（RSA加密后Base64长度 > 100）
            if (passwordToVerify.length() < 100) {
                return ResultVO.unauthorized("密码传输格式错误，请使用加密传输");
            }
            passwordToVerify = rsaKeyPair.decrypt(passwordToVerify);
        }
        // 开发模式：直接使用原文

        // 3. 验证密码
        if (!passwordEncoder.matches(passwordToVerify, user.getPassword())) {
            return ResultVO.unauthorized("密码错误");
        }

        // 3. 生成 Token
        String accessToken = JwtUtil.generateAccessToken(user.getUserId(), user.getUsername());
        String refreshToken = JwtUtil.generateRefreshToken(user.getUserId(), user.getUsername());

        // 4. 存储 Token 到 Redis（新 token 覆盖旧 token，实现单点登录）
        redisTokenService.storeAccessToken(user.getUserId(), user.getUsername(), loginType, accessToken);
        redisTokenService.storeRefreshToken(user.getUserId(), user.getUsername(), loginType, refreshToken);

        // 5. 返回 Token 和用户信息
        return ResultVO.success("登录成功", java.util.Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册 - 发布正式版本后，注册接口只能注册顾客账号，餐厅员工和骑手账号只能在后台添加")
    public ResultVO<String> register(@RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO, encryptionRequired);
        return ResultVO.success("注册成功");
    }

    /**
     * 刷新 Token
     * 
     * POST /api/v1/auth/refresh
     * Header: X-Login-Type: EMPLOYEE / RIDER / CUSTOMER
     *       Authorization: Bearer <refreshToken>
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新Token")
    public ResultVO<?> refresh(@RequestHeader("X-Login-Type") String loginType,
                              @RequestHeader("Authorization") String authorization) {
        // 从 Authorization 头提取 refreshToken
        String refreshToken = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            refreshToken = authorization.substring(7);
        }
        
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResultVO.paramError("refreshToken不能为空");
        }

        // 验证 refreshToken（JWT 格式 + Redis 验证）
        if (!JwtUtil.validateToken(refreshToken) || !redisTokenService.validateRefreshToken(refreshToken)) {
            return ResultVO.unauthorized("Token无效或已过期");
        }

        // 从 Redis 获取 Token 信息
        RedisTokenService.TokenInfo tokenInfo = redisTokenService.getRefreshTokenInfo(refreshToken);
        if (tokenInfo == null) {
            return ResultVO.unauthorized("Token无效或已过期");
        }

        // 验证登录类型匹配
        if (!tokenInfo.getLoginType().equals(loginType)) {
            return ResultVO.unauthorized("登录类型不匹配");
        }

        try {
            // 重新加载用户
            SecurityUser user = (SecurityUser) userDetailsService.loadUserByUsername(
                    tokenInfo.getUsername(), loginType);

            // 生成新 Token
            String newAccessToken = JwtUtil.generateAccessToken(user.getUserId(), user.getUsername());
            String newRefreshToken = JwtUtil.generateRefreshToken(user.getUserId(), user.getUsername());

            // 使旧 RefreshToken 失效（可选，防止 refresh token 重放攻击）
            redisTokenService.invalidateRefreshToken(refreshToken);

            // 存储新 Token 到 Redis
            redisTokenService.storeAccessToken(user.getUserId(), user.getUsername(), loginType, newAccessToken);
            redisTokenService.storeRefreshToken(user.getUserId(), user.getUsername(), loginType, newRefreshToken);

            return ResultVO.success("刷新成功", java.util.Map.of(
                    "accessToken", newAccessToken,
                    "refreshToken", newRefreshToken
            ));
        } catch (AuthenticationException e) {
            return ResultVO.unauthorized("账号不存在");
        }
    }
}
