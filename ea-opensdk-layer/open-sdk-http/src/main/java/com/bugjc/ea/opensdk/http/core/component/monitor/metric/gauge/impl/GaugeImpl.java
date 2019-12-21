package com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.impl;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.GaugeKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.Metric;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

/**
 * 度量值指标服务默认实现
 *
 * @author aoki
 * @date 2019/12/20
 **/
public class GaugeImpl<T> implements Metric<T> {

    /**
     * 度量值指标实例对象
     */
    private T metric;

    @Override
    public T getMetric() {
        return this.metric;
    }


    /**
     * 实例度量值指标对象
     * @param registry
     * @param gaugeKey
     * @param gauge
     */
    public GaugeImpl(MetricRegistry registry, GaugeKey gaugeKey, Gauge gauge) {
        String name = MetricRegistry.name(getClass(), gaugeKey.name());
        this.metric = (T) registry.register(name, gauge);
    }
}
