package com.bugjc.ea.opensdk.http;

import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.http.core.util.SSLUtil;
import com.bugjc.ea.opensdk.http.model.AppParam;
import com.bugjc.ea.opensdk.http.service.AuthService;
import com.bugjc.ea.opensdk.http.service.HttpService;
import com.bugjc.ea.opensdk.http.service.JobService;
import com.bugjc.ea.opensdk.http.service.impl.AuthServiceImpl;
import com.bugjc.ea.opensdk.http.service.impl.HttpServiceImpl;
import com.bugjc.ea.opensdk.http.service.impl.JobServiceImpl;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;

/**
 * PerformanceTesting
 * 构建API
 * @author aoki
 */
public class ApiBuilder {
    private AppParam appParam = null;
    private HttpServiceImpl httpServiceImpl = new HttpServiceImpl();
    private JobServiceImpl jobServiceImpl = new JobServiceImpl();
    private AuthServiceImpl authServiceImpl = new AuthServiceImpl();
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
            this.httpClientBuilder.connectTimeout((long)timeout, TimeUnit.MILLISECONDS);
            return this;
        }
    }

    /**
     * 设置服务方接口基地址
     * @param appParam
     * @return
     */
    public ApiBuilder setAppParam(AppParam appParam) {
        this.appParam = appParam;
        this.httpServiceImpl.setAppParam(this.appParam);
        return this;
    }

    /**
     * 设置服务方接口调用“凭证”保存到 redis,不设置默认保存到本地内存中(非必填)
     * @param jedisPool
     * @return
     */
    public ApiBuilder setJedisPool(JedisPool jedisPool) {
        this.httpServiceImpl.setJedisPool(jedisPool);
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

            this.httpServiceImpl.setHttpClient(this.httpClient);
            return this.httpServiceImpl;
        }
    }

    /**
     * 构建 任务调度 API调用对象
     * @return
     */
    public JobService buildJobApi(){
        jobServiceImpl.setHttpService(this.build());
        return jobServiceImpl;
    }

    /**
     * 构建 平台认证 API调用对象
     * @return
     */
    public AuthService buildAuthApi(){
        authServiceImpl.setHttpService(this.build());
        return authServiceImpl;
    }

}
