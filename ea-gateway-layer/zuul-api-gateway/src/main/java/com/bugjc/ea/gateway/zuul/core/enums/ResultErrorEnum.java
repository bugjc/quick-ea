package com.bugjc.ea.gateway.zuul.core.enums;

/**
 * 响应状态描述枚举
 *
 * @author qingyang
 */
public enum ResultErrorEnum {
    /**
     * 响应成功结果
     */
    Success(200,"成功"),
    /**
     * 响应签名验证失败
     */
    VerifySignError(201,"签名验证失败"),
    /**
     * 响应参数错误
     */
    ParamBodyError(202,"body参数不能为空"),
    /**
     * 响应io错误
     */
    IOError(203,"IO流读取失败"),
    /**
     * header参数不能为空
     */
    ParamHeaderError(204,"header参数不能为空"),

    /**
     * 解密失败
     */
    DECRYPT_ERROR(206,"header参数不能为空"),

    /**
     * 无效的请求地址
     */
    ERROR_URI(209, "无效的请求地址"),

    //重放限制
    LOGIN_REPLAY_COUNT_OVER_LIMIT(527, "请求不能重放"),

    //token过期
    TOKEN_EXPIRE(403, "token已过期"),

    /**
     * token验证失败
     */
    VerifyTokenError(205,"token验证失败");
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
