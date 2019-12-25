package com.bugjc.ea.opensdk.http.core.component.monitor.metric;

import com.codahale.metrics.MetricRegistry;

/**
 * 创建全局唯一度量注册器
 * @author aoki
 * @date 2019/12/19
 * **/
public class MetricRegistryFactory {

    private MetricRegistry registry = new MetricRegistry();

    private MetricRegistryFactory(){}

    enum SingletonEnum {
        //创建一个枚举对象，该对象天生为单例
        INSTANCE;
        private MetricRegistryFactory metricRegistryFactory;

        /**
         * 私有化枚举的构造函数
         */
        SingletonEnum() {
            metricRegistryFactory = new MetricRegistryFactory();
        }

        public MetricRegistryFactory getInstance() {
            return metricRegistryFactory;
        }
    }

    /**
     * 暴露获取实例的静态方法
     *
     * @return
     */
    public static MetricRegistryFactory getInstance() {
        return SingletonEnum.INSTANCE.getInstance();
    }

    /**
     * 获取 MetricRegistry 实例对象（单例）
     * @return
     */
    public MetricRegistry getRegistry(){
        return registry;
    }
}
