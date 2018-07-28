package com.bugjc.ea.gateway.core.enums;

/**
 * 响应状态描述枚举
 *
 * @author qingyang
 */
public enum ResultErrorEnum {
    /**
     * 响应成功结果
     */
    Success(200,"成功"),
    /**
     * 响应签名验证失败
     */
    VerifySignError(201,"签名验证失败"),
    /**
     * 响应参数错误
     */
    ParamError(202,"参数错误"),
    /**
     * 响应io错误
     */
    IOError(203,"IO流读取失败");
    private int code;
    private String msg;
    ResultErrorEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
