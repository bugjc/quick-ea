package com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.impl;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.HistogramKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.Metric;
import com.codahale.metrics.MetricRegistry;

/**
 * 直方图指标服务默认实现
 * @author aoki
 * @date 2019/12/20
 * **/
public class HistogramImpl<T> implements Metric<T> {

    /**
     * 直方图指标实例对象
     */
    private T metric;

    @Override
    public T getMetric() {
        return this.metric;
    }


    public HistogramImpl(MetricRegistry registry, HistogramKey histogramKey) {
        String name = MetricRegistry.name(getClass(), histogramKey.name());
        this.metric = (T) registry.histogram(name);
    }

}
