package com.bugjc.ea.opensdk.http.core.component.monitor.entity;

import com.bugjc.ea.opensdk.http.core.component.monitor.enums.StatusEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.MetricCounterEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Metadata implements Serializable {

    /**
     * 标识ID
     */
    private String id;

    /**
     * 接口路径
     */
    private String path;

    /**
     * 接口状态
     */
    private StatusEnum status;

    /**
     * 调用耗时
     */
    private long intervalMs;

    /**
     * 指标类型
     */
    private MetricCounterEnum type;

    /**
     * 创建时间
     */
    private Date createTime;

}
