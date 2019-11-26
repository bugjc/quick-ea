package com.bugjc.ea.opensdk.http;

import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.http.core.component.eureka.impl.EurekaDefaultConfigImpl;
import com.bugjc.ea.opensdk.http.core.util.IpAddressUtil;
import com.bugjc.ea.opensdk.http.core.util.SSLUtil;
import com.bugjc.ea.opensdk.http.model.AppParam;
import com.bugjc.ea.opensdk.http.service.HttpService;
import com.bugjc.ea.opensdk.http.service.impl.HttpServiceImpl;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;

/**
 * PerformanceTesting
 * 构建API
 * @author aoki
 */
@Slf4j
public class ApiBuilder {
    private AppParam appParam = null;
    private JedisPool jedisPool = null;
    private HttpService httpService = new HttpServiceImpl();
    private OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
    private OkHttpClient httpClient;

    public ApiBuilder(){
        this.httpClientBuilder.connectTimeout(30L, TimeUnit.SECONDS).readTimeout(30L, TimeUnit.SECONDS);
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
     * 设置服务方接口调用“凭证”保存到 redis,不设置默认保存到本地内存中
     * @param jedisPool
     * @return
     */
    public ApiBuilder setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        this.httpService.setJedisPool(jedisPool);
        return this;
    }

    /**
     * 构建http调用对象
     * @return
     */
    public HttpService build(){
        if (this.appParam == null){
            throw new IllegalStateException("app param object not set");
        } else if (StrUtil.isBlank(this.appParam.getBaseUrl())) {
            throw new IllegalStateException("base url  not set");
        } else if (StrUtil.isBlank(this.appParam.getRsaPrivateKey())) {
            throw new IllegalStateException("access party rsa private key not set");
        } else if (StrUtil.isBlank(this.appParam.getRsaPublicKey())) {
            throw new IllegalStateException("service party rsa public key not set");
        } else if (StrUtil.isBlank(this.appParam.getAppId())) {
            throw new IllegalStateException("app id not set");
        } else if (StrUtil.isBlank(this.appParam.getAppSecret())){
            throw new IllegalStateException("app secret not set");
        } else {

            boolean flag = IpAddressUtil.internalIp(appParam.getBaseUrl());
            if (flag){
                if (jedisPool == null){
                    log.warn("not configured jedis pool");
                }else {
                    //TODO 检查 eureka-client.properties 文件是否存在
                    // 内部调用 初始化Eureka Client，TODO eureka-client.properties 文件
                    EurekaDefaultConfigImpl.getInstance(jedisPool).init();
                }
            }

            if (this.httpClient == null) {
                this.httpClientBuilder.connectionPool(new ConnectionPool(2, 3L, TimeUnit.MINUTES));

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
