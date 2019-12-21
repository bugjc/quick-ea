package com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.impl;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.Metric;
import com.codahale.metrics.MetricRegistry;

/**
 * 计数器指标服务默认实现
 * @author aoki
 * @date 2019/12/20
 * **/
public class CounterImpl<T> implements Metric<T> {

    /**
     * 计数器指标实例对象
     */
    private T metric;

    @Override
    public T getMetric() {
        return this.metric;
    }


    public CounterImpl(MetricRegistry registry, CounterKey counterKey) {
        String name = MetricRegistry.name(getClass(), counterKey.name());
        this.metric = (T) registry.counter(name);
    }
}
