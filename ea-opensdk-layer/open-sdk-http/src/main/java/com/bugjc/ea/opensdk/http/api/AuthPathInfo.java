package com.bugjc.ea.opensdk.http.api;

/**
 * 授权服务接口,接口路径信息，路径、版本
 * @author aoki
 */
public enum AuthPathInfo {
    /**
     * 接口授权服务
     */
    QUERY_TOKEN_V1("/auth/query_token","1.0","获取接口调用凭证");

    private String path;
    private String version;
    private String desc;

    AuthPathInfo(String path, String version, String desc) {
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
