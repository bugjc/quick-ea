package com.bugjc.ea.auth.core.dto;

/**
 * 响应码枚举，参考HTTP状态码的语义（网关状态码）
 * @author : aoki
 */
public  enum ApiGatewayServerResultCode {
    //业务服务不可用
    BUSINESS_SERVICE_UNAVAILABLE(1020,"业务服务不可用"),
    //路由失败
    ROUTE_FAILURE(1030,"路由失败"),
    //缺失AppId参数
    APP_ID_MISSING(1050,"缺失 APP_ID 参数"),
    //缺失Sequence参数
    SEQUENCE_MISSING(1051,"缺失 SEQUENCE 参数"),
    //缺失Signature参数
    SIGNATURE_MISSING(1052,"缺失 SIGNATURE 参数"),
    //缺失业务参数
    BUSINESS_PARAM_MISSING(1053,"缺失业务参数"),
    //缺失Token参数
    TOKEN_MISSING(1054,"缺失 TOKEN 参数"),
    //IO 异常
    IO_ERROR(1060,"IO 异常"),
    //请求不能重放限制
    REQUEST_REPLAY_LIMIT(1100,"请求不能重放"),
    //@delete不合法的AppId
    INVALID_APP_ID(1200,"不合法的 APP_ID"),
    //未配置RSA安全密钥对
    NOT_CONFIG_RSA(1300,"未配置RSA安全密钥对"),
    //验签失败
    SIGNATURE_ERROR(1301,"验签失败");

    private final int code;
    private final String message;

    ApiGatewayServerResultCode(int code,String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage(){
        return message;
    }
}
