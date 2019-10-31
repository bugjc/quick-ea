package com.bugjc.ea.opensdk.http.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HttpSecurityException extends RuntimeException {

    private int code;  //异常状态码
    private String message;  //异常信息
    private String method;   //发生的方法，位置等
    private String desc;   //描述

    public HttpSecurityException(String message) {
        super(message);
        this.message = message;
    }

    public HttpSecurityException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public HttpSecurityException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public HttpSecurityException(int code, String message, String method, String desc, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.method = method;
        this.desc = desc;
    }
}