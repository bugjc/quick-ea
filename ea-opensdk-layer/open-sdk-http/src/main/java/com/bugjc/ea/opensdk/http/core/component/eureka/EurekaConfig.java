package com.bugjc.ea.opensdk.http.core.component.eureka;

import com.bugjc.ea.opensdk.http.core.component.eureka.model.Server;

/**
 * eureka 配置
 * @author aoki
 * @date 2019/11/26
 * **/
public interface EurekaConfig {

    /**
     * 初始化 eureka 配置
     */
    void init();

    /**
     * 销毁 eureka 实例
     */
    void shutdown();

    /**
     * 设置 存储对象,默认读取 eureka 配置信息的 jedispool 连接池
     * @param storageObject
     */
    void setStorageObject(Object storageObject);

    /**
     * 根据接口路径获取一个服务，例如：path = /job/create
     * @param path
     * @return
     */
    Server chooseServer(String path);
}
