package com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.impl;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.Metric;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterService;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.GaugeKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.util.Ratio;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.google.inject.Inject;

/**
 * 计算请求成功率实现
 * @author aoki
 * @date 2019/12/20
 * **/
public class ReqSuccessRatioGaugeServiceImpl extends GaugeDefaultServiceImpl<Double> {

    @Inject
    public ReqSuccessRatioGaugeServiceImpl(Metric<Counter, CounterKey, CounterService> counterMetric) {
        super(GaugeKey.RequestSuccessRatio, new Gauge<Double>() {

            /**
             * 计算请求成功率
             * @return
             */
            @Override
            public Double getValue() {
                return Ratio.of(counterMetric.get(CounterKey.SuccessRequests).getCount(), counterMetric.get(CounterKey.TotalRequests).getCount()).getValue();
            }
        });
    }
}
