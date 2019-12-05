package com.bugjc.ea.opensdk.http.core.component.monitor.event;

import javafx.event.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 事件消费者
 * @author aoki
 * @date 2019/12/5
 * **/
@Slf4j
public class HttpCallEventHandler implements EventHandler<HttpCallEvent> {
    @Override
    public void handle(HttpCallEvent event) {
        log.info("消费消息：{}", event.getPath());
    }
}
