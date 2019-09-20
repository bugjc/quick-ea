package com.bugjc.ea.opensdk.http.core.crypto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 接入方加密参数对象
 */
@Data
public class AccessPartyDecryptParam implements Serializable {

    /**
     * 应用id
     */
    private String appId;

    /**
     * RSA 公钥（服务方）
     */
    private String rsaPublicKey;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 随机数，60s 内不能重复，用于签名
     */
    private String nonce;

    /**
     * 业务方应用交易流水号，应用内唯一，格式为：yyyyMMddHHmmss+10 位数字
     */
    private String sequence;

    /**
     * 签名字符串
     */
    private String signature;

    /**
     * 接口业务参数（明文）
     */
    private String body;


}
