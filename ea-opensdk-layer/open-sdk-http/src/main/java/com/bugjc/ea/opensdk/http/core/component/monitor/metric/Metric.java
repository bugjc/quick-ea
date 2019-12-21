package com.bugjc.ea.opensdk.http.core.component.monitor.metric;

/**
 * 监控度量对象
 * @author aoki
 * @date 2019/12/20
 * **/
public interface Metric<T> {

    /**
     * 获取度量对象
     * @return
     */
    T getMetric();
}
