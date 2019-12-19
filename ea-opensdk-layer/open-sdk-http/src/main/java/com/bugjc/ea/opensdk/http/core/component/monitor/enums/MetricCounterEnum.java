package com.bugjc.ea.opensdk.http.core.component.monitor.enums;

import com.codahale.metrics.MetricRegistry;

/**
 * 监控指标定义
 * @author aoki
 */
public enum MetricCounterEnum {
    /**
     * 总请求数指标
     */
    TotalRequests,
    /**
     * 成功总请求数指标
     */
    SuccessRequests;

    /**
     * 获取指标名称
     * @return
     */
    public String getName(){
        return MetricRegistry.name(MetricCounterEnum.class, name());
    }
}
