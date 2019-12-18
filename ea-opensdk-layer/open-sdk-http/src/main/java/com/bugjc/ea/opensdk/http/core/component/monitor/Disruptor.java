package com.bugjc.ea.opensdk.http.core.component.monitor;

import com.lmax.disruptor.RingBuffer;

/**
 * Disruptor 接口
 * @author aoki
 * @date 2019/12/18
 * **/
public interface Disruptor<T> {

    /**
     * 获取生产者
     * @return
     */
    RingBuffer<T> getProducer();
}
