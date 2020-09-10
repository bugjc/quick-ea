package com.bugjc.ea.opensdk.http.core.dto;

/**
 * 响应码枚举，参考HTTP状态码的语义(通用状态码)
 *
 * @author : aoki
 */
public enum CommonResultCode implements ResultCode{
    //成功
    SUCCESS(200, "成功"),
    //失败
    FAIL(400, "失败"),
    //未认证（签名错误）
    UNAUTHORIZED(401, "未认证（签名错误）"),
    //token过期
    TOKEN_EXPIRE(403, "Token 过期"),
    //接口不存在
    NOT_FOUND(404, "接口不存在"),
    //服务器内部错误
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    //系统维护
    SYSTEM_MAINTENANCE(999, "系统维护");

    private final int code;
    private final String message;

    CommonResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
