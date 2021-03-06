package com.bugjc.ea.opensdk.http.core.component.eureka;

import com.bugjc.ea.opensdk.http.core.component.eureka.entity.Server;

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
     * 根据接口路径获取一个服务，例如：path = /job/create
     * @param path
     * @return
     */
    Server chooseServer(String path);
}
