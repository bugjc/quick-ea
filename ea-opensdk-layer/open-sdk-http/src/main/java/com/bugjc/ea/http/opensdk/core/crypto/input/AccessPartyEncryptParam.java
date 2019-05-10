package com.bugjc.ea.http.opensdk.core.crypto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 接入方加密参数对象
 */
@Data
public class AccessPartyEncryptParam implements Serializable {

    /**
     * 应用id
     */
    private String appId;

    /**
     * 接入方
     */
    private String rsaPrivateKey;

    /**
     * 接口业务参数（明文）
     */
    private String body;


}
