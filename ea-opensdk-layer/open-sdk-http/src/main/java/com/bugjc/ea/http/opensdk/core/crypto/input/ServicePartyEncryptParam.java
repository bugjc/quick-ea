package com.bugjc.ea.http.opensdk.core.crypto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 服务方解密参数对象
 */
@Data
public class ServicePartyEncryptParam implements Serializable {

    /**
     * 应用id
     */
    private String appId;

    /**
     * RSA 私钥（服务方）
     */
    private String rsaPrivateKey;

    /**
     * 业务方应用交易流水号，应用内唯一，格式为：yyyyMMddHHmmss+10 位数字
     */
    private String sequence;

    /**
     * 接口业务参数（加密）
     */
    private String body;


}
