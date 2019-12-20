package com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.impl;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.HistogramKey;

/**
 * 直方图指标服务统计时耗实现
 * @author aoki
 * @date 2019/12/20
 * **/
public class IntervalHistogramServiceImpl extends HistogramDefaultServiceImpl {

    public IntervalHistogramServiceImpl() {
        super(HistogramKey.Interval);
    }
}
