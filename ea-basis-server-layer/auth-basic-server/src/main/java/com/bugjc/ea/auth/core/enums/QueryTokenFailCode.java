package com.bugjc.ea.auth.core.enums;

import com.bugjc.ea.opensdk.http.core.dto.ResultCode;

/**
 * 获取 token 接口业务失败码
 *
 * @author yangqing
 */
public enum QueryTokenFailCode implements ResultCode {
    /**
     * 获取 token 接口业务失败码
     */
    SUCCESS(0, "成功状态"),
    NoSuchCarrier(1, "无此运营商"),
    KeyError(2, "密钥错误"),
    TokenCannotBeEmpty(3, "token不能为空"),
    ERROR(99, "获取TOKEN失败");

    private final int code;
    private final String message;

    QueryTokenFailCode(int code, String message) {
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
