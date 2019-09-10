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
public class AppVersionMap implements Serializable {


    /**
     * 应用版本映射id。
     */
    @Id
    private String id;

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
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;


}
