package com.bugjc.ea.auth.core.enums;

/**
 * 校验token接口
 * @author yangqing
 */
public enum VerifyTokenFailCode {
    /**
     * 校验token接口
     */
    SUCCESS(0,"校验token状态"),
    ERROR(99,"校验token失败");

    private final int code;
    private final String desc;

    VerifyTokenFailCode(int code, String desc) {
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
