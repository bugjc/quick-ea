package com.bugjc.ea.http.opensdk.core.constants;

/**
 * @author aoki
 */
public interface HttpHeaderKeyConstants {
    String APP_ID = "Appid";
    String VERSION = "Version";
    String SEQUENCE = "Sequence";
    String TIMESTAMP = "Timestamp";
    String NONCE = "Nonce";
    String SIGNATURE = "Signature";
    String CONTENT_TYPE = "Content-Type";
    String CONTENT_TYPE_APPLICATION_JSON_VALUE = "application/json;charset=UTF-8";
    String ACCEPT = "Accept";
    String ACCEPT_APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";
    /**
     * 失败回退
     */
    String EA_FALLBACK = "EA-Fallback";
}
