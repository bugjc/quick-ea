package com.bugjc.ea.gateway.core.dto;

/**
 * 响应码枚举，参考HTTP状态码的语义(通用状态码)
 * @author : aoki
 */
public  enum ResultCode {
    //成功
    SUCCESS(200),
    //失败
    FAIL(400),
    //未认证（签名错误）
    UNAUTHORIZED(401),
    //接口不存在
    NOT_FOUND(404),
    //服务器内部错误
    INTERNAL_SERVER_ERROR(500),
    //服务不可用
    SERVICE_UNAVAILABLE(503);

    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}