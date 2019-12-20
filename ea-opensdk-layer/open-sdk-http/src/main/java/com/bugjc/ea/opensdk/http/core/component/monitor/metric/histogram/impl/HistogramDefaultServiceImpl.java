package com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.impl;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.HistogramKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.HistogramService;
import com.codahale.metrics.MetricRegistry;

/**
 * 直方图指标服务默认实现
 * @author aoki
 * @date 2019/12/20
 * **/
public class HistogramDefaultServiceImpl implements HistogramService {
    /**
     * bean key，用于依赖注入识别
     */
    private String key;

    /**
     * 指标名，用于注册 Histogram(直方图组件)名称
     */
    private String name;


    public HistogramDefaultServiceImpl(HistogramKey histogramKey) {
        this.key = histogramKey.name();
        this.name = MetricRegistry.name(getClass(), this.key);
    }


    @Override
    public String getKey(){
        return this.key;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
