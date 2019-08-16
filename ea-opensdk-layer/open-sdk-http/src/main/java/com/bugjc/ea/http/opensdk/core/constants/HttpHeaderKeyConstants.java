package com.bugjc.ea.http.opensdk.core.constants;

/**
 * @author aoki
 */
public interface HttpHeaderKeyConstants {
    String APP_ID = "AppId";
    String VERSION = "Version";
    String SEQUENCE = "Sequence";
    String TIMESTAMP = "Timestamp";
    String NONCE = "Nonce";
    String SIGNATURE = "Signature";
    String AUTHORIZATION = "Authorization";
    String CONTENT_TYPE = "Content-Type";
    String CONTENT_TYPE_APPLICATION_JSON_VALUE = "application/json;charset=UTF-8";
    String ACCEPT = "Accept";

    /**
     * 是否开启debug
     */
    String IS_DEBUG = "IsDebug";

    /**
     * 网关错误标记，遇到此标记，客户端无需解密
     */
    String GATEWAY_ERROR_FLAG = "Gateway-Error-Flag";
}
