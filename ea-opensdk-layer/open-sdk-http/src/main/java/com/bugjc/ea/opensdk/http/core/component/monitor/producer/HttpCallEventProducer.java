package com.bugjc.ea.opensdk.http.core.component.monitor.producer;

import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEvent;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * 调用 http 消息生产者
 *
 * @author aoki
 * @date 2019/12/5
 **/
public class HttpCallEventProducer {

    private final RingBuffer<HttpCallEvent> ringBuffer;

    public HttpCallEventProducer(RingBuffer<HttpCallEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<HttpCallEvent, ByteBuffer> TRANSLATOR = (event, sequence, bb) -> {
        HttpCallEvent httpCallEvent = JSON.parseObject(bb.array(), HttpCallEvent.class);
        if (httpCallEvent == null){
            return;
        }
        event.setMetadata(httpCallEvent.getMetadata());
    };

    public void onData(ByteBuffer bb) {
        ringBuffer.publishEvent(TRANSLATOR, bb);
    }

}
