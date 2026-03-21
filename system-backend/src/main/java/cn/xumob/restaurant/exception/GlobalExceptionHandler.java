package cn.xumob.restaurant.exception;

import cn.xumob.restaurant.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResultVO<?> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResultVO.error("系统错误，请联系管理员");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResultVO<?> handleAuthenticationException(AuthenticationException e) {
        log.error("账号验证失败: {}", e.getMessage(), e);
        return ResultVO.unauthorized(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResultVO<?> handleBadCredentialsException(BadCredentialsException e) {
        log.error("认证失败: {}", e.getMessage(), e);
        return ResultVO.unauthorized(e.getMessage());
    }

    @ExceptionHandler(UserExistException.class)
    public ResultVO<?> handleUserExistException(UserExistException e) {
        log.error("用户已存在: {}", e.getMessage(), e);
        return ResultVO.error(e.getMessage());
    }

    @ExceptionHandler(RegisterTypeAvailableException.class)
    public ResultVO<?> handleRegisterTypeAvailableException(RegisterTypeAvailableException e) {
        log.error("注册类型不可用: {}", e.getMessage(), e);
        return ResultVO.error(e.getMessage());
    }

    @ExceptionHandler(InsufficientPositionLevelException.class)
    public ResultVO<?> handleInsufficientPositionLevelException(InsufficientPositionLevelException e) {
        log.error("职位级别不足: {}", e.getMessage());
        // 职位级别不足返回 403 Forbidden
        return ResultVO.forbidden(e.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResultVO<?> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.error("权限不足: {}", e.getMessage());
        return ResultVO.forbidden(e.getMessage());
    }
}
