package com.bugjc.ea.opensdk.http.core.component.monitor.enums;

/**
 * 元数据状态
 * @author aoki
 */
public enum StatusEnum {
    /**
     * 状态
     */
    Ready(0,"准备就绪"),
    CallSuccess(1,"调用成功"),
    CallFailed(2,"调用失败");

    private final int status;
    private final String desc;

    StatusEnum(int status, String desc) {
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
