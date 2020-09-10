package com.bugjc.ea.opensdk.http.core.dto;

/**
 * 平台认证服务-token应答结果
 *
 * @author aoki
 */
public enum TokenResultCode implements ResultCode {
    /**
     * 请求 token 应答结果状态映射
     */
    Normal(0, "正常"),
    Retry(1, "可重试"),
    Ignorable(2, "可忽略");

    private final int code;
    private final String message;

    TokenResultCode(int code, String message) {
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
