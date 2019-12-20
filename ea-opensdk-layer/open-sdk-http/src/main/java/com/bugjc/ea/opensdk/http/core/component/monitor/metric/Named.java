package com.bugjc.ea.opensdk.http.core.component.monitor.metric;

/**
 * 监控组件注册的指标名
 * @author aoki
 * @date 2019/12/20
 * **/
public interface Named {

    /**
     * bean key，用于依赖注入识别
     * @return
     */
    String getKey();

    /**
     * 隐式指标名，用于注册 Gauge(度量值组件)名称
     * @return
     */
    String getName();
}
