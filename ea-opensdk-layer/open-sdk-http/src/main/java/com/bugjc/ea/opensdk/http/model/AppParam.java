package com.bugjc.ea.opensdk.http.model;

import com.bugjc.ea.opensdk.http.core.component.token.enums.AuthTypeEnum;
import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;

/**
 * @author aoki
 */
public class AppParam implements Serializable {

    /**
     * 接口地址
     */
    @Getter
    @Setter
    private String baseUrl;

    /**
     * 应用id
     */
    @Getter
    @Setter
    private String appId;

    /**
     * 应用密钥
     */
    @Getter
    @Setter
    private String appSecret;

    /**
     * 服务方公钥
     */
    @Getter
    @Setter
    private String rsaPublicKey;

    /**
     * 接入方私钥
     */
    @Getter
    @Setter
    private String rsaPrivateKey;

    /**
     * 授权服务存储方式
     */
    @Getter
    private int authType = AuthTypeEnum.Memory.getType();

    /**
     * 设置服务方接口调用“凭证”保存到 redis,不设置默认保存到本地内存中（非必填）
     */
    @Getter
    private JedisPool jedisPool;

    public void setJedisPool(JedisPool jedisPool){
        this.jedisPool = jedisPool;
        this.authType = AuthTypeEnum.Redis.getType();
    }
}
