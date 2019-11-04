package com.bugjc.ea.gateway.zuul.core.enums.business;

/**
 * 应用类型
 * @author aoki
 */
public enum AppType {
    /**
     * 应用类型：1 - 业务,，2 - 平台
     */
    Business(1,"业务"),
    Platform(2,"平台");

    private final int type;
    private final String desc;

    AppType(int type, String desc) {
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
