package com.bugjc.ea.opensdk.http.core.component.monitor.entity;

import com.bugjc.ea.opensdk.http.core.component.monitor.enums.MetricCounterEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEventMetadataBuilder;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.StatusEnum;

import java.util.Date;

/**
 * 监控元数据建造者
 * @author aoki
 * @date 2019/12/8
 * **/
public abstract class MetadataBuilder {

    protected Metadata metadata = new Metadata();

    public abstract HttpCallEventMetadataBuilder setId(String id);

    public abstract HttpCallEventMetadataBuilder setPath(String path);

    public abstract HttpCallEventMetadataBuilder setStatus(StatusEnum status);

    public abstract HttpCallEventMetadataBuilder setIntervalMs(long intervalMs);

    public abstract HttpCallEventMetadataBuilder setType(MetricCounterEnum type);

    public abstract HttpCallEventMetadataBuilder setCreateTime(Date createTime);

    public Metadata build(){
        return metadata;
    }

}
