package cn.xumob.restaurant.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
@Component
public class JwtUtil {

    private static String secret;

    private static Long expiration; // 默认 24 小时

    private static Long refreshExpiration; // 默认 7 天

    @Value("${jwt.secret:xumob-restaurant-system-secret-key-2026-xumob}")
    public void setSecret(String secretValue) {
        secret = secretValue;
    }

    @Value("${jwt.expiration:86400000}")
    public void setExpiration(Long expirationValue) {
        expiration = expirationValue;
    }

    @Value("${jwt.refresh-expiration:604800000}")
    public void setRefreshExpiration(Long refreshExpirationValue) {
        refreshExpiration = refreshExpirationValue;
    }

    /**
     * 获取签名密钥
     */
    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 生成访问令牌 (Access Token)
     *
     * @param userId 用户 ID
     * @param username 用户名
     * @return JWT token
     */
    public static String generateAccessToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("type", "access");
        return createToken(claims, username, expiration);
    }

    /**
     * 生成访问令牌 (Access Token) - 根据 Authentication 对象
     *
     * @param authentication Spring Security Authentication 对象
     * @return JWT token
     */
    public static String generateAccessToken(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("Authentication cannot be null");
        }

        // 从 Authentication 中提取用户信息
        Object principal = authentication.getPrincipal();
        Long userId = null;
        String username = null;

        // 如果 Principal 是自定义用户对象，尝试提取信息
        if (principal instanceof cn.xumob.restaurant.entity.SysUser) {
            cn.xumob.restaurant.entity.SysUser user = (cn.xumob.restaurant.entity.SysUser) principal;
            userId = user.getId();
            username = user.getUsername();
        } else if (principal instanceof org.springframework.security.core.userdetails.User) {
            // 如果是 Spring Security 默认的 User 对象
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principal;
            username = user.getUsername();
            // 注意：默认的 User 对象没有 ID，需要从其他地方获取
        } else if (principal instanceof String) {
            // 如果 Principal 是字符串（用户名）
            username = (String) principal;
        } else {
            // 尝试通过反射或其他方式获取
            username = authentication.getName(); // 获取用户名
        }

        if (username == null) {
            throw new IllegalArgumentException("Cannot extract username from Authentication");
        }

        return generateAccessToken(userId, username);
    }

    /**
     * 生成刷新令牌 (Refresh Token)
     *
     * @param userId 用户 ID
     * @param username 用户名
     * @return JWT token
     */
    public static String generateRefreshToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("type", "refresh");
        return createToken(claims, username, refreshExpiration);
    }

    /**
     * 生成刷新令牌 (Refresh Token) - 根据 Authentication 对象
     *
     * @param authentication Spring Security Authentication 对象
     * @return JWT token
     */
    public static String generateRefreshToken(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("Authentication cannot be null");
        }

        // 从 Authentication 中提取用户信息
        Object principal = authentication.getPrincipal();
        Long userId = null;
        String username = null;

        // 如果 Principal 是自定义用户对象，尝试提取信息
        if (principal instanceof cn.xumob.restaurant.entity.SysUser) {
            cn.xumob.restaurant.entity.SysUser user = (cn.xumob.restaurant.entity.SysUser) principal;
            userId = user.getId();
            username = user.getUsername();
        } else if (principal instanceof org.springframework.security.core.userdetails.User) {
            // 如果是 Spring Security 默认的 User 对象
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principal;
            username = user.getUsername();
            // 注意：默认的 User 对象没有 ID，需要从其他地方获取
        } else if (principal instanceof String) {
            // 如果 Principal 是字符串（用户名）
            username = (String) principal;
        } else {
            // 尝试通过反射或其他方式获取
            username = authentication.getName(); // 获取用户名
        }

        if (username == null) {
            throw new IllegalArgumentException("Cannot extract username from Authentication");
        }

        return generateRefreshToken(userId, username);
    }

    /**
     * 创建 JWT token
     *
     * @param claims 声明
     * @param subject 主体 (通常是用户名)
     * @param expirationTime 过期时间 (毫秒)
     * @return JWT token
     */
    private static String createToken(Map<String, Object> claims, String subject, Long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 验证 Token 是否有效
     *
     * @param token JWT token
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            // 签名验证失败
        } catch (MalformedJwtException e) {
            // 无效的 JWT 格式
        } catch (ExpiredJwtException e) {
            // Token 过期
        } catch (IllegalArgumentException e) {
            // 声明为空
        } catch (Exception e) {
            // 其他异常
        }
        return false;
    }

    /**
     * 获取 Token 中的 Claims
     *
     * @param token JWT token
     * @return Claims 对象
     */
    public static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 Token 中获取用户名
     *
     * @param token JWT token
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    /**
     * 从 Token 中获取用户 ID
     *
     * @param token JWT token
     * @return 用户 ID
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            Object userId = claims.get("userId");
            if (userId instanceof Integer) {
                return ((Integer) userId).longValue();
            } else if (userId instanceof Long) {
                return (Long) userId;
            }
        }
        return null;
    }

    /**
     * 检查 Token 是否过期
     *
     * @param token JWT token
     * @return 是否过期
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                return claims.getExpiration().before(new Date());
            }
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }

    /**
     * 获取 Token 距离过期的剩余时间 (毫秒)
     *
     * @param token JWT token
     * @return 剩余时间 (毫秒)，过期返回 0
     */
    public static Long getRemainingTimeFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            Long expirationTime = claims.getExpiration().getTime();
            Long currentTime = System.currentTimeMillis();
            Long remainingTime = expirationTime - currentTime;
            return remainingTime > 0 ? remainingTime : 0;
        }
        return 0L;
    }

    /**
     * 从请求头中提取 Token (去掉 "Bearer " 前缀)
     *
     * @param bearerToken 带有 Bearer 前缀的 token
     * @return 实际的 token
     */
    public static String extractTokenFromBearer(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }
}
