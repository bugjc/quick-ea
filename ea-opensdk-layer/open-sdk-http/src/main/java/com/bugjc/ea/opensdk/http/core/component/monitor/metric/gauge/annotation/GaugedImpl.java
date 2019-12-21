package com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.annotation;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.GaugeKey;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * 类描述
 * @author aoki
 * @date 2019/12/20
 * **/
public class GaugedImpl implements Gauged, Serializable {

    private final GaugeKey value;

    public GaugedImpl(GaugeKey value){
        this.value = value;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Gauged.class;
    }

    @Override
    public GaugeKey value() {
        return this.value;
    }

    @Override
    public int hashCode() {
        // This is specified in java.lang.Annotation.
        return (127 * "value".hashCode()) ^ value.hashCode();
    }
}
