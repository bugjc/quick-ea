package com.bugjc.ea.opensdk.http.core.component.monitor.metric;

/**
 * 类描述
 * 说明：T(度量组件)、K(显视指标名)、V(指标元数据，隐式指标名、度量值对象)
 * @author aoki
 * @date 2019/12/20
 * **/
public interface MetricGauge<T, K, V extends Gauged> extends Metric<T, K, V>{
}
