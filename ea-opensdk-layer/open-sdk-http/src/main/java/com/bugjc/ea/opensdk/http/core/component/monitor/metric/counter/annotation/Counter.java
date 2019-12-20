package com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.annotation;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;
import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 计数器指标
 * @author aoki
 * @date 2019/12/20
 * **/
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RUNTIME)
@BindingAnnotation
public @interface Counter {
    /**
     * 获取度量实例的 key
     * @return
     */
    CounterKey value();
}
