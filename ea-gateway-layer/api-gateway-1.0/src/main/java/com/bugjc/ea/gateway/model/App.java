package com.bugjc.ea.gateway.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用类
 * @author aoki
 */
@Data
public class App implements Serializable {

    /**
     * 应用id。
     */
    private String appId;

    /**
     * RSA 公钥
     */
    private String rsaPublicKey;

    /**
     * RSA 私钥
     */
    private String rsaPrivateKey;

    /**
     * 是否启用（1:启用，0禁用）
     */
    private Boolean enabled;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;


}
