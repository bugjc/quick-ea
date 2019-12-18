package com.bugjc.ea.opensdk.http.core.component.monitor.consumer;

import com.bugjc.ea.opensdk.http.core.component.monitor.Metric;
import com.bugjc.ea.opensdk.http.core.component.monitor.data.CountInfoTable;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEvent;
import com.codahale.metrics.Counter;
import com.google.inject.Inject;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 事件消费者
 * @author aoki
 * @date 2019/12/5
 * **/
@Slf4j
public class HttpCallEventHandler implements EventHandler<HttpCallEvent> {

    @Inject
    private Metric<Counter> metric;

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

        //指标计数
        metric.get().inc();
        //metric.print(2, TimeUnit.SECONDS);
        //counter.inc();
    }
}
