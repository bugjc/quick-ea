package com.bugjc.ea.opensdk.http.core.component.monitor;

import cn.hutool.core.collection.BoundedPriorityQueue;
import com.bugjc.ea.opensdk.http.core.component.monitor.entity.HttpMetadata;

/**
 * @author aoki
 */
public class HttpMonitorDataManage {

    /**
     * 队列
     */
    private BoundedPriorityQueue<HttpMetadata> dataQueue = new BoundedPriorityQueue<HttpMetadata>(5000, (o1, o2) -> o1 == o2 ? 0 : -1);

    /**
     * 私有化构造函数
     */
    private HttpMonitorDataManage(){}

    /**
     * 定义一个静态枚举类
     */
    enum SingletonEnum {
        //创建一个枚举对象，该对象天生为单例
        INSTANCE;
        private HttpMonitorDataManage httpMonitorDataManage;

        /**
         * 私有化枚举的构造函数
         */
        SingletonEnum(){
            httpMonitorDataManage = new HttpMonitorDataManage();
        }

        public HttpMonitorDataManage getInstance(){
            return httpMonitorDataManage;
        }
    }

    /**
     * 暴露获取实例的静态方法
     * @return
     */
    public static HttpMonitorDataManage getInstance(){
        return SingletonEnum.INSTANCE.getInstance();
    }



    /**
     * 收集监控数据
     * @param metadata
     * @return
     */
    boolean add(HttpMetadata metadata){
        return dataQueue.offer(metadata);
    }

    /**
     * 读取监控数据
     * @return
     */
    HttpMetadata poll(){
        return dataQueue.poll();
    }
}
