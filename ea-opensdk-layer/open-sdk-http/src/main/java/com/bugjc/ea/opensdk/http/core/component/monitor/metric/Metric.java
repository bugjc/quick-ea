package com.bugjc.ea.opensdk.http.core.component.monitor.metric;

/**
 * 度量接口
 * 说明：T(度量组件)、K(显视指标名)、V(指标元数据，隐式指标名)
 * @author aoki
 * @date 2019/12/18
 * **/
public interface Metric<T, K, V extends Named> {

    /**
     * 初始化指标
     * @param enumName    --定义指标的枚举类
     */
    void init(V enumName);

    /**
     * 获取度量实例
     * @param enumName      --指标定义的枚举类
     * @return  T
     */
    T get(K enumName);
}
