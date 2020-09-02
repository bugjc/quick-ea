package com.glcxw.wechat.core.dto;

/**
 * 响应码枚举，参考HTTP状态码的语义
 * @author : aoki
 */
public enum ResultCode {
    //成功
    SUCCESS(200),
    //失败
    FAIL(400),
    //未认证（签名错误）
    UNAUTHORIZED(401),
    //接口不存在
    NOT_FOUND(404),
    //服务器内部错误
    INTERNAL_SERVER_ERROR(500),
    //会员查找不到
    MEMBER_NOT_FOUND(511),
    //会员未关注公众号
    NOT_SUBSCRIBE(512),
    //会员未绑定微信
    NOT_BIND_WE_CHAT(513);

    public int code;

    ResultCode(int code) {
        this.code = code;
    }
}
