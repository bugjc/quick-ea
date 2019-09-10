package com.bugjc.ea.auth.core.enums.business;

/**
 * 订单状态
 * @author yangqing
 */
public enum AppTokenStatus {
    /**
     * 订单状态
     */
    CurrentLatestToken(0,"当前最新的TOKEN"),
    TokenToBeDestroyed(1,"即将销毁的TOKEN"),
    DestroyedToken(2,"已销毁的TOKEN");

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
