package com.bugjc.ea.opensdk.http.core.component.monitor.metric;

import com.bugjc.ea.opensdk.http.core.component.monitor.enums.MetricCounterEnum;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.Hashtable;

/**
 * 度量计数器(单例)
 * @author aoki
 * @date 2019/12/18
 * **/
public class CounterMetric implements Metric<Counter, MetricCounterEnum> {

    private MetricRegistry registry;

    /**
     * 初始化 hashtable 用于存储 counter 实例
     */
    private static final Hashtable<String, Counter> HASHTABLE = new Hashtable<>();

    @Inject
    public CounterMetric(@Named("MetricCounterEnum") MetricCounterEnum[] metrics, MetricRegistry registry){
        this.registry = registry;

        //预加载度量实例
        for (MetricCounterEnum metric : metrics) {
            this.init(metric);
        }

    }

    @Override
    public synchronized void init(MetricCounterEnum enumName) {
        String name = enumName.getName();
        Counter counter = HASHTABLE.get(name);
        if (counter != null){
            return;
        }
        HASHTABLE.put(name, registry.counter(name));
    }

    /**
     * 获取度量计数器
     * @return Counter
     */
    @Override
    public Counter get(MetricCounterEnum name) {
        for (;;) {
            Counter counter = HASHTABLE.get(name.getName());
            if (counter == null){
                init(name);
                continue;
            }
            return counter;
        }
    }
}
