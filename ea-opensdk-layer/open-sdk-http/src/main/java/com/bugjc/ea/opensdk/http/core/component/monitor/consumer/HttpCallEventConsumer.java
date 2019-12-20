package com.bugjc.ea.opensdk.http.core.component.monitor.consumer;

import com.bugjc.ea.opensdk.http.core.component.monitor.data.CountInfoTable;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.StatusEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEvent;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.Metric;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterService;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.GaugeKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.GaugeService;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.HistogramKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.HistogramService;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
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
    private Metric<Counter, CounterKey, CounterService> counterMetric;
    @Inject
    private Metric<Histogram, HistogramKey, HistogramService> histogramMetric;
    @Inject
    private Metric<Gauge, GaugeKey, GaugeService> gaugeMetric;

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
        counterMetric.get(CounterKey.TotalRequests).inc();
        if (event.getMetadata().getStatus() == StatusEnum.CallSuccess){
            //成功请求计数
            counterMetric.get(CounterKey.SuccessRequests).inc();
        }

        //时耗分布区间计算
        histogramMetric.get(HistogramKey.Interval).update(event.getMetadata().getIntervalMs());

        //统计请求成功率
        gaugeMetric.get(GaugeKey.RequestSuccessRatio);
    }
}
