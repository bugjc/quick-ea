package com.bugjc.ea.opensdk.http.core.component.monitor.event;

import com.bugjc.ea.opensdk.http.core.component.monitor.entity.MetadataBuilder;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.MetricCounterEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.StatusEnum;

import java.util.Date;

/**
 * 建造 http 调用元数据
 * @author aoki
 * @date 2019/12/8
 * **/
public class HttpCallEventMetadataBuilder extends MetadataBuilder {
    @Override
    public HttpCallEventMetadataBuilder setId(String id) {
        metadata.setId(id);
        return this;
    }

    @Override
    public HttpCallEventMetadataBuilder setPath(String path) {
        metadata.setPath(path);
        return this;
    }

    @Override
    public HttpCallEventMetadataBuilder setStatus(StatusEnum status) {
        metadata.setStatus(status);
        return this;
    }

    @Override
    public HttpCallEventMetadataBuilder setIntervalMs(long intervalMs) {
        metadata.setIntervalMs(intervalMs);
        return this;
    }

    @Override
    public HttpCallEventMetadataBuilder setType(MetricCounterEnum type) {
        metadata.setType(type);
        return this;
    }

    @Override
    public HttpCallEventMetadataBuilder setCreateTime(Date createTime) {
        metadata.setCreateTime(createTime);
        return this;
    }
}
