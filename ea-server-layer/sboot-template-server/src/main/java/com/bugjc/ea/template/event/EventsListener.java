package com.bugjc.ea.template.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Author yangqing
 * @Date 2019/7/5 14:44
 **/
@Slf4j
@Component
public class EventsListener {

    /**
     * 监听写日志事件
     * @param event
     */
    @EventListener
    public void writeLogEvent(WriteLogEvent event) {
        //这里处理写日志事件
    }
}
