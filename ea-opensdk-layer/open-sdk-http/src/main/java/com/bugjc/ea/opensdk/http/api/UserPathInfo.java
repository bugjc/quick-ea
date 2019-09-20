package com.bugjc.ea.opensdk.http.api;

/**
 * 用户服务接口,接口路径信息，路径、版本
 * @author aoki
 */
public enum UserPathInfo {
    /**
     * 接口信息
     */
    GET_USER_INFO_V1("/user/info/get","1.0","根据用户编号获取用户信息"),
    BALANCE_PAY_V1("/user/balance/pay","1.0","余额支付");

    private String path;
    private String version;
    private String desc;

    UserPathInfo(String path, String version, String desc) {
        this.path = path;
        this.version = version;
        this.desc = desc;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getDesc() {
        return desc;
    }
}
