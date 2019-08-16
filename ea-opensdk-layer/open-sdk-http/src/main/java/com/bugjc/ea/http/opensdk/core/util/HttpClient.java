package com.bugjc.ea.http.opensdk.core.util;

import com.bugjc.ea.http.opensdk.APIBuilder;
import com.bugjc.ea.http.opensdk.model.AppParam;
import com.bugjc.ea.http.opensdk.service.HttpService;

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
        return new APIBuilder()
                .setAppParam(appParam)
                .setHttpConnTimeout(5000)
                .build();
    }
}
