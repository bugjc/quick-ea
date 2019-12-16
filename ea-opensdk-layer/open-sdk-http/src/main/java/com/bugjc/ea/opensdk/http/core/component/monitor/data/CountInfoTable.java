package com.bugjc.ea.opensdk.http.core.component.monitor.data;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.StatusEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.TypeEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEvent;
import com.bugjc.ea.opensdk.http.core.component.monitor.rocksdb.Cache;
import com.bugjc.ea.opensdk.http.core.component.monitor.rocksdb.RocksTTLDBCache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/**
 * 统计信息
 * @author aoki
 * @date 2019/12/4
 * **/
@Slf4j
public class CountInfoTable implements Serializable {

    private static final String DB_PATH = "D://data//count_info//";
    private static Cache cache;
    static {
        Map<Object, Object> conf = new HashMap<>();
        conf.put("rocksdb.root.dir", DB_PATH);
        Map<String,Integer> map = new HashMap<>();
        map.put("total", 10);
        conf.put("cfNames", map);

        cache = new RocksTTLDBCache();
        try {
            cache.init(conf);
        } catch (Exception e) {
            e.printStackTrace();
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


    private long getSuccessNum(){
        return COUNT_NUMBER_OF_SUCCESSES.longValue();
    }


    private long getFailNum(){
        return COUNT_NUMBER_OF_FAILURES.longValue();
    }

    /**
     * 统计数量
     */
    public void increment(HttpCallEvent httpCallEvent){
        if (httpCallEvent.getMetadata().getType() != TypeEnum.TotalRequests){
            return;
        }

        //只处理 TotalRequests 主题类型的数据
        if (httpCallEvent.getMetadata().getStatus() == StatusEnum.CallSuccess){
            COUNT_NUMBER_OF_SUCCESSES.increment();
        } else if (httpCallEvent.getMetadata().getStatus() == StatusEnum.CallFailed){
            COUNT_NUMBER_OF_FAILURES.increment();
        }

        //存储监控数据
        cache.put(httpCallEvent.getMetadata().getId(), httpCallEvent.getMetadata());
    }

    /**
     * 获取统计信息
     * @return
     */
    public CountInfo getCountInfo(){
        return new CountInfo(getSuccessNum(), getFailNum());
    }

    /**
     * 获取监控数据
     * @return
     */
    public long getValues(){
        cache.get("1");
        return cache.size();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class CountInfo implements Serializable{

        /**
         * 接口路径
         */
        private String path;

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

        /**
         * 调用信息
         * @param numberOfSuccesses
         * @param numberOfFailures
         */
        CountInfo(long numberOfSuccesses, long numberOfFailures){
            this.path = "/";
            this.numberOfSuccesses = numberOfSuccesses;
            this.numberOfFailures = numberOfFailures;
            this.total = numberOfSuccesses + numberOfFailures;
            if (this.total != 0){
                this.successRate = NumberUtil.div(this.numberOfSuccesses, this.total, 2);
                this.failureRate = NumberUtil.div(this.numberOfFailures, this.total,2);
            }
        }

        @Override
        public String toString(){
            return JSON.toJSONString(this);
        }
    }
}
