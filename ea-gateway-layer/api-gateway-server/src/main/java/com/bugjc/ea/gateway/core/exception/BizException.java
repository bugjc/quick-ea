package com.bugjc.ea.gateway.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException {

    private int code;  //异常状态码
    private String message;  //异常信息
    private String method;   //发生的方法，位置等
    private String desc;   //描述

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
