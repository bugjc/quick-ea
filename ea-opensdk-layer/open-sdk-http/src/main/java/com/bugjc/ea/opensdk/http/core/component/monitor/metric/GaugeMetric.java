//package com.bugjc.ea.opensdk.http.core.component.monitor.metric;
//
//import com.bugjc.ea.opensdk.http.core.component.monitor.metric.index.MetricGaugeIndex;
//import com.codahale.metrics.Gauge;
//import com.codahale.metrics.MetricRegistry;
//import com.google.inject.Inject;
//
//import java.util.Hashtable;
//
///**
// * 度量瞬时值(单例)
// * @author aoki
// * @date 2019/12/19
// * **/
//public class GaugeMetric implements Metric<Gauge, MetricGaugeIndex> {
//
//    private MetricRegistry registry;
//    /**
//     * 初始化 hashtable 用于存储 counter 实例
//     */
//    private static final Hashtable<String, Gauge> HASHTABLE = new Hashtable<>();
//
//    @Inject
//    public GaugeMetric(MetricGaugeIndex metricGauge, MetricRegistry registry){
//        this.registry = registry;
//
//        //预加载度量实例
//        for (MetricGaugeIndex metric : metricGauge.getList()) {
//            this.init(metric);
//        }
//    }
//
//    @Override
//    public void init(MetricGaugeIndex enumName) {
//        String name = enumName.getName();
//        Gauge gauge = HASHTABLE.get(name);
//        if (gauge != null){
//            return;
//        }
//        Gauge metric = enumName.getGauge();
//        HASHTABLE.put(name, registry.register(name, metric));
//    }
//
//    @Override
//    public Gauge get(MetricGaugeIndex enumName) {
//        for (;;) {
//            Gauge gauge = HASHTABLE.get(enumName.getName());
//            if (gauge == null){
//                init(enumName);
//                continue;
//            }
//            return gauge;
//        }
//    }
//}
