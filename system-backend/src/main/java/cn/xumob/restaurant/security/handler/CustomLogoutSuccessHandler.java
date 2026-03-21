package cn.xumob.restaurant.security.handler;

import cn.xumob.restaurant.service.RedisTokenService;
import cn.xumob.restaurant.util.JwtUtil;
import cn.xumob.restaurant.vo.ResultVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 登出成功处理器
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final RedisTokenService redisTokenService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication auth) throws IOException, ServletException {
        // 从请求头获取 Token
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            
            // 从 Redis 删除 Token
            redisTokenService.invalidateAccessToken(token);
            log.info("用户登出，Token 已从 Redis 中移除");
        }

        ResultVO<String> result = ResultVO.success("登出成功");

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
