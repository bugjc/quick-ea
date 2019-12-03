package com.bugjc.ea.opensdk.http.service;

import com.bugjc.ea.opensdk.http.core.aop.ProxyUtil;
import com.bugjc.ea.opensdk.http.core.component.monitor.HttpCallAspect;

public class HttpServiceFactory {

    /**
     * 创建 http service 代理切面，显式定义
     * @param httpService
     * @return
     */
    public static HttpService createProxy(HttpService httpService){
        return (HttpService) ProxyUtil.createProxy(httpService, HttpCallAspect.class);
    }
}
