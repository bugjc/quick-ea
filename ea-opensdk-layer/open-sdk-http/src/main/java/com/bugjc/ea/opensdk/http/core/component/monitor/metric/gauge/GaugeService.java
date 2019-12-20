package com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.Gauged;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.Named;

/**
 * 度量值指标服务需要实现的接口
 * @author aoki
 * @date 2019/12/20
 * **/
public interface GaugeService<T> extends Named, Gauged<T> {
}
