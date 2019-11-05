package com.bugjc.ea.opensdk.http.service;

import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.model.AppParam;
import okhttp3.OkHttpClient;
import redis.clients.jedis.JedisPool;

import java.io.IOException;

/**
 * http 服务
 * @author aoki
 */
public interface HttpService {

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

    /**
     * 设置 http 客户端
     * @param okHttpClient
     */
    void setOkHttpClient(OkHttpClient okHttpClient);

    /**
     * 设置应用接入参数
     * @param appParam
     */
    void setAppParam(AppParam appParam);

    /**
     * 获取应用接入参数
     * @return
     */
    AppParam getAppParam();

    /**
     * 设置 redis 连接池
     * @param jedisPool
     */
    void setJedisPool(JedisPool jedisPool);

    /**
     * http post方式调用接口(自动获取token)
     * @param path       --接口地址
     * @param version    --接口版本
     * @param body       --接口参数
     * @return
     * @throws IOException
     */
    Result post(String path, String version, String body) throws IOException;

    /**
     * http post方式调用接口（手动设置token）
     * @param path       --接口地址
     * @param version    --接口版本
     * @param token      --访问令牌
     * @param body       --接口参数
     * @return
     * @throws IOException
     */
    Result post(String path, String version,String token,String body) throws IOException;
}
