package com.bugjc.ea.opensdk.http.core.component.monitor.consumer;

import com.bugjc.ea.opensdk.http.core.component.monitor.data.CountInfoTable;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.MetricCounterEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.MetricHistogramEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.StatusEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEvent;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.Metric;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * 事件消费者
 * @author aoki
 * @date 2019/12/5
 * **/
@Slf4j
public class HttpCallEventConsumer implements EventConsumer<HttpCallEvent> {

    @Inject
    private Metric<Counter, MetricCounterEnum> counterMetric;
    @Inject
    private Metric<Histogram, MetricHistogramEnum> histogramMetric;

    /**
     * 接收消息
     * @param event
     * @param l
     * @param b
     */
    @Override
    public void onEvent(HttpCallEvent event, long l, boolean b)  {
        log.info("消费消息：{}", event.toString());
        CountInfoTable countInfoTable = CountInfoTable.getInstance();
        countInfoTable.increment(event);

        //总请求计数
        counterMetric.get(MetricCounterEnum.TotalRequests).inc();
        if (event.getMetadata().getStatus() == StatusEnum.CallSuccess){
            //成功请求计数
            counterMetric.get(MetricCounterEnum.SuccessRequests).inc();
        }

        //时耗分布区间计算
        histogramMetric.get(MetricHistogramEnum.Interval).update(event.getMetadata().getIntervalMs());
    }
}
