package com.glcxw.gateway.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用类
 * @author aoki
 */
@Data
public class AppVersionMap implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 路径
     */
    private String path;

    /**
     * 版本映射的路径
     */
    private String mapPath;

    /**
     * 创建时间
     */
    private Date createTime;


}
