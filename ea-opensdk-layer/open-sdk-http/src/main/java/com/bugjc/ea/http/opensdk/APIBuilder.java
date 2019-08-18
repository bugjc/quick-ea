package com.bugjc.ea.http.opensdk;

import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.http.opensdk.core.util.SSLUtil;
import com.bugjc.ea.http.opensdk.model.AppParam;
import com.bugjc.ea.http.opensdk.service.HttpService;
import com.bugjc.ea.http.opensdk.service.impl.HttpServiceImpl;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.util.concurrent.TimeUnit;

/**
 * 构建API
 * @author aoki
 */
public class APIBuilder {
    private AppParam appParam = null;
    private HttpServiceImpl httpServiceImpl = new HttpServiceImpl();
    private OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
    private OkHttpClient httpClient;

    public APIBuilder(){
        this.httpClientBuilder.connectTimeout(30L, TimeUnit.SECONDS).readTimeout(30L, TimeUnit.SECONDS);
    }

    /**
     * 设置超时时间
     * @param timeout
     * @return
     */
    public APIBuilder setHttpConnTimeout(int timeout) {
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
    public APIBuilder setAppParam(AppParam appParam) {
        this.appParam = appParam;
        this.httpServiceImpl.setAppParam(this.appParam);
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

}
