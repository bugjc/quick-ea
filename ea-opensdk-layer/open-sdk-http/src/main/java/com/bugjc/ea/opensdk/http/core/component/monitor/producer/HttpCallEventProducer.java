package com.bugjc.ea.opensdk.http.core.component.monitor.producer;

import cn.hutool.core.bean.BeanUtil;
import com.bugjc.ea.opensdk.http.core.component.monitor.Disruptor;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEvent;
import com.google.inject.Inject;
import com.lmax.disruptor.EventTranslatorOneArg;
import lombok.extern.slf4j.Slf4j;

/**
 * 调用 http 消息生产者
 *
 * @author aoki
 * @date 2019/12/5
 **/
@Slf4j
public class HttpCallEventProducer implements EventProducer<HttpCallEvent> {

    @Inject
    private Disruptor<HttpCallEvent> disruptor;

    private static final EventTranslatorOneArg<HttpCallEvent, HttpCallEvent> TRANSLATOR = (event, sequence, bb) -> {
        BeanUtil.copyProperties(bb, event);
    };

    /**
     * 发送消息
     *
     * @param event --事件
     */
    @Override
    public void onData(HttpCallEvent event) {
        log.info("生产消息：{}", event.toString());
        disruptor.getProducer().publishEvent(TRANSLATOR, event);
    }
}
