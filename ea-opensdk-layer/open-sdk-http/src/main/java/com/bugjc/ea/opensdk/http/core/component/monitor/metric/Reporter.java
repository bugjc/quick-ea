package com.bugjc.ea.opensdk.http.core.component.monitor.metric;

import com.codahale.metrics.ConsoleReporter;

import java.util.concurrent.TimeUnit;

/**
 * 数据报表
 * @author aoki
 * @date 2019/12/19
 * **/
public class Reporter {

    private Reporter(){}

    enum SingletonEnum {
        //创建一个枚举对象，该对象天生为单例
        INSTANCE;
        private Reporter reporter;

        /**
         * 私有化枚举的构造函数
         */
        SingletonEnum() {
            reporter = new Reporter();
        }

        public Reporter getInstance() {
            return reporter;
        }
    }

    /**
     * 暴露获取实例的静态方法
     *
     * @return
     */
    public static Reporter getInstance() {
        return SingletonEnum.INSTANCE.getInstance();
    }

    /**
     * 控制台输出报表
     * @return 默认返回输出间隔为“秒”单位
     */
    public ConsoleReporter consoleReporter(){
        return ConsoleReporter.forRegistry(MetricRegistryFactory.getInstance().getRegistry()).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.SECONDS).build();
    }
}
