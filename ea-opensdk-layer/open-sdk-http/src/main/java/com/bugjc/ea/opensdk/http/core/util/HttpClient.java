package com.bugjc.ea.opensdk.http.core.util;

import cn.hutool.core.lang.Singleton;
import com.bugjc.ea.opensdk.http.ApiBuilder;
import com.bugjc.ea.opensdk.http.model.AppInternalParam;
import com.bugjc.ea.opensdk.http.model.AppParam;
import com.bugjc.ea.opensdk.http.service.HttpService;

/**
 * http api 客户端
 * @author aoki
 */
public class HttpClient {

    private HttpService httpService;
    private HttpClient(){};

    /**
     * 获取服务实例
     * @param appParam          --接入应用调用接口协议所需参数
     * @return
     */
    public static HttpService getHttpService(AppParam appParam){
        return Singleton.get(HttpClient.class).getHttpServiceInstance(appParam, null);
    }

    /**
     * 获取服务调用实例对象
     * @param appParam          --接入应用调用接口协议所需参数
     * @return
     */
    public static HttpService getHttpService(AppParam appParam, AppInternalParam appInternalParam){
        return Singleton.get(HttpClient.class).getHttpServiceInstance(appParam, appInternalParam);
    }

    /**
     * 获取http服务对象
     * @return
     */
    private HttpService getHttpServiceInstance(AppParam appParam, AppInternalParam appInternalParam) {

        if (httpService == null){
            synchronized (this){
                if (httpService == null){
                    httpService = new ApiBuilder()
                            .setAppParam(appParam)
                            .setAppInternalParam(appInternalParam)
                            .setHttpConnTimeout(5000)
                            .build();
                }
            }
        }

        return httpService;
    }
}
