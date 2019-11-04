package com.bugjc.ea.opensdk.http.core.util;

import com.bugjc.ea.opensdk.http.ApiBuilder;
import com.bugjc.ea.opensdk.http.model.AppParam;
import com.bugjc.ea.opensdk.http.service.AuthService;
import com.bugjc.ea.opensdk.http.service.HttpService;
import com.bugjc.ea.opensdk.http.service.JobService;

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

    /**
     * 获取开放平台网关平台授权认证服务 HTTP 调用对象
     * @param appParam
     * @return
     */
    public static AuthService getAuthService(AppParam appParam) {
        return new ApiBuilder()
                .setAppParam(appParam)
                .setHttpConnTimeout(5000)
                .buildAuthApi();
    }

    /**
     * 获取开放平台网关任务调度服务 HTTP 调用对象
     * @param appParam
     * @return
     */
    public static JobService getJobService(AppParam appParam) {
        return new ApiBuilder()
                .setAppParam(appParam)
                .setHttpConnTimeout(5000)
                .buildJobApi();
    }
}
