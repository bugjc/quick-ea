package com.bugjc.ea.gateway.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 排除签名路径
 * @author aoki
 */
@Data
public class AppConfig implements Serializable {

    /**
     * 主键
     */
    private int id;

    /**
     * app_id
     */
    private String appId;

    /**
     * 排除前缀路径
     */
    private String excludePath;

    /**
     * 是否需要签名（1:是,0:否）
     */
    private Boolean isSignature;

    /**
     * 是否核查会员Token（1:是,0:否）
     */
    private Boolean isVerifyToken;

    /**
     * 是否启用（1:启用，0:禁用）
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
