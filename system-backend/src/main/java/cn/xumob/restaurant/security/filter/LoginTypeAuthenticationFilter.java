package cn.xumob.restaurant.security.filter;

import cn.xumob.restaurant.security.handler.CustomAuthenticationProvider.LoginTypeWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * 自定义用户名密码认证过滤器 - 支持登录类型
 */
public class LoginTypeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public LoginTypeAuthenticationFilter(String defaultFilterProcessesUrl,
                                         AuthenticationSuccessHandler successHandler,
                                         AuthenticationFailureHandler failureHandler) {
        super(defaultFilterProcessesUrl);
        setAuthenticationSuccessHandler(successHandler);
        setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                               HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String loginType = obtainLoginType(request);

        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        if (loginType == null || loginType.isEmpty()) {
            loginType = "CUSTOMER"; // 默认顾客
        }

        // 用 LoginTypeWrapper 包装用户名
        LoginTypeWrapper principal = new LoginTypeWrapper(username, loginType);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                principal,
                password
        );

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 获取登录类型
     */
    private String obtainLoginType(HttpServletRequest request) {
        // 优先从请求头获取，其次从请求参数获取
        String loginType = request.getHeader("X-Login-Type");
        if (loginType == null || loginType.isEmpty()) {
            loginType = request.getParameter("loginType");
        }
        return loginType;
    }

    private String obtainUsername(HttpServletRequest request) {
        return request.getParameter("username");
    }

    private String obtainPassword(HttpServletRequest request) {
        return request.getParameter("password");
    }
}
