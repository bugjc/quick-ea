package com.bugjc.opensdk.util.http.exception;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HttpSecurityException extends RuntimeException {

    private int code;  //异常状态码
    private String message;  //异常信息
    private String method;   //发生的方法，位置等
    private String desc;   //描述

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

    public HttpSecurityException(Throwable e) {
        super(e.getMessage(), e);
    }

    public HttpSecurityException(String message) {
        super(message);
    }

    public HttpSecurityException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public HttpSecurityException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public HttpSecurityException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}
