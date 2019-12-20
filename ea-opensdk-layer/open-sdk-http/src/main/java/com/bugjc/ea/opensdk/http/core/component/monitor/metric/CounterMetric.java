package com.bugjc.ea.opensdk.http.core.component.monitor.metric;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterService;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;

import java.util.Hashtable;
import java.util.Set;

/**
 * 度量计数器(单例)
 * @author aoki
 * @date 2019/12/18
 * **/
public class CounterMetric implements Metric<Counter, CounterKey, CounterService> {

    private MetricRegistry registry;

    /**
     * 初始化 hashtable 用于存储 counter 实例
     */
    private static final Hashtable<String, Counter> HASHTABLE = new Hashtable<>();

    @Inject
    public CounterMetric(Set<CounterService> counterServices, MetricRegistry registry){
        this.registry = registry;

        //预加载度量实例
        for (CounterService counterService : counterServices) {
            this.init(counterService);
        }

    }

    @Override
    public synchronized void init(CounterService enumName) {
        String key = enumName.getKey();
        String name = enumName.getName();
        Counter counter = HASHTABLE.get(name);
        if (counter != null){
            return;
        }
        HASHTABLE.put(key, registry.counter(name));
    }

    /**
     * 获取度量计数器
     * @return Counter
     */
    @Override
    public Counter get(CounterKey enumName) {
        return HASHTABLE.get(enumName.name());
    }
}
