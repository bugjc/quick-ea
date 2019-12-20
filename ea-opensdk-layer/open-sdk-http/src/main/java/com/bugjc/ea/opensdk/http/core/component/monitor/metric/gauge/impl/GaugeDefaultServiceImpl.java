package com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.impl;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.GaugeKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.GaugeService;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

/**
 * 度量值指标服务默认实现
 * @author aoki
 * @date 2019/12/20
 * **/
public class GaugeDefaultServiceImpl<T> implements GaugeService<T> {
    /**
     * bean key，用于依赖注入识别
     */
    private String key;

    /**
     * 指标名，用于注册 Gauge(度量值组件)名称
     */
    private String name;

    /**
     * 度量值实例对象
     */
    private Gauge<T> gauge;

    /**
     * 默认实现
     * @param gaugeKey
     * @param gauge
     */
    public GaugeDefaultServiceImpl(GaugeKey gaugeKey, Gauge<T> gauge) {
        this.key = gaugeKey.name();
        this.name = MetricRegistry.name(getClass(), this.key);
        this.gauge = gauge;
    }


    @Override
    public String getKey(){
        return this.key;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Gauge<T> getGauge() {
        return this.gauge;
    }
}
