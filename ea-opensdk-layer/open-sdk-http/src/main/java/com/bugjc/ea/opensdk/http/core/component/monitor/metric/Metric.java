package com.bugjc.ea.opensdk.http.core.component.monitor.metric;

import java.util.concurrent.TimeUnit;

/**
 * 度量接口
 * @author aoki
 * @date 2019/12/18
 * **/
public interface Metric<T> {
    /**
     * 获取度量实例
     * @param name      --指标名
     * @return  T
     */
    T get(String name);

    /**
     * 打印结果
     * @param period        --间隔时间
     * @param unit          --时间单位
     */
    void print(long period, TimeUnit unit);
}
