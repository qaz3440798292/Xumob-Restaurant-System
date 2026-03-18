package cn.xumob.restaurant.controller;

import cn.xumob.restaurant.dto.LoginDTO;
import cn.xumob.restaurant.dto.LoginResponseDTO;
import cn.xumob.restaurant.dto.UserContext;
import cn.xumob.restaurant.util.JwtUtil;
import cn.xumob.restaurant.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;

    @Value("${jwt.expiration:86400000}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshTokenExpiration;

    @PostMapping("/login")
    public ResultVO<LoginResponseDTO> login(LoginDTO loginDTO) {
        UserContext.setType(loginDTO.getType());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(),
                loginDTO.getPassword()
        );

        Authentication authentication = authManager.authenticate(token);

        // 生成 Access Token 和 Refresh Token
        String accessToken = JwtUtil.generateAccessToken(authentication);
        String refreshToken = JwtUtil.generateRefreshToken(authentication);

        // 构建登录响应
        LoginResponseDTO response = LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenExpiration / 1000) // 转换为秒
                .refreshExpiresIn(refreshTokenExpiration / 1000) // 转换为秒
                .build();

        return ResultVO.success("登录成功", response);
    }
}
