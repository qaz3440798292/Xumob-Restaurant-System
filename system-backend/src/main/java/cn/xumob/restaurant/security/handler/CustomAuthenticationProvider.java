package cn.xumob.restaurant.security.handler;

import cn.xumob.restaurant.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义认证提供者 - 支持多登录类型
 */
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        // 从 principal 中获取登录类型
        String loginType = "CUSTOMER";
        if (authentication.getPrincipal() instanceof LoginTypeWrapper) {
            loginType = ((LoginTypeWrapper) authentication.getPrincipal()).getLoginType();
        }

        // 根据登录类型加载用户
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username, loginType);
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("账号不存在");
        }

        // 验证密码
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }

        // 创建认证令牌
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                password,
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 登录类型包装类 - 用于传递登录类型
     */
    public static class LoginTypeWrapper {
        private final String username;
        private final String loginType;

        public LoginTypeWrapper(String username, String loginType) {
            this.username = username;
            this.loginType = loginType;
        }

        public String getUsername() {
            return username;
        }

        public String getLoginType() {
            return loginType;
        }
    }
}
