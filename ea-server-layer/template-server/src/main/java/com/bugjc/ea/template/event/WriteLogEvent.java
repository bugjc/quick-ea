package com.bugjc.ea.template.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 写日志事件
 * @Author yangqing
 * @Date 2019/7/5 14:42
 **/
public class WriteLogEvent extends ApplicationEvent {

    @Getter
    private String logBody;

    public WriteLogEvent(Object source) {
        super(source);
    }

    public WriteLogEvent(Object source, String logBody) {
        super(source);
        this.logBody = logBody;
    }
}
