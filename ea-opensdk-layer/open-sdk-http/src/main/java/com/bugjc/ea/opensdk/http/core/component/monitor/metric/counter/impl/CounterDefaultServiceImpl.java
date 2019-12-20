package com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.impl;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterService;
import com.codahale.metrics.MetricRegistry;

/**
 * 计数器指标服务默认实现
 * @author aoki
 * @date 2019/12/20
 * **/
public class CounterDefaultServiceImpl implements CounterService {
    /**
     * bean key，用于依赖注入识别
     */
    private String key;

    /**
     * 指标名，用于注册 Gauge(度量值组件)名称
     */
    private String name;


    public CounterDefaultServiceImpl(CounterKey counterKey) {
        this.key = counterKey.name();
        this.name = MetricRegistry.name(getClass(), this.key);
    }


    @Override
    public String getKey(){
        return this.key;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
