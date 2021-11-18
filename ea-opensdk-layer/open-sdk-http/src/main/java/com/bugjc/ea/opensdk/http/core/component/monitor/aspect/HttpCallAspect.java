package com.bugjc.ea.opensdk.http.core.component.monitor.aspect;

import cn.hutool.core.date.TimeInterval;
import com.bugjc.ea.opensdk.aop.aspect.Aspect;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEvent;
import com.bugjc.ea.opensdk.http.core.component.monitor.producer.EventProducer;
import com.google.inject.Inject;
import com.lmax.disruptor.EventFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * http 调用切面
 * @author aoki
 * @date 2019/11/30
 * **/
@Slf4j
public class HttpCallAspect implements Aspect, Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private EventProducer<HttpCallEvent> producer;
    @Inject
    private EventFactory<HttpCallEvent> httpCallEvent;

    private final TimeInterval interval = new TimeInterval();


    @Override
    public boolean before(Object target, Method method, Object[] args) {
        interval.start();
        return true;
    }

    @Override
    public void afterReturning(Object target, Method method, Object[] args, Object returnVal) {
        //设置元数据
        String id = String.valueOf(Arrays.hashCode(args));
        String path = String.valueOf(args[0]);
        producer.onData(httpCallEvent.newInstance().setCallSuccess(id, path, interval.intervalMs()));
    }

    @Override
    public void afterThrowing(Object target, Method method, Object[] args, Throwable e) {
        //设置元数据
        String id = String.valueOf(Arrays.hashCode(args));
        String path = String.valueOf(args[0]);
        producer.onData(httpCallEvent.newInstance().setCallFailed(id, path, interval.intervalMs()));
    }

    @Override
    public void after() {}
}
