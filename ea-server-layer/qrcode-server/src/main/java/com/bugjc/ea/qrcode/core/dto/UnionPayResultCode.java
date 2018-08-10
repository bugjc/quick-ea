package com.bugjc.ea.qrcode.core.dto;

/**
 * 响应码枚举，参考HTTP状态码的语义
 * @author : aoki
 */
public enum UnionPayResultCode {
    SUCCESS("00","成功"),
    FAIL_11("11","验证签名失败"),
    FAIL_33("10","交易金额超限"),
    FAIL_38("38","基于风控原因失败"),
    FAIL_97("97","需输入支付密码"),
    FAIL_98("98","支付时间过长失效"),
    ERROR("99","交易失败")
    ;

    private String code;
    private String message;

    UnionPayResultCode(String code,String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }
}
