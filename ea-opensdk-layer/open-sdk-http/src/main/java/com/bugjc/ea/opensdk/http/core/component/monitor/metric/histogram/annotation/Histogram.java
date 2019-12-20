package com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.annotation;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.HistogramKey;
import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 直方图指标
 * @author aoki
 * @date 2019/12/20
 * **/
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RUNTIME)
@BindingAnnotation
public @interface Histogram {
    /**
     * 获取度量实例的 key
     * @return
     */
    HistogramKey value();
}
