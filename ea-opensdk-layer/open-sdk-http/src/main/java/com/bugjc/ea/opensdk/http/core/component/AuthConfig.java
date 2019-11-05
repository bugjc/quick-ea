package com.bugjc.ea.opensdk.http.core.component;

import com.bugjc.ea.opensdk.http.service.HttpService;

import java.io.IOException;

/**
 * 平台认证服务配置
 * @author aoki
 * @date 2019/11/5
 * **/
public interface AuthConfig {

    /**
     * 设置 平台接口认证服务
     * @param httpService
     * @date 2019/11/5
     **/
    void setHttpService(HttpService httpService);

    /**
     * 设置 存储对象,默认内存存储方式不使用
     * @param storageObject
     */
    default void setStorageObject(Object storageObject){};
    /**
     * 获取 token
     * @return
     * @throws IOException
     * @date 2019/11/5
     */
    String getToken() throws IOException;
}
