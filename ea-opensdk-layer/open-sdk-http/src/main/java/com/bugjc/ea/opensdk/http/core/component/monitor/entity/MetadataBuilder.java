package com.bugjc.ea.opensdk.http.core.component.monitor.entity;

import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallMetadataBuilder;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.StatusEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.TypeEnum;

import java.util.Date;

/**
 * 监控元数据建造者
 * @author aoki
 * @date 2019/12/8
 * **/
public abstract class MetadataBuilder {

    protected Metadata metadata = new Metadata();

    public abstract HttpCallMetadataBuilder setId(String id);

    public abstract HttpCallMetadataBuilder setPath(String path);

    public abstract HttpCallMetadataBuilder setStatus(StatusEnum status);

    public abstract HttpCallMetadataBuilder setIntervalMs(long intervalMs);

    public abstract HttpCallMetadataBuilder setType(TypeEnum type);

    public abstract HttpCallMetadataBuilder setCreateTime(Date createTime);

    public Metadata build(){
        return metadata;
    }

}
