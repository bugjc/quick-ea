package com.bugjc.ea.auth.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义业务异常父类
 * @author aoki
 * @date 2019/11/4
 * **/
@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException {
    /**
     * 异常状态码
     */
    private int code;

    /**
     * 异常信息
     */
    private String message;

    /**
     * 发生的方法，位置等
     */
    private String method;

    /**
     * 描述
     */
    private String desc;

    public BizException(String message) {
        super(message);
        this.message = message;
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public BizException(int code, String message, String method, String desc, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.method = method;
        this.desc = desc;
    }
}
