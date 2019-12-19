package com.bugjc.ea.opensdk.http.core.component.monitor.metric;

import com.bugjc.ea.opensdk.http.core.component.monitor.enums.MetricHistogramEnum;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.Hashtable;

/**
 * 度量数据的分布情况。
 * @author aoki
 * @date 2019/12/19
 * **/
public class HistogramMetric implements Metric<Histogram, MetricHistogramEnum> {

    private MetricRegistry registry;

    /**
     * 初始化 hashtable 用于存储 counter 实例
     */
    private static final Hashtable<String, Histogram> HASHTABLE = new Hashtable<>();

    @Inject
    public HistogramMetric(@Named("MetricHistogramEnum") MetricHistogramEnum[] metrics, MetricRegistry registry){
        this.registry = registry;
        //预加载度量实例
        for (MetricHistogramEnum metric : metrics) {
            this.init(metric);
        }

    }

    @Override
    public synchronized void init(MetricHistogramEnum enumName) {
        String name = enumName.getName();
        Histogram histogram = HASHTABLE.get(name);
        if (histogram != null){
            return;
        }
        HASHTABLE.put(name, registry.histogram(name));
    }

    @Override
    public Histogram get(MetricHistogramEnum enumName) {
        for (;;) {
            Histogram histogram = HASHTABLE.get(enumName.getName());
            if (histogram == null){
                init(enumName);
                continue;
            }
            return histogram;
        }
    }
}
