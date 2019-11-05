package com.bugjc.ea.opensdk.http.core.util;

import com.bugjc.ea.opensdk.http.ApiBuilder;
import com.bugjc.ea.opensdk.http.model.AppParam;
import com.bugjc.ea.opensdk.http.service.HttpService;

/**
 * http api 客户端
 * @author aoki
 */
public class HttpClient {

    /**
     * 获取开放平台网关HTTP调用对象
     * @param appParam
     * @return
     */
    public static HttpService getHttpService(AppParam appParam) {
        return new ApiBuilder()
                .setAppParam(appParam)
                .setHttpConnTimeout(5000)
                .build();
    }
}
