package com.bugjc.ea.opensdk.http.core.util;

import com.bugjc.ea.opensdk.http.ApiBuilder;
import com.bugjc.ea.opensdk.http.model.AppInternalParam;
import com.bugjc.ea.opensdk.http.model.AppParam;
import com.bugjc.ea.opensdk.http.service.AuthService;
import com.bugjc.ea.opensdk.http.service.HttpService;
import com.bugjc.ea.opensdk.http.service.JobService;

/**
 * http api 客户端
 * @author aoki
 */
public class HttpClient {

    private HttpService httpService;
    private AuthService authService;
    private JobService jobService;
    private HttpClient(){};

    /**
     * 定义一个静态枚举类
     */
    enum SingletonEnum {
        //创建一个枚举对象，该对象天生为单例
        INSTANCE,
        //创建一个缓存实例对象
        CACHE_INSTANCE,
        //创建一个锁重入实例对象
        LOCK_INSTANCE;
        private HttpClient httpClient;

        /**
         * 私有化枚举的构造函数
         */
        SingletonEnum(){
            httpClient = new HttpClient();
        }

        public HttpClient getInstance(){
            return httpClient;
        }

    }

    /**
     * 暴露获取实例的静态方法
     * @return
     */
    public static HttpClient getInstance(){
        return SingletonEnum.INSTANCE.getInstance();
    }

    /**
     * 获取服务实例
     * @param appParam          --接入应用调用接口协议所需参数
     * @return
     */
    public HttpService getHttpService(AppParam appParam){
        return getHttpServiceInstance(appParam, null);
    }

    /**
     * 获取服务调用实例对象
     * @param appParam          --接入应用调用接口协议所需参数
     * @return
     */
    public HttpService getHttpService(AppParam appParam, AppInternalParam appInternalParam){
        return getHttpServiceInstance(appParam, appInternalParam);
    }


    /**
     * 获取服务实例
     * @param appParam          --接入应用调用接口协议所需参数
     * @return
     */
    public AuthService getAuthService(AppParam appParam){
        return getAuthServiceInstance(appParam, null);
    }

    /**
     * 获取服务调用实例对象
     * @param appParam          --接入应用调用接口协议所需参数
     * @return
     */
    public AuthService getAuthService(AppParam appParam, AppInternalParam appInternalParam){
        return getAuthServiceInstance(appParam, appInternalParam);
    }

    /**
     * 获取服务实例
     * @param appParam          --接入应用调用接口协议所需参数
     * @return
     */
    public JobService getJobService(AppParam appParam){
        return getJobServiceInstance(appParam, null);
    }

    /**
     * 获取服务调用实例对象
     * @param appParam          --接入应用调用接口协议所需参数
     * @return
     */
    public JobService getJobService(AppParam appParam, AppInternalParam appInternalParam){
        return getJobServiceInstance(appParam, appInternalParam);
    }

    /**
     * 获取http服务对象
     * @return
     */
    private HttpService getHttpServiceInstance(AppParam appParam, AppInternalParam appInternalParam) {

        if (httpService == null){
            synchronized (this){
                if (httpService == null){
                    httpService = new ApiBuilder()
                            .setAppParam(appParam)
                            .setAppInternalParam(appInternalParam)
                            .setHttpConnTimeout(5000)
                            .build();
                }
            }
        }

        return httpService;
    }

    /**
     * 获取http服务对象
     * @return
     */
    private AuthService getAuthServiceInstance(AppParam appParam, AppInternalParam appInternalParam) {

        if (authService == null){
            synchronized (this){
                if (authService == null){
                    authService = new ApiBuilder()
                            .setAppParam(appParam)
                            .setAppInternalParam(appInternalParam)
                            .setHttpConnTimeout(5000)
                            .buildAuthService();
                }
            }
        }

        return authService;
    }


    /**
     * 获取http服务对象
     * @return
     */
    private JobService getJobServiceInstance(AppParam appParam, AppInternalParam appInternalParam) {

        if (jobService == null){
            synchronized (this){
                if (jobService == null){
                    jobService = new ApiBuilder()
                            .setAppParam(appParam)
                            .setAppInternalParam(appInternalParam)
                            .setHttpConnTimeout(5000)
                            .buildJobService();
                }
            }
        }

        return jobService;
    }
}
