package com.bugjc.ea.server.message.core.enums;

/**
 * 用户登录业务失败应答码
 * @author yangqing
 */
public enum UserLoginFailCode {
    /**
     * 业务应答码
     */
    Success(0,"登录成功"),
    WrongPassword(1,"密码错误"),
    ERROR(99,"登录失败");

    private final int code;
    private final String desc;

    UserLoginFailCode(int code, String desc) {
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
