package com.bugjc.ea.opensdk.http.core.component.monitor.metric;

/**
 * 度量接口
 * @author aoki
 * @date 2019/12/18
 * **/
public interface Metric<T, E> {

    /**
     * 初始化指标
     * @param enumName    --定义指标的枚举类
     */
    void init(E enumName);

    /**
     * 获取度量实例
     * @param enumName      --指标定义的枚举类
     * @return  T
     */
    T get(E enumName);
}
