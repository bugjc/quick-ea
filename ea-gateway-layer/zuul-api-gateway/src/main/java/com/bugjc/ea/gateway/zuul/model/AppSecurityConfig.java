package com.bugjc.ea.gateway.zuul.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 排除签名路径
 * @author aoki
 */
@Data
public class AppSecurityConfig implements Serializable {

    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * app_id
     */
    private String appId;

    /**
     * 路径
     */
    private String path;

    /**
     * 是否核验签名（1:是,0:否）
     */
    private boolean isVerifySignature;

    /**
     * 是否核验Token（1:是,0:否）
     */
    private boolean isVerifyToken;

    /**
     * 是否启用（1:启用，0:禁用）
     */
    private boolean enabled;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;
}
