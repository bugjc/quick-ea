package com.bugjc.ea.user.core.enums;

/**
 * 余额支付接口业务失败码
 * @author yangqing
 */
public enum BalancePayFailCode {
    /**
     * 业务失败码
     */
    Success(0,"成功"),
    InsufficientBalance(1,"余额不足"),
    Error(99,"余额支付失败");

    private final int code;
    private final String desc;

    BalancePayFailCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
