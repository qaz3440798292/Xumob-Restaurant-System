package cn.xumob.restaurant.security.handler;

import cn.xumob.restaurant.security.SecurityUser;
import cn.xumob.restaurant.vo.ResultVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录成功处理器
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Authentication auth) throws IOException, ServletException {
        SecurityUser user = (SecurityUser) auth.getPrincipal();

        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("userId", user.getUserId());
        data.put("identityType", user.getIdentityType());
        data.put("employeeId", user.getEmployeeId());
        data.put("positionId", user.getPositionId());
        data.put("positionCode", user.getPositionCode());
        data.put("positionName", user.getPositionName());
        data.put("positionLevel", user.getPositionLevel());
        data.put("authorities", user.getAuthorities());

        ResultVO<Map<String, Object>> result = ResultVO.success("登录成功", data);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
