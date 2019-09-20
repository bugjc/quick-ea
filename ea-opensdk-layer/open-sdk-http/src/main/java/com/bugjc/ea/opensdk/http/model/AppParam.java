package com.bugjc.ea.opensdk.http.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author aoki
 */
@Data
public class AppParam implements Serializable {

    /**
     * 接口地址
     */
    private String baseUrl;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 应用密钥
     */
    private String appSecret;

    /**
     * 服务方公钥
     */
    private String rsaPublicKey;

    /**
     * 接入方私钥
     */
    private String rsaPrivateKey;
}
