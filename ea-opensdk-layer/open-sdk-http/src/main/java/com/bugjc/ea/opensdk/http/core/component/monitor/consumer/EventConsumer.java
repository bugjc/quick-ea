package com.bugjc.ea.opensdk.http.core.component.monitor.consumer;

import com.lmax.disruptor.EventHandler;

/**
 * 事件消费者
 * @author aoki
 * @date 2019/12/19
 * **/
public interface EventConsumer<T> extends EventHandler<T> {}
