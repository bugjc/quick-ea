package com.bugjc.ea.http.opensdk.core.dto;

/**
 * 响应码枚举，参考HTTP状态码的语义(通用状态码)
 * @author : aoki
 */
public  enum ResultCode {
    //成功
    SUCCESS(200),
    //失败
    FAIL(400),
    //接口不存在
    NOT_FOUND(404),
    //服务器内部错误
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
