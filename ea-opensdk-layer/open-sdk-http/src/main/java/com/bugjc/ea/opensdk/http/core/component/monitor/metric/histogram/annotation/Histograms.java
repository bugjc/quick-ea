package com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.annotation;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.HistogramKey;

/**
 * 直方图注解
 * @author aoki
 * @date 2019/12/20
 * **/
public class Histograms {

    private Histograms() {}

    /** Creates a {@link HistogramOf} annotation with {@code name} as the value. */
    public static HistogramOf named(HistogramKey name) {
        return new HistogramOfImpl(name);
    }
}
