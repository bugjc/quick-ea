package com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.impl;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;

/**
 * 成功请求总数指标服务实现
 * @author aoki
 * @date 2019/12/20
 * **/
public class SuccessRequestCounterImpl extends CounterImpl<Counter> {

    @Inject
    public SuccessRequestCounterImpl(MetricRegistry registry) {
        super(registry, CounterKey.SuccessRequests);
    }
}
