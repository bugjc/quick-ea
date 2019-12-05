package com.bugjc.ea.opensdk.http.core.component.monitor.entity;

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
    public static class CountInfo implements Serializable{
        /**
         * 调用成功数量
         */
        private long successNum;

        /**
         * 调用失败数量
         */
        private long failNum;
    }

    private final LongAdder SUCCESS_NUM_COUNT = new LongAdder();
    private final LongAdder FAIL_NUM_COUNT = new LongAdder();

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
            SUCCESS_NUM_COUNT.increment();
        } else if (httpMetadata.getStatus() == HttpCallEvent.StatusEnum.CallFailed){
            FAIL_NUM_COUNT.increment();
        }

    }

    private long getSuccessNum(){
        return SUCCESS_NUM_COUNT.longValue();
    }

    private long getFailNum(){
        return FAIL_NUM_COUNT.longValue();
    }

    /**
     * 获取统计信息
     * @return
     */
    public CountInfo getCountInfo(){
        return new CountInfo(getSuccessNum(), getFailNum());
    }

}
