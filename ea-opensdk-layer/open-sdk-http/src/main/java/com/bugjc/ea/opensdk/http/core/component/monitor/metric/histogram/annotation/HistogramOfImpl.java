package com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.annotation;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.HistogramKey;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * 计数器
 * @author aoki
 * @date 2019/12/20
 * **/
public class HistogramOfImpl implements HistogramOf, Serializable {

    private final HistogramKey value;

    public HistogramOfImpl(HistogramKey value){
        this.value = value;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return HistogramOf.class;
    }

    @Override
    public HistogramKey value() {
        return this.value;
    }

    @Override
    public int hashCode() {
        // This is specified in java.lang.Annotation.
        return (127 * "value".hashCode()) ^ value.hashCode();
    }
}
