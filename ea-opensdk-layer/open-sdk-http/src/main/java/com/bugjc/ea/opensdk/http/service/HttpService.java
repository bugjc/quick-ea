package com.bugjc.ea.opensdk.http.service;

import com.bugjc.ea.opensdk.http.core.aop.AspectInterceptor;
import com.bugjc.ea.opensdk.http.core.component.eureka.EurekaConfig;
import com.bugjc.ea.opensdk.http.core.component.token.AuthConfig;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.exception.HttpSecurityException;
import com.bugjc.ea.opensdk.http.model.AppInternalParam;
import com.bugjc.ea.opensdk.http.model.AppParam;
import okhttp3.OkHttpClient;

/**
 * http 服务
 * @author aoki
 */
public interface HttpService {
    /********************************************* 获取开放服务接口调用实例 start ****************************************************************/
    /**
     * 获取平台认证服务实例
     * @return
     */
    AuthService getAuthService();

    /**
     * 获取任务调度服务实例
     * @return
     */
    JobService getJobService();

    /********************************************* 获取开放服务接口调用实例 end ****************************************************************/





    /********************************************* 设置接口调用需要的依赖服务或接入协议参数 start ****************************************************************/
    /**
     * 设置 http 客户端
     * @param okHttpClient
     */
    void setOkHttpClient(OkHttpClient okHttpClient);

    /**
     * 设置 eureka 服务实例
     * @param eurekaConfig
     */
    void setEurekaConfig(EurekaConfig eurekaConfig);

    /**
     * 设置授权服务实例
     * @param authConfig
     */
    void setAuthConfig(AuthConfig authConfig);

    /**
     * 设置应用接入参数
     * @param appParam
     */
    void setAppParam(AppParam appParam);

    /**
     * 设置内部应用所需的隐式参数
     * @param appInternalParam
     */
    void setAppInternalParam(AppInternalParam appInternalParam);

    /**
     * 获取应用接入参数
     * @return
     */
    AppParam getAppParam();

    /********************************************* 设置接口调用需要的依赖服务或接入协议参数 end ****************************************************************/





    /********************************************* 实际发起接口调用的方法 start ****************************************************************/
    /**
     * http post方式调用接口(自动获取token)
     * @param path       --接口地址
     * @param version    --接口版本
     * @param body       --接口参数
     * @return
     * @throws HttpSecurityException
     */
    @AspectInterceptor
    Result post(String path, String version, String body) throws HttpSecurityException;

    /**
     * http post方式调用接口（手动设置token）
     * @param path       --接口地址
     * @param version    --接口版本
     * @param token      --访问令牌
     * @param body       --接口参数
     * @return
     * @throws HttpSecurityException
     */
    @AspectInterceptor
    Result post(String path, String version, String token, String body) throws HttpSecurityException;

    /********************************************* 实际发起接口调用的方法 end ****************************************************************/
}
