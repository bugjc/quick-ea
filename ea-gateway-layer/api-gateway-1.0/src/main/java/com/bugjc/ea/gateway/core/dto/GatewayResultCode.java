package com.bugjc.ea.gateway.core.dto;

/**
 * 响应码枚举，参考HTTP状态码的语义（网关状态码）
 * @author : aoki
 */
public  enum GatewayResultCode {
    //缺失AppId参数
    APP_ID_MISSING(1000),
    //缺失Sequence参数
    SEQUENCE_MISSING(1001),
    //缺失Signature参数
    SIGNATURE_MISSING(1002),
    //缺失Signature参数
    BUSINESS_PARAM_MISSING(1003),
    //缺失Token参数
    TOKEN_MISSING(1004),
    //IO 异常
    IO_ERROR(1050),
    //请求不能重放限制
    REQUEST_REPLAY_LIMIT(1100),
    //不合法的AppId
    INVALID_APP_ID(1200),
    //未配置RSA安全密钥对
    NOT_CONFIG_RSA(1300),
    //验签失败
    SIGNATURE_ERROR(1301);

    private final int code;

    GatewayResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
