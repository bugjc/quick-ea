package com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.annotation;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.GaugeKey;

/**
 * 度量值注解
 * @author aoki
 * @date 2019/12/20
 * **/
public class Gauges {

    private Gauges() {}

    /** Creates a {@link Gauged} annotation with {@code name} as the value. */
    public static Gauged named(GaugeKey name) {
        return new GaugedImpl(name);
    }
}
