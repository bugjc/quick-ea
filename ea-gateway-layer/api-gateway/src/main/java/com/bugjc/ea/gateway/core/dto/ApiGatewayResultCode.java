package com.bugjc.ea.gateway.core.dto;

import com.netflix.discovery.shared.Application;

/**
 * 响应码枚举，参考HTTP状态码的语义
 * @author : aoki
 */
public  enum ApiGatewayResultCode {
    //业务应用负载均衡服务不可用
    BUSINESS_LOAD_BALANCING_UNAVAILABLE(1000,"业务应用负载均衡服务暂不可用。");

    private final int code;
    private final String message;

    ApiGatewayResultCode(int code,String message) {
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
