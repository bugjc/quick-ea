package com.bugjc.ea.opensdk.http.core.component.monitor.metric.index;

import com.bugjc.ea.opensdk.http.core.component.monitor.enums.MetricCounterEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.Metric;
import com.codahale.metrics.Counter;
import com.codahale.metrics.RatioGauge;
import com.google.inject.Inject;

/**
 * 计算请求成功率
 * @author aoki
 * @date 2019/12/19
 * **/
public class RequestSuccessRatioGauge extends RatioGauge {

    @Inject
    private Metric<Counter, MetricCounterEnum> counterMetric;

    @Override
    protected Ratio getRatio() {
        //计算请求成功率
        return RatioGauge.Ratio.of(counterMetric.get(MetricCounterEnum.SuccessRequests).getCount(), counterMetric.get(MetricCounterEnum.TotalRequests).getCount());
    }

}
