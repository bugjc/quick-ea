package com.bugjc.ea.opensdk.http.core.component.monitor.metric.index;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建 Gauged 类别的监控实例(单例)
 *
 * @author aoki
 * @date 2019/12/19
 **/

public class MetricGaugeIndex {

    private MetricGaugeIndex() {
    }

    private static class SingletonClassInstance {
        private static final MetricGaugeIndex INSTANCE = new MetricGaugeIndex();
    }


    public static MetricGaugeIndex getInstance() {
        return SingletonClassInstance.INSTANCE;
    }

    private static final Map<MetricGaugeIndex.MetricGaugeEnum, MetricGauge> MAP = new HashMap<>();

    @Data
    public static class MetricGauge implements Named {
        private String name;
        private Gauge gauge;

        /**
         * 内部转换map to list需要
         *
         * @param name
         * @param gauge
         */
        private MetricGauge(String name, Gauge gauge) {
            this.name = name;
            this.gauge = gauge;
        }

        /**
         * 获取指标名称
         *
         * @return
         */
        @Override
        public String getName() {
            return MetricRegistry.name(MetricGauge.class, this.name);
        }
    }

    public enum MetricGaugeEnum {
        /**
         * 统计成功率
         */
        RequestSuccessRatio;
    }

    @Inject
    private MetricGaugeIndex(Map<MetricGaugeIndex.MetricGaugeEnum, Gauge> gaugeMap) {
        if (gaugeMap == null || gaugeMap.size() == 0) {
            return;
        }

        for (Map.Entry<MetricGaugeIndex.MetricGaugeEnum, Gauge> gaugeEntry : gaugeMap.entrySet()) {
            MAP.put(gaugeEntry.getKey(), new MetricGauge(gaugeEntry.getKey().name(), gaugeEntry.getValue()));
        }

    }

    /**
     * 获取指标集合
     *
     * @return
     */
    public List<MetricGauge> getList() {
        return new ArrayList<>(MAP.values());
    }

    /**
     * 获取指标元数据
     *
     * @param key
     * @return
     */
    public MetricGauge get(MetricGaugeEnum key) {
        return MAP.get(key);
    }
}
