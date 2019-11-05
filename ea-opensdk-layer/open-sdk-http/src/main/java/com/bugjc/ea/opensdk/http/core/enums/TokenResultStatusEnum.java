package com.bugjc.ea.opensdk.http.core.enums;

/**
 * 平台认证服务-token应答结果
 * @author aoki
 */
public enum TokenResultStatusEnum {
    /**
     * 请求 token 应答结果状态映射
     */
    Normal(0,"正常"),
    Retry(1,"可重试"),
    Ignorable(2,"可忽略");

    private final int status;
    private final String desc;

    TokenResultStatusEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
