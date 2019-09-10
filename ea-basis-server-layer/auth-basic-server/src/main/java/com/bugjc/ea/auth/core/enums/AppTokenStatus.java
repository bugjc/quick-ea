package com.bugjc.ea.auth.core.enums;

/**
 * 订单状态
 * @author yangqing
 */
public enum AppTokenStatus {
    /**
     * 订单状态
     */
    SUCCESS(0,"成功状态"),
    NoSuchCarrier(1,"无此运营商"),
    KeyError(2,"密钥错误"),
    TokenCannotBeEmpty(3,"token不能为空"),
    ERROR(99,"获取TOKEN失败");

    private final int status;
    private final String desc;

    AppTokenStatus(int status, String desc) {
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
