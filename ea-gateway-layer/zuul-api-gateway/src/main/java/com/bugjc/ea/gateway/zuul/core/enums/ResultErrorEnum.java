package com.bugjc.ea.gateway.zuul.core.enums;

/**
 * 响应状态描述枚举
 *
 * @author qingyang
 */
public enum ResultErrorEnum {
    /**
     * 网关应答结果
     */
    VerifySignError(1001,"签名验证失败"),
    ParamBodyError(1002,"body参数不能为空"),
    IOError(1003,"IO流读取失败"),
    ParamHeaderError(1004,"header参数不能为空"),
    DECRYPT_ERROR(1005,"解密失败"),
    ERROR_URI(1006, "无效的请求地址"),
    LOGIN_REPLAY_COUNT_OVER_LIMIT(1007, "请求不能重放"),
    TOKEN_EXPIRE(1008, "token已过期"),
    VerifyTokenError(1009,"token验证失败");
    private final int code;
    private final String msg;

    ResultErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
