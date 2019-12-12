package com.bugjc.ea.opensdk.http.core.component.token.enums;

/**
 * 授权服务存储方式
 * @author aoki
 */
public enum AuthTypeEnum {
    /**
     * 状态
     */
    Memory(0,"默认本地存储token"),
    Redis(1,"远程redis凡是存储token");

    private final int type;
    private final String desc;

    AuthTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
