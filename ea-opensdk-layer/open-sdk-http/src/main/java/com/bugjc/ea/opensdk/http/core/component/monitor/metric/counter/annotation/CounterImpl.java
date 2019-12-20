package com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.annotation;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * 计数器
 * @author aoki
 * @date 2019/12/20
 * **/
public class CounterImpl implements Counter, Serializable {

    private final CounterKey value;

    public CounterImpl(CounterKey value){
        this.value = value;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Counter.class;
    }

    @Override
    public CounterKey value() {
        return this.value;
    }
}
