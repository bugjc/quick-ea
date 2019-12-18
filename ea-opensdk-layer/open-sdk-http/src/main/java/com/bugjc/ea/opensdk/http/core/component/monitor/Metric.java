package com.bugjc.ea.opensdk.http.core.component.monitor;

import java.util.concurrent.TimeUnit;

/**
 * 度量接口
 * @author aoki
 * @date 2019/12/18
 * **/
public interface Metric<T> {
    /**
     * 获取度量实例
     * @return
     */
    T get();

    /**
     * 打印结果
     * @param period
     * @param unit
     */
    void print(long period, TimeUnit unit);
}
