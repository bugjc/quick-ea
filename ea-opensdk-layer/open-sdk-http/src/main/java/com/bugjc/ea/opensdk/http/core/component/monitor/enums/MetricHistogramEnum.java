package com.bugjc.ea.opensdk.http.core.component.monitor.enums;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.index.Named;
import com.codahale.metrics.MetricRegistry;

/**
 * 监控指标定义
 * @author aoki
 */
public enum MetricHistogramEnum implements Named {
    /**
     * 调用耗时统计
     */
    Interval;

    /**
     * 获取指标名称
     * @return
     */
    @Override
    public String getName(){
        return MetricRegistry.name(MetricHistogramEnum.class, name());
    }
}
