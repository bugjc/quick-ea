package com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.impl;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.HistogramKey;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;

/**
 * 直方图指标服务统计时耗实现
 * @author aoki
 * @date 2019/12/20
 * **/
public class IntervalHistogramMetric extends AbstractHistogramMetric<Histogram> {

    @Inject
    public IntervalHistogramMetric(MetricRegistry registry) {
        super(registry, HistogramKey.Interval);
    }
}
