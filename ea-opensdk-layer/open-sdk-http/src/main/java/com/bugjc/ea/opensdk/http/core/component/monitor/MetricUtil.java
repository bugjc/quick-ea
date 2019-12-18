package com.bugjc.ea.opensdk.http.core.component.monitor;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

/**
 * 度量工具类
 * @author aoki
 * @date 2019/12/18
 * **/
public class MetricUtil {
    /**
     * 度量注册中心
     */
    private static final MetricRegistry REGISTRY = new MetricRegistry();

    /**
     * 初始化 hashtable 用于存储 counter 实例
     */
    private static final Hashtable<String, Counter> COUNTER_HASHTABLE = new Hashtable<>();

    /**
     * 控制台打印监控数据
     */
    private static final ConsoleReporter REPORT = ConsoleReporter.forRegistry(REGISTRY)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    private MetricUtil(){}

    /**
     * 定义一个静态枚举类
     */
    enum SingletonEnum {
        //创建一个枚举对象，该对象天生为单例
        INSTANCE;
        private MetricUtil metricUtil;

        /**
         * 私有化枚举的构造函数
         */
        SingletonEnum(){
            metricUtil = new MetricUtil();
        }

        public MetricUtil getInstance(){
            return metricUtil;
        }

    }

    /**
     * 暴露获取实例的静态方法
     * @return
     */
    public static MetricUtil getInstance(){
        return SingletonEnum.INSTANCE.getInstance();
    }

    /**
     * 控制台打印度量报表
     * @param period        --间隔时间
     * @param unit          --时间单位
     */
    public static void consolePrint(long period, TimeUnit unit){
        REPORT.start(period, unit);
    }

    /**
     * 获取度量计数器
     * @param name      --指标别名
     * @return
     */
    public Counter getCounter(String name){
        Counter counter = COUNTER_HASHTABLE.get(name);
        if (counter == null){
            synchronized (this){
                if (counter == null){
                    counter = REGISTRY.counter(name);
                    COUNTER_HASHTABLE.put(name, counter);
                }
            }
        }
        return counter;
    }
    
    
}
