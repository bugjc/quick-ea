package com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.annotation;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;

/**
 * 计数器注解
 * @author aoki
 * @date 2019/12/20
 * **/
public class Counters {

    private Counters() {}

    /** Creates a {@link Counted} annotation with {@code name} as the value. */
    public static Counted named(CounterKey name) {
        return new CountedImpl(name);
    }
}
