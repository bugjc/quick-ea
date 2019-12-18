package com.bugjc.ea.opensdk.http.core.component.monitor.event;

import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.http.core.component.monitor.entity.Metadata;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.MetricCounterEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 调用 http 事件
 * @author aoki
 * @date 2019/12/5
 * **/
public class HttpCallEvent implements Serializable {

    /**
     * 元数据
     */
    @Getter
    @Setter
    private Metadata metadata;

    /**
     * 设置调用成功的原始数据
     * @param id
     * @param path
     * @param intervalMs
     * @return
     */
    public HttpCallEvent setCallSuccess(String id, String path, long intervalMs){
        this.metadata = new HttpCallEventMetadataBuilder()
                .setId(id)
                .setPath(path)
                .setIntervalMs(intervalMs)
                .setStatus(StatusEnum.CallSuccess)
                .setType(MetricCounterEnum.TotalRequests)
                .setCreateTime(new Date())
                .build();
        return this;
    }

    /**
     * 设置调用失败的原始数据
     * @param id
     * @param path
     * @param intervalMs
     * @return
     */
    public HttpCallEvent setCallFailed(String id, String path, long intervalMs){
        metadata = new HttpCallEventMetadataBuilder()
                .setId(id)
                .setPath(path)
                .setIntervalMs(intervalMs)
                .setStatus(StatusEnum.CallFailed)
                .setType(MetricCounterEnum.TotalRequests)
                .setCreateTime(new Date())
                .build();
        return this;
    }

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
}
