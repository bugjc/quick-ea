package com.bugjc.ea.auth.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

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
    @Id
    private String id;

    /**
     * 应用密钥
     */
    private String appSecret;

    /**
     * RSA 公钥（服务方）
     */
    private String rsaPublicKey;

    /**
     * RSA 私钥（接入方）
     */
    private String rsaPrivateKey;

    /**
     * 类型：1,- 业务，2 - 平台
     */
    private Integer type;

    /**
     * 接入方元数据
     */
    private String accessPartyMetadata;

    /**
     * 服务方元数据
     */
    private String servicePartyMetadata;

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
