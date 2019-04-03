package com.glcxw.gateway.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用路由类
 * @author aoki
 */
@Data
public class AppRoute implements Serializable {

    private static final long serialVersionUID = -210892970343051215L;
    /**
     * id。
     */
    private Integer id;

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
