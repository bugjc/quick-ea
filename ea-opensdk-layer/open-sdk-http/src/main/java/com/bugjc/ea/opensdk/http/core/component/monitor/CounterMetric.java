package com.bugjc.ea.opensdk.http.core.component.monitor;

import com.bugjc.ea.opensdk.http.core.component.monitor.enums.TypeEnum;
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
public class CounterMetric implements Metric<Counter>{

    private MetricRegistry registry;
    private TypeEnum typeEnum;
    private Counter counter;
    private ConsoleReporter consoleReporter;
    /**
     * 初始化 hashtable 用于存储 counter 实例
     */
    private static final Hashtable<String, Counter> COUNTER_HASHTABLE = new Hashtable<>();

    @Inject
    public CounterMetric(@Named("TotalRequests") TypeEnum typeEnum, MetricRegistry registry){
        this.registry = registry;
        this.typeEnum = typeEnum;
        this.consoleReporter = ConsoleReporter.forRegistry(registry).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.SECONDS).build();

        //预加载度量计数器实例
        this.counter = COUNTER_HASHTABLE.get(typeEnum.name());
        this.get();
    }

    /**
     * 获取度量计数器
     * @return Counter
     */
    @Override
    public Counter get() {
        if (counter == null){
            synchronized (this){
                if (counter == null){
                    counter = registry.counter(typeEnum.name());
                    COUNTER_HASHTABLE.put(typeEnum.name(), counter);
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
