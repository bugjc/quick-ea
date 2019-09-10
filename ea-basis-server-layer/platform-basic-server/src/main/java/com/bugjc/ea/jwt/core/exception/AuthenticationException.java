package com.bugjc.ea.auth.core.exception;

/**
 * 认证异常类
 */
public class AuthenticationException extends BusinessException {

    public AuthenticationException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AuthenticationException(int code, String message, String method, String desc, Throwable cause) {
        super(code, message, method, desc, cause);
    }
}
