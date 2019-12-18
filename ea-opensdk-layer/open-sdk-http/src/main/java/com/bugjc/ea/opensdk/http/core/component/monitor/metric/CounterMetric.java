package com.bugjc.ea.opensdk.http.core.component.monitor.metric;

import com.bugjc.ea.opensdk.http.core.component.monitor.util.StrUtil;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

/**
 * 度量计数器
 * @author aoki
 * @date 2019/12/18
 * **/
public class CounterMetric implements Metric<Counter> {

    private MetricRegistry registry;
    private ConsoleReporter consoleReporter;
    /**
     * 初始化 hashtable 用于存储 counter 实例
     */
    private static final Hashtable<String, Counter> COUNTER_HASHTABLE = new Hashtable<>();

    @Inject
    public CounterMetric(@Named("MetricCounterEnum") String metricCounterStr, MetricRegistry registry){
        this.registry = registry;
        this.consoleReporter = ConsoleReporter.forRegistry(registry).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.SECONDS).build();

        //预加载度量计数器实例
        for (String metricCounterName : StrUtil.arrStrToList(metricCounterStr)) {
            this.get(metricCounterName);
        }

    }

    /**
     * 获取度量计数器
     * @return Counter
     */
    @Override
    public Counter get(String name) {
        Counter counter = COUNTER_HASHTABLE.get(name);
        if (counter == null){
            synchronized (this){
                if (counter == null){
                    counter = registry.counter(name);
                    COUNTER_HASHTABLE.put(name, counter);
                }
            }
        }
        return counter;
    }

    /**
     * 打印结果
     * @param period        --间隔时间
     * @param unit          --时间单位
     */
    @Override
    public void print(long period, TimeUnit unit) {
        consoleReporter.start(period, unit);
    }
}
