package com.bugjc.ea.opensdk.http;

import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.http.core.component.eureka.EurekaConfig;
import com.bugjc.ea.opensdk.http.core.component.eureka.impl.EurekaDefaultConfigImpl;
import com.bugjc.ea.opensdk.http.core.component.token.AuthConfig;
import com.bugjc.ea.opensdk.http.core.component.token.impl.AuthDefaultConfigImpl;
import com.bugjc.ea.opensdk.http.core.component.token.impl.AuthRedisConfigImpl;
import com.bugjc.ea.opensdk.http.core.exception.ElementNotFoundException;
import com.bugjc.ea.opensdk.http.core.util.IpAddressUtil;
import com.bugjc.ea.opensdk.http.core.util.SSLUtil;
import com.bugjc.ea.opensdk.http.model.AppInternalParam;
import com.bugjc.ea.opensdk.http.model.AppParam;
import com.bugjc.ea.opensdk.http.service.HttpService;
import com.bugjc.ea.opensdk.http.service.impl.HttpServiceImpl;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * PerformanceTesting
 * 构建API
 * @author aoki
 */
@Slf4j
public class ApiBuilder {
    private AppParam appParam = null;
    private AppInternalParam appInternalParam = null;
    private HttpService httpService = new HttpServiceImpl();
    private OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
    private OkHttpClient httpClient;

    public ApiBuilder(){
        this.httpClientBuilder.connectTimeout(5L, TimeUnit.SECONDS).readTimeout(20L, TimeUnit.SECONDS);
    }

    /**
     * 设置超时时间
     * @param timeout
     * @return
     */
    public ApiBuilder setHttpConnTimeout(int timeout) {
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout should great than 0");
        } else {
            this.httpClientBuilder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
            return this;
        }
    }

    /**
     * 设置服务方接口基地址(必设)
     * @param appParam
     * @return
     */
    public ApiBuilder setAppParam(AppParam appParam) {
        this.appParam = appParam;
        this.httpService.setAppParam(this.appParam);
        return this;
    }

    /**
     * 设置服务方接口基地址(非必设)
     * @param appInternalParam
     * @return
     */
    public ApiBuilder setAppInternalParam(AppInternalParam appInternalParam) {
        this.appInternalParam = appInternalParam;
        this.httpService.setAppInternalParam(this.appInternalParam);
        return this;
    }

    /**
     * 构建http调用对象
     * @return
     */
    public HttpService build(){
        if (this.appParam == null){
            throw new ElementNotFoundException("app param object not set");
        } else if (StrUtil.isBlank(this.appParam.getBaseUrl())) {
            throw new ElementNotFoundException("base url  not set");
        } else if (StrUtil.isBlank(this.appParam.getRsaPrivateKey())) {
            throw new ElementNotFoundException("access party rsa private key not set");
        } else if (StrUtil.isBlank(this.appParam.getRsaPublicKey())) {
            throw new ElementNotFoundException("service party rsa public key not set");
        } else if (StrUtil.isBlank(this.appParam.getAppId())) {
            throw new ElementNotFoundException("app id not set");
        } else if (StrUtil.isBlank(this.appParam.getAppSecret())){
            throw new ElementNotFoundException("app secret not set");
        } else {
            //接入方设置了应用内部调用且接口基地址用的是内网地址
            if (appInternalParam != null && IpAddressUtil.internalIp(appParam.getBaseUrl())){
                if (appInternalParam.getJedisPool() == null){
                    throw new ElementNotFoundException("in app call does not set jedis");
                }

                //设置内部调用开关
                appInternalParam.setEnable(true);
                //设置eureka服务实例
                EurekaConfig eurekaConfig = EurekaDefaultConfigImpl.getInstance(appInternalParam.getJedisPool());
                this.httpService.setEurekaConfig(eurekaConfig);
                //初始化 eureka
                eurekaConfig.init();
            }

            //设置授权服务实现
            AuthConfig authConfig = AuthDefaultConfigImpl.getInstance(this.httpService);
            if (httpService.getAppParam().getJedisPool() != null){
                authConfig = AuthRedisConfigImpl.getInstance(this.httpService);
            }
            this.httpService.setAuthConfig(authConfig);

            //设置 http client 连接池
            if (this.httpClient == null) {
                this.httpClientBuilder.connectionPool(new ConnectionPool(2, 5L, TimeUnit.MINUTES));

                try {
                    this.httpClientBuilder.sslSocketFactory(SSLUtil.getAllTrustContext().getSocketFactory(), SSLUtil.getAllTrustManager());
                    this.httpClientBuilder.hostnameVerifier((s, sslSession) -> true);
                } catch (Exception var2) {
                    throw new RuntimeException(var2);
                }

                this.httpClient = this.httpClientBuilder.build();
            }

            this.httpService.setOkHttpClient(this.httpClient);
            return this.httpService;
        }
    }
}
