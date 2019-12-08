package com.bugjc.ea.opensdk.http.core.component.monitor.event;

import com.bugjc.ea.opensdk.http.core.component.monitor.entity.MetadataBuilder;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.StatusEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.TypeEnum;

import java.util.Date;

/**
 * 建造 http 调用元数据
 * @author aoki
 * @date 2019/12/8
 * **/
public class HttpCallMetadataBuilder extends MetadataBuilder {
    @Override
    public HttpCallMetadataBuilder setId(String id) {
        metadata.setId(id);
        return this;
    }

    @Override
    public HttpCallMetadataBuilder setPath(String path) {
        metadata.setPath(path);
        return this;
    }

    @Override
    public HttpCallMetadataBuilder setStatus(StatusEnum status) {
        metadata.setStatus(status);
        return this;
    }

    @Override
    public HttpCallMetadataBuilder setIntervalMs(long intervalMs) {
        metadata.setIntervalMs(intervalMs);
        return this;
    }

    @Override
    public HttpCallMetadataBuilder setType(TypeEnum type) {
        metadata.setType(type);
        return this;
    }

    @Override
    public HttpCallMetadataBuilder setCreateTime(Date createTime) {
        metadata.setCreateTime(createTime);
        return this;
    }
}
