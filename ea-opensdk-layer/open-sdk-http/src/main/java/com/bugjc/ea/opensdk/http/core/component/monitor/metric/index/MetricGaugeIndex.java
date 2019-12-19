//package com.bugjc.ea.opensdk.http.core.component.monitor.metric.index;
//
//import com.bugjc.ea.opensdk.http.core.component.monitor.enums.MetricCounterEnum;
//import com.codahale.metrics.Gauge;
//import com.codahale.metrics.MetricRegistry;
//import com.google.inject.Inject;
//import lombok.Data;
//import lombok.Getter;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
///**
// * 创建 Gauge 类别的监控实例(单例)
// * @author aoki
// * @date 2019/12/19
// * **/
//@Data
//public class MetricGaugeIndex {
//
//    private String name;
//    private Gauge gauge;
//
//    @Getter
//    private List<MetricGaugeIndex> list;
//
//    @Inject
//    private MetricGaugeIndex(Map<String, Gauge> gaugeMap){
//        this.list.addAll(toList(gaugeMap));
//    }
//
//    /**
//     * 获取指标及监控实例
//     * @return
//     */
//    private List<MetricGaugeIndex> toList(Map<String, Gauge> gaugeMap){
//        if (gaugeMap == null || gaugeMap.size() == 0){
//            return Collections.emptyList();
//        }
//
//        List<MetricGaugeIndex> gaugeList = new ArrayList<>();
//        for (Map.Entry<String, Gauge> gaugeEntry : gaugeMap.entrySet()) {
//            gaugeList.add(new MetricGaugeIndex(gaugeEntry.getKey(), gaugeEntry.getValue()));
//        }
//        return gaugeList;
//    }
//
//    /**
//     * 内部转换map to list需要
//     * @param name
//     * @param gauge
//     */
//    private MetricGaugeIndex(String name, Gauge gauge) {
//        this.name = name;
//        this.gauge = gauge;
//    }
//
//    /**
//     * 获取指标名称
//     * @return
//     */
//    public String getName(){
//        return MetricRegistry.name(MetricCounterEnum.class, this.name);
//    }
//}
