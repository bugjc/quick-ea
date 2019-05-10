package com.bugjc.ea.http.opensdk.model;

import lombok.Data;

import java.io.Serializable;

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
     * 服务方公钥
     */
    private String rsaPublicKey;
    /**
     * 接入方
     */
    private String rsaPrivateKey;

}
