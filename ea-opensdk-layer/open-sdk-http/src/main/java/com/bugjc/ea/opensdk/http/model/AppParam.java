package com.bugjc.ea.opensdk.http.model;

import lombok.Data;
import redis.clients.jedis.JedisPool;

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

    /**
     * 设置服务方接口调用“凭证”保存到 redis,不设置默认保存到本地内存中（非必填）
     */
    private JedisPool jedisPool;
}
