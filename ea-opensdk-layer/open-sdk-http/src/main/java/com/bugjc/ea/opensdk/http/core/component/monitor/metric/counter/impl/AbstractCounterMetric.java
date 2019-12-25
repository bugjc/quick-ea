package com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.impl;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.Metric;
import com.codahale.metrics.MetricRegistry;

/**
 * {@link Metric}的计数器支持类，它减少重复并导致更易读的配置；只需继承此类，实例化时使用此类构造函数注入即可。
 * @author aoki
 * @date 2019/12/20
 * **/
public abstract class AbstractCounterMetric<T> implements Metric<T> {

    /**
     * 计数器指标实例对象
     */
    private T metric;

    @Override
    public T getMetric() {
        return this.metric;
    }


    public AbstractCounterMetric(MetricRegistry registry, CounterKey counterKey) {
        String name = MetricRegistry.name(getClass(), counterKey.name());
        this.metric = (T) registry.counter(name);
    }
}
