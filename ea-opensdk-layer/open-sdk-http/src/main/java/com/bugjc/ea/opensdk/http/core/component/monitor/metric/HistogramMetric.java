package com.bugjc.ea.opensdk.http.core.component.monitor.metric;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.HistogramKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.HistogramService;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;

import java.util.Hashtable;
import java.util.Set;

/**
 * 直方图
 * @author aoki
 * @date 2019/12/19
 * **/
public class HistogramMetric implements Metric<Histogram, HistogramKey, HistogramService> {

    private MetricRegistry registry;

    /**
     * 初始化 hashtable 用于存储 counter 实例
     */
    private static final Hashtable<String, Histogram> HASHTABLE = new Hashtable<>();

    @Inject
    public HistogramMetric(Set<HistogramService> histogramServices, MetricRegistry registry){
        this.registry = registry;
        //预加载度量实例
        for (HistogramService histogramService : histogramServices) {
            this.init(histogramService);
        }

    }

    @Override
    public synchronized void init(HistogramService enumName) {
        String key = enumName.getKey();
        String name = enumName.getName();
        Histogram histogram = HASHTABLE.get(name);
        if (histogram != null){
            return;
        }
        HASHTABLE.put(key, registry.histogram(name));
    }

    @Override
    public Histogram get(HistogramKey enumName) {
        return HASHTABLE.get(enumName.name());
    }
}
