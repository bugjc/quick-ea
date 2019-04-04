package com.bugjc.ea.jwt.core.dto;

/**
 * 响应码枚举，参考HTTP状态码的语义
 * @author : aoki
 */
public enum AuthResultCode {
    //缺失token参数
    ACCESS_TOKEN_MISSING(2001),
    //不合法的token
    INVALID_TOKEN_EXPIRED(2002),
    //Token过期
    ACCESS_TOKEN_EXPIRED(2003);

    public int code;

    AuthResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }}
