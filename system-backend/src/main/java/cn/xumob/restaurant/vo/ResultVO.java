package cn.xumob.restaurant.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应结果对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 成功响应 (无数据)
     */
    public static <T> ResultVO<T> success() {
        return ResultVO.<T>builder()
                .code(200)
                .message("success")
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 成功响应 (有数据)
     */
    public static <T> ResultVO<T> success(T data) {
        return ResultVO.<T>builder()
                .code(200)
                .message("success")
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 成功响应 (自定义消息)
     */
    public static <T> ResultVO<T> success(String message, T data) {
        return ResultVO.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 失败响应 (默认 500 状态码)
     */
    public static <T> ResultVO<T> error(String message) {
        return ResultVO.<T>builder()
                .code(500)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 失败响应 (自定义状态码)
     */
    public static <T> ResultVO<T> error(Integer code, String message) {
        return ResultVO.<T>builder()
                .code(code)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 失败响应 (自定义状态码和数据)
     */
    public static <T> ResultVO<T> error(Integer code, String message, T data) {
        return ResultVO.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 参数验证失败
     */
    public static <T> ResultVO<T> paramError(String message) {
        return ResultVO.<T>builder()
                .code(400)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 未授权
     */
    public static <T> ResultVO<T> unauthorized(String message) {
        return ResultVO.<T>builder()
                .code(401)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 禁止访问
     */
    public static <T> ResultVO<T> forbidden(String message) {
        return ResultVO.<T>builder()
                .code(403)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 资源不存在
     */
    public static <T> ResultVO<T> notFound(String message) {
        return ResultVO.<T>builder()
                .code(404)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}

