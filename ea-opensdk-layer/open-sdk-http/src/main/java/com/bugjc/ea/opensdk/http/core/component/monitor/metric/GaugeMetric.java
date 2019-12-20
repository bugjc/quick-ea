package com.bugjc.ea.opensdk.http.core.component.monitor.metric;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.GaugeKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.GaugeService;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;

import java.util.Hashtable;
import java.util.Set;

/**
 * 度量瞬时值(单例)
 * @author aoki
 * @date 2019/12/19
 * **/
public class GaugeMetric implements MetricGauge<Gauge, GaugeKey, GaugeService> {

    private MetricRegistry registry;
    /**
     * 初始化 hashtable 用于存储 counter 实例
     */
    private static final Hashtable<String, Gauge> HASHTABLE = new Hashtable<>();

    @Inject
    public GaugeMetric(Set<GaugeService> gaugeServices, MetricRegistry registry){
        this.registry = registry;

        //预加载度量实例
        for (GaugeService gaugeService : gaugeServices) {
            this.init(gaugeService);
        }
    }

    @Override
    public synchronized void init(GaugeService enumName) {
        String key = enumName.getKey();
        String name = enumName.getName();
        Gauge gauge = HASHTABLE.get(name);
        if (gauge != null){
            return;
        }
        Gauge metric = enumName.getGauge();
        HASHTABLE.put(key, registry.register(name, metric));
    }

    @Override
    public Gauge get(GaugeKey enumName) {
        return HASHTABLE.get(enumName.name());
    }
}
