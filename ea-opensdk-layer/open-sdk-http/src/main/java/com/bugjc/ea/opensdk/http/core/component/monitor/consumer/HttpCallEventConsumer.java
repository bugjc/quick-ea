package com.bugjc.ea.opensdk.http.core.component.monitor.consumer;

import com.bugjc.ea.opensdk.http.core.component.monitor.data.CountInfoTable;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.StatusEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEvent;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.Metric;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.annotation.Counted;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.HistogramKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.annotation.HistogramOf;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * 事件消费者
 *
 * @author aoki
 * @date 2019/12/5
 **/
@Slf4j
public class HttpCallEventConsumer implements EventConsumer<HttpCallEvent> {

    @Inject
    @Counted(CounterKey.SuccessRequests)
    private Metric<Counter> successRequests;
    @Inject
    @Counted(CounterKey.TotalRequests)
    private Metric<Counter> totalRequests;
    @Inject
    @HistogramOf(HistogramKey.Interval)
    private Metric<Histogram> interval;

    /**
     * 接收消息
     *
     * @param event
     * @param l
     * @param b
     */
    @Override
    public void onEvent(HttpCallEvent event, long l, boolean b) {
        log.info("消费消息：{}", event.toString());
        CountInfoTable countInfoTable = CountInfoTable.getInstance();
        countInfoTable.increment(event);

        //总请求计数
        totalRequests.getMetric().inc();
        if (event.getMetadata().getStatus() == StatusEnum.CallSuccess) {
            //成功请求计数
            successRequests.getMetric().inc();
        }

        //时耗分布区间计算
        interval.getMetric().update(event.getMetadata().getIntervalMs());
    }
}
