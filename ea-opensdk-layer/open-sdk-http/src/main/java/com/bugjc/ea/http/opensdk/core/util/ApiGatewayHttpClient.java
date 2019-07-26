package com.bugjc.ea.http.opensdk.core.util;

import com.bugjc.ea.http.opensdk.APIBuilder;
import com.bugjc.ea.http.opensdk.model.AppParam;
import com.bugjc.ea.http.opensdk.service.HttpService;

public class ApiGatewayHttpClient {

    /**
     * 获取开放平台网关HTTP调用对象
     * @param appParam
     * @return
     */
    public static HttpService getHttpService(AppParam appParam) {
        return new APIBuilder()
                .setAppId(appParam.getAppId())
                .setBaseUrl(appParam.getBaseUrl())
                .setRsaPrivateKey(appParam.getRsaPrivateKey())
                .setRsaPublicKey(appParam.getRsaPublicKey())
                .setHttpConnTimeout(5000)
                .build();
    }
}