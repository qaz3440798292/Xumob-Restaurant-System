package cn.xumob.restaurant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Redis Token 管理服务
 * 
 * 功能：
 * 1. 存储 Token 到 Redis（新 token 覆盖旧 token，实现单点登录）
 * 2. 校验 Token 是否有效
 * 3. 注销 Token
 * 4. 支持 AccessToken 和 RefreshToken 分离
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisTokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    // Token 前缀
    private static final String ACCESS_TOKEN_PREFIX = "token:access:";
    private static final String REFRESH_TOKEN_PREFIX = "token:refresh:";
    private static final String USER_TOKEN_INDEX = "token:user:";

    // Access Token 有效期（毫秒）
    private static final long ACCESS_TOKEN_EXPIRE = 24 * 60 * 60 * 1000L; // 24小时

    // Refresh Token 有效期（毫秒）
    private static final long REFRESH_TOKEN_EXPIRE = 7 * 24 * 60 * 60 * 1000L; // 7天

    /**
     * 存储 AccessToken
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @param loginType 登录类型（EMPLOYEE/RIDER/CUSTOMER）
     * @param accessToken JWT token
     */
    public void storeAccessToken(Long userId, String username, String loginType, String accessToken) {
        // 先删除该用户的旧 Token（如果存在）
        String oldToken = getUserCurrentAccessToken(userId, loginType);
        if (oldToken != null) {
            invalidateAccessToken(oldToken);
        }

        String key = ACCESS_TOKEN_PREFIX + accessToken;
        
        TokenInfo tokenInfo = new TokenInfo(userId, username, loginType, accessToken);
        redisTemplate.opsForValue().set(key, tokenInfo, ACCESS_TOKEN_EXPIRE, TimeUnit.MILLISECONDS);
        
        // 同时记录用户的 token 索引（用于单点登录：覆盖旧 token）
        String userIndexKey = USER_TOKEN_INDEX + userId + ":" + loginType;
        redisTemplate.opsForValue().set(userIndexKey, accessToken, ACCESS_TOKEN_EXPIRE, TimeUnit.MILLISECONDS);
        
        log.info("存储 AccessToken - userId: {}, loginType: {}", userId, loginType);
    }

    /**
     * 存储 RefreshToken
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @param loginType 登录类型
     * @param refreshToken JWT refresh token
     */
    public void storeRefreshToken(Long userId, String username, String loginType, String refreshToken) {
        // 先删除该用户的旧 RefreshToken（如果存在）
        String oldRefreshToken = getUserCurrentRefreshToken(userId, loginType);
        if (oldRefreshToken != null) {
            invalidateRefreshToken(oldRefreshToken);
        }

        String key = REFRESH_TOKEN_PREFIX + refreshToken;
        
        TokenInfo tokenInfo = new TokenInfo(userId, username, loginType, refreshToken);
        redisTemplate.opsForValue().set(key, tokenInfo, REFRESH_TOKEN_EXPIRE, TimeUnit.MILLISECONDS);
        
        // 记录用户的 refresh token 索引
        String userIndexKey = USER_TOKEN_INDEX + userId + ":" + loginType + ":refresh";
        redisTemplate.opsForValue().set(userIndexKey, refreshToken, REFRESH_TOKEN_EXPIRE, TimeUnit.MILLISECONDS);
        
        log.info("存储 RefreshToken - userId: {}, loginType: {}", userId, loginType);
    }

    /**
     * 验证 AccessToken 是否有效
     * 
     * @param accessToken JWT token
     * @return 是否有效
     */
    public boolean validateAccessToken(String accessToken) {
        String key = ACCESS_TOKEN_PREFIX + accessToken;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 验证 RefreshToken 是否有效
     * 
     * @param refreshToken JWT refresh token
     * @return 是否有效
     */
    public boolean validateRefreshToken(String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + refreshToken;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 获取 Token 信息
     * 
     * @param accessToken JWT token
     * @return TokenInfo 或 null
     */
    public TokenInfo getTokenInfo(String accessToken) {
        String key = ACCESS_TOKEN_PREFIX + accessToken;
        Object value = redisTemplate.opsForValue().get(key);
        if (value instanceof TokenInfo) {
            return (TokenInfo) value;
        }
        return null;
    }

    /**
     * 使 AccessToken 失效（退出登录）
     * 
     * @param accessToken JWT token
     */
    public void invalidateAccessToken(String accessToken) {
        TokenInfo tokenInfo = getTokenInfo(accessToken);
        if (tokenInfo != null) {
            // 删除 token
            redisTemplate.delete(ACCESS_TOKEN_PREFIX + accessToken);
            
            // 删除用户索引
            String userIndexKey = USER_TOKEN_INDEX + tokenInfo.getUserId() + ":" + tokenInfo.getLoginType();
            redisTemplate.delete(userIndexKey);
            
            log.info("AccessToken 失效 - userId: {}, loginType: {}", tokenInfo.getUserId(), tokenInfo.getLoginType());
        }
    }

    /**
     * 使 RefreshToken 失效
     * 
     * @param refreshToken JWT refresh token
     */
    public void invalidateRefreshToken(String refreshToken) {
        TokenInfo tokenInfo = getRefreshTokenInfo(refreshToken);
        if (tokenInfo != null) {
            redisTemplate.delete(REFRESH_TOKEN_PREFIX + refreshToken);
            
            String userIndexKey = USER_TOKEN_INDEX + tokenInfo.getUserId() + ":" + tokenInfo.getLoginType() + ":refresh";
            redisTemplate.delete(userIndexKey);
            
            log.info("RefreshToken 失效 - userId: {}, loginType: {}", tokenInfo.getUserId(), tokenInfo.getLoginType());
        }
    }

    /**
     * 使所有 Token 失效（用户所有登录端）
     * 
     * @param userId 用户ID
     */
    public void invalidateAllTokens(Long userId) {
        // 遍历所有登录类型
        String[] loginTypes = {"EMPLOYEE", "RIDER", "CUSTOMER"};
        for (String loginType : loginTypes) {
            // 删除 access token
            String accessIndexKey = USER_TOKEN_INDEX + userId + ":" + loginType;
            Object accessToken = redisTemplate.opsForValue().get(accessIndexKey);
            if (accessToken != null) {
                redisTemplate.delete(ACCESS_TOKEN_PREFIX + accessToken);
            }
            redisTemplate.delete(accessIndexKey);
            
            // 删除 refresh token
            String refreshIndexKey = USER_TOKEN_INDEX + userId + ":" + loginType + ":refresh";
            Object refreshToken = redisTemplate.opsForValue().get(refreshIndexKey);
            if (refreshToken != null) {
                redisTemplate.delete(REFRESH_TOKEN_PREFIX + refreshToken);
            }
            redisTemplate.delete(refreshIndexKey);
        }
        
        log.info("用户所有 Token 失效 - userId: {}", userId);
    }

    /**
     * 获取 RefreshToken 信息
     */
    public TokenInfo getRefreshTokenInfo(String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + refreshToken;
        Object value = redisTemplate.opsForValue().get(key);
        if (value instanceof TokenInfo) {
            return (TokenInfo) value;
        }
        return null;
    }

    /**
     * 获取用户当前的 AccessToken（用于单点登录检测）
     */
    public String getUserCurrentAccessToken(Long userId, String loginType) {
        String userIndexKey = USER_TOKEN_INDEX + userId + ":" + loginType;
        Object token = redisTemplate.opsForValue().get(userIndexKey);
        return token != null ? token.toString() : null;
    }

    /**
     * 获取用户当前的 RefreshToken
     */
    public String getUserCurrentRefreshToken(Long userId, String loginType) {
        String userIndexKey = USER_TOKEN_INDEX + userId + ":" + loginType + ":refresh";
        Object token = redisTemplate.opsForValue().get(userIndexKey);
        return token != null ? token.toString() : null;
    }

    /**
     * Token 信息内部类
     */
    public static class TokenInfo implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        
        private Long userId;
        private String username;
        private String loginType;
        private String token;

        public TokenInfo() {}

        public TokenInfo(Long userId, String username, String loginType, String token) {
            this.userId = userId;
            this.username = username;
            this.loginType = loginType;
            this.token = token;
        }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getLoginType() { return loginType; }
        public void setLoginType(String loginType) { this.loginType = loginType; }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}
