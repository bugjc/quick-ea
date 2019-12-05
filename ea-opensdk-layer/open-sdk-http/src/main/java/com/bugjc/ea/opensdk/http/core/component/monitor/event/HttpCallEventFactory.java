package com.bugjc.ea.opensdk.http.core.component.monitor.event;

import com.lmax.disruptor.EventFactory;

/**
 * 实例化 Event 对象
 * @author aoki
 * @date 2019/12/5
 * **/
public class HttpCallEventFactory implements EventFactory<HttpCallEvent> {
    @Override
    public HttpCallEvent newInstance() {
        return new HttpCallEvent();
    }
}
