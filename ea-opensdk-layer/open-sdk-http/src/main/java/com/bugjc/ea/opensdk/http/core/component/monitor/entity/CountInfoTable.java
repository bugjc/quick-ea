package com.bugjc.ea.opensdk.http.core.component.monitor.entity;

import cn.hutool.core.util.NumberUtil;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.atomic.LongAdder;

/**
 * 统计信息
 * @author aoki
 * @date 2019/12/4
 * **/
public class CountInfoTable implements Serializable {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class CountInfo implements Serializable{

        /**
         * 调用总次数
         */
        private long total;

        /**
         * 调用成功数量
         */
        private long numberOfSuccesses;

        /**
         * 调用失败数量
         */
        private long numberOfFailures;

        /**
         * 成功率
         */
        private double successRate;

        /**
         * 失败率
         */
        private double failureRate;

        CountInfo(long numberOfSuccesses, long numberOfFailures){
            this.numberOfSuccesses = numberOfSuccesses;
            this.numberOfFailures = numberOfFailures;
            this.total = numberOfSuccesses + numberOfFailures;
            this.successRate = NumberUtil.div(this.numberOfSuccesses, this.total, 2);
            this.failureRate = NumberUtil.div(this.numberOfFailures, this.total,2);
        }
    }

    /**
     * 统计成功的调用次数
     */
    private final static LongAdder COUNT_NUMBER_OF_SUCCESSES = new LongAdder();

    /**
     * 统计失败的调用次数
     */
    private final static LongAdder COUNT_NUMBER_OF_FAILURES = new LongAdder();

    /**
     * 私有化构造函数
     */
    private CountInfoTable(){}

    /**
     * 定义一个静态枚举类
     */
    enum SingletonEnum {
        //创建一个枚举对象，该对象天生为单例
        INSTANCE;
        private CountInfoTable countInfoTable;

        /**
         * 私有化枚举的构造函数
         */
        SingletonEnum(){
            countInfoTable = new CountInfoTable();
        }

        public CountInfoTable getInstance(){
            return countInfoTable;
        }
    }

    /**
     * 暴露获取实例的静态方法
     * @return
     */
    public static CountInfoTable getInstance(){
        return SingletonEnum.INSTANCE.getInstance();
    }

    /**
     * 统计调用成功数量
     */
    public void increment(HttpCallEvent httpMetadata){
        if (httpMetadata.getType() != HttpCallEvent.TypeEnum.TotalRequests){
            return;
        }

        //只处理 TotalRequests 主题类型的数据
        if (httpMetadata.getStatus() == HttpCallEvent.StatusEnum.CallSuccess){
            COUNT_NUMBER_OF_SUCCESSES.increment();
        } else if (httpMetadata.getStatus() == HttpCallEvent.StatusEnum.CallFailed){
            COUNT_NUMBER_OF_FAILURES.increment();
        }

    }

    private long getSuccessNum(){
        return COUNT_NUMBER_OF_SUCCESSES.longValue();
    }

    private long getFailNum(){
        return COUNT_NUMBER_OF_FAILURES.longValue();
    }

    /**
     * 获取统计信息
     * @return
     */
    public CountInfo getCountInfo(){
        return new CountInfo(getSuccessNum(), getFailNum());
    }

}
