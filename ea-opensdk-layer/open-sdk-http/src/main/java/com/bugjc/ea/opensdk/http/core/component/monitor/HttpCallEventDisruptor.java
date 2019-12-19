package com.bugjc.ea.opensdk.http.core.component.monitor;

import com.bugjc.ea.opensdk.http.core.component.monitor.consumer.EventConsumer;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEvent;
import com.google.inject.Inject;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;

/**
 * http调用事件 Disruptor 实例对象
 * @author aoki
 * @date 2019/12/18
 * **/
@Slf4j
public class HttpCallEventDisruptor implements Disruptor<HttpCallEvent> {

    /** 指定环形缓冲区的大小，必须为2的幂。 */
    private final static int BUFFER_SIZE = 1024;
    /** 使用单生产者模式 */
    private final static ProducerType PRODUCER_TYPE = ProducerType.SINGLE;

    @Getter
    private com.lmax.disruptor.dsl.Disruptor<HttpCallEvent> disruptor;

    @Inject
    public HttpCallEventDisruptor(EventConsumer<HttpCallEvent> eventConsumer,
                                  EventFactory<HttpCallEvent> eventFactory,
                                  ThreadFactory threadFactory,
                                  WaitStrategy waitStrategy){
       this.disruptor = new com.lmax.disruptor.dsl.Disruptor<>(eventFactory, BUFFER_SIZE, threadFactory, PRODUCER_TYPE, waitStrategy);

       //注入事件消费者并启动
       this.disruptor.handleEventsWith(eventConsumer);
       this.disruptor.start();
       log.info("注入事件消费者:{} 到队列：{}", eventConsumer, disruptor);
    }

    @Override
    public RingBuffer<HttpCallEvent> getProducer() {
        return disruptor.getRingBuffer();
    }
}
