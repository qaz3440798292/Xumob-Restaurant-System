package cn.xumob.restaurant.config;

import cn.xumob.restaurant.security.CustomUserDetailsService;
import cn.xumob.restaurant.security.filter.JwtAuthenticationFilter;
import cn.xumob.restaurant.security.handler.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security 配置
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS 配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "X-Login-Type"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 安全过滤链配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF（前后端分离）
                .csrf(AbstractHttpConfigurer::disable)

                // 启用 CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 不使用 Session（无状态）
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 配置请求授权
                .authorizeHttpRequests(auth -> auth
                        // 公开接口
                        .requestMatchers(
                                "/api/v1/auth/login",
                                "/api/v1/auth/register",
                                "/api/v1/auth/refresh",
                                "/api/v1/auth/public/**",
                                "/api/v1/auth/public-key",
                                "/error"
                        ).permitAll()

                        // 顾客相关接口
                        .requestMatchers("/api/v1/customers/**").hasRole("CUSTOMER")

                        // 骑手相关接口
                        .requestMatchers("/api/v1/riders/**").hasRole("RIDER")

                        // 员工后台接口 - 需要员工身份
                        .requestMatchers("/api/v1/admin/**").hasRole("EMPLOYEE")

                        // Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // 其他请求需要认证
                        .anyRequest().authenticated()
                )

                // 禁用默认 formLogin
                .formLogin(form -> form.disable())

                // 添加 JWT 认证过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // 登出配置
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessHandler(logoutSuccessHandler)
                )

                // 异常处理
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                );

        return http.build();
    }
}
