package cn.xumob.restaurant.exception;

/**
 * 职位级别不足异常
 */
public class InsufficientPositionLevelException extends RuntimeException {
    
    public InsufficientPositionLevelException(String message) {
        super(message);
    }
}
