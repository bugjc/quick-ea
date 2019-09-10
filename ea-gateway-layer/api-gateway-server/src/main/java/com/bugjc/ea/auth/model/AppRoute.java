package com.bugjc.ea.auth.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用路由类
 * @author aoki
 */
@Data
public class AppRoute implements Serializable {

    /**
     * 应用路由ID
     */
    @Id
    private String id;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 路由id
     */
    private String routeId;

    /**
     * 是否启用debug模式（1:启用，0禁用）
     */
    private Boolean isDebug;

    /**
     * 是否启用（1:启用，0禁用）
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private Date createTime;


}
