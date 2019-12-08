package com.bugjc.ea.opensdk.http.core.component.monitor.enums;

/**
 * 元数据指标
 * @author aoki
 */
public enum TypeEnum {
    /**
     * 指标
     */
    TotalRequests(0,"总请求数");

    private final int type;
    private final String desc;

    TypeEnum(int type, String desc) {
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
