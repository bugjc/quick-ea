package com.bugjc.ea.opensdk;

import com.bugjc.ea.opensdk.core.util.SSLUtil;
import com.bugjc.ea.opensdk.model.AppParam;
import com.bugjc.ea.opensdk.service.HttpService;
import com.bugjc.ea.opensdk.service.impl.HttpServiceImpl;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.util.concurrent.TimeUnit;

public class APIBuilder {
    private AppParam appParam = new AppParam();
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
     * @param baseUrl
     * @return
     */
    public APIBuilder setBaseUrl(String baseUrl) {
        appParam.setBaseUrl(baseUrl);
        this.httpServiceImpl.setAppParam(appParam);
        return this;
    }

    /**
     * 设置服务方分配的应用ID
     * @param appId
     * @return
     */
    public APIBuilder setAppId(String appId) {
        appParam.setAppId(appId);
        this.httpServiceImpl.setAppParam(appParam);
        return this;
    }

    /**
     * 设置服务方分配的公钥
     * @param rsaPublicKey
     * @return
     */
    public APIBuilder setRsaPublicKey(String rsaPublicKey) {
        appParam.setRsaPublicKey(rsaPublicKey);
        this.httpServiceImpl.setAppParam(appParam);
        return this;
    }

    /**
     * 设置接入方生成的私钥
     * @param rsaPrivateKey
     * @return
     */
    public APIBuilder setRsaPrivateKey(String rsaPrivateKey) {
        appParam.setRsaPrivateKey(rsaPrivateKey);
        this.httpServiceImpl.setAppParam(appParam);
        return this;
    }

    /**
     * 构建http调用对象
     * @return
     */
    public HttpService build(){
        if (this.appParam.getBaseUrl() == null) {
            throw new IllegalStateException("base url  not set");
        } else if (this.appParam.getRsaPrivateKey() == null) {
            throw new IllegalStateException("access party rsa private key not set");
        } else if (this.appParam.getRsaPublicKey() == null) {
            throw new IllegalStateException("service party rsa public key not set");
        } else if (this.appParam.getAppId() == null) {
            throw new IllegalStateException("appid not set");
        } else {
            if (this.httpClient == null) {
                this.httpClientBuilder.connectionPool(new ConnectionPool(2, 3L, TimeUnit.MINUTES));

                try {
                    this.httpClientBuilder.sslSocketFactory(SSLUtil.getAllTrustContext().getSocketFactory(), SSLUtil.getAllTrustManager());
                    this.httpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String s, SSLSession sslSession) {
                            return true;
                        }
                    });
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
