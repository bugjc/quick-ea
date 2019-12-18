package com.bugjc.ea.opensdk.http.core.component.monitor.producer;

/**
 * 生产者事件
 * @author aoki
 * @date 2019/12/18
 * **/
public interface EventProducer<T> {

    /**
     * 发送事件消息
     * @param event    --事件
     */
    void onData(T event);
}
