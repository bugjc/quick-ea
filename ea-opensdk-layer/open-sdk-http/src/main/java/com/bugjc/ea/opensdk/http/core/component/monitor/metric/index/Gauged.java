package com.bugjc.ea.opensdk.http.core.component.monitor.metric.index;

import com.codahale.metrics.Gauge;

/**
 * 监控的实例
 */
public interface Gauged {
    Gauge getGauge();
}
