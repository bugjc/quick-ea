package com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.annotation;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * 计数器
 * @author aoki
 * @date 2019/12/20
 * **/
public class CountedImpl implements Counted, Serializable {

    private final CounterKey value;

    public CountedImpl(CounterKey value){
        this.value = value;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Counted.class;
    }

    @Override
    public CounterKey value() {
        return this.value;
    }

    @Override
    public int hashCode() {
        // This is specified in java.lang.Annotation.
        return (127 * "value".hashCode()) ^ value.hashCode();
    }
}
