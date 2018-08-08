package com.bugjc.ea.qrcode.core.enums;

/**
 * 响应码枚举，参考HTTP状态码的语义
 * @author : aoki
 */
public enum QrCodeResultCodeEnum {
    //成功
    SUCCESS("00"),
    //交易失败，详情咨询95516
    FAIL_01("01"),
    //交易状态未明
    FAIL_04("01")
    ;

    private String code;

    QrCodeResultCodeEnum(String code) {
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }
}
