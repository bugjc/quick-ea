package com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.impl;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;

/**
 * 总请求数指标服务实现
 * @author aoki
 * @date 2019/12/20
 * **/
public class TotalRequestCounterImpl extends CounterImpl<Counter> {

    @Inject
    public TotalRequestCounterImpl(MetricRegistry registry) {
        super(registry, CounterKey.TotalRequests);
    }
}
