package com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.annotation;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.HistogramKey;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * 计数器
 * @author aoki
 * @date 2019/12/20
 * **/
public class HistogramImpl implements Histogram, Serializable {

    private final HistogramKey value;

    public HistogramImpl(HistogramKey value){
        this.value = value;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Histogram.class;
    }

    @Override
    public HistogramKey value() {
        return this.value;
    }
}
