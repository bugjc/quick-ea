package com.bugjc.ea.opensdk.http.core.component.monitor.metric;

import com.codahale.metrics.Gauge;

/**
 * 监控的实例
 */
public interface Gauged<T> extends Named{
    Gauge<T> getGauge();
}
