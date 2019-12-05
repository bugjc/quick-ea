package com.bugjc.ea.opensdk.http.core.component.monitor.event;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 事件消费者
 * @author aoki
 * @date 2019/12/5
 * **/
@Slf4j
public class HttpCallEventHandler implements EventHandler<HttpCallEvent> {

    @Override
    public void onEvent(HttpCallEvent event, long l, boolean b) throws Exception {
        log.info("消费消息：{}", event.getPath());
    }
}
