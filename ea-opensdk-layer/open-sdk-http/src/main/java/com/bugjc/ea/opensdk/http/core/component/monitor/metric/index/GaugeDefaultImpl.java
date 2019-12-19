package com.bugjc.ea.opensdk.http.core.component.monitor.metric.index;


import com.codahale.metrics.Gauge;

/**
 * Gauge 类指标
 * @author aoki
 * @date 2019/12/19
 * **/
public class GaugeDefaultImpl<T> implements Gauge<T> {

    @Override
    public T getValue() {
        return null;
    }
}
