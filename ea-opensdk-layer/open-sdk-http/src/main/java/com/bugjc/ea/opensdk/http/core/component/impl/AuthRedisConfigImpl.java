package com.bugjc.ea.opensdk.http.core.component.impl;

import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.opensdk.http.api.AuthPathInfo;
import com.bugjc.ea.opensdk.http.core.component.AccessTokenConstants;
import com.bugjc.ea.opensdk.http.core.component.AuthConfig;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.enums.TokenResultStatusEnum;
import com.bugjc.ea.opensdk.http.model.auth.QueryTokenBody;
import com.bugjc.ea.opensdk.http.service.HttpService;
import com.github.jedis.lock.JedisLock;
import lombok.Setter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 平台认证服务 redis 实现
 * @author aoki
 * @date 2019/11/5
 * **/
public class AuthRedisConfigImpl implements AuthConfig {

    /**
     * 远程 redis存储 token
     */
    private JedisPool redisStorage;

    /**
     *  平台接口授权服务 http客户端
     */
    @Setter
    private HttpService httpService;

    /**
     * 私有化构造函数
     */
    private AuthRedisConfigImpl(){}

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
        private AuthConfig authConfig;
        private Lock accessTokenLock;

        /**
         * 私有化枚举的构造函数
         */
        SingletonEnum(){
            authConfig = new AuthRedisConfigImpl();
            accessTokenLock = new ReentrantLock();
        }

        public AuthConfig getInstance(){
            return authConfig;
        }

        public Lock getLockInstance(){
            return accessTokenLock;
        }
    }

    /**
     * 暴露获取实例的静态方法
     * @return
     */
    public static AuthConfig getInstance(HttpService httpService, JedisPool redisStorage){
        AuthConfig authConfig = AuthRedisConfigImpl.SingletonEnum.INSTANCE.getInstance();
        authConfig.setHttpService(httpService);
        authConfig.setStorageObject(redisStorage);
        return authConfig;
    }


    @Override
    public void setStorageObject(Object storageObject) {
        this.redisStorage = (JedisPool) storageObject;
    }

    @Override
    public String getToken() {
        if (httpService == null){
            return null;
        }

        if (redisStorage == null){
            return null;
        }

        String appId = httpService.getAppParam().getAppId();

        try (Jedis jedis = redisStorage.getResource()) {
            //从redis 中获取 token
            String token = jedis.get(AccessTokenConstants.getKey(appId));
            if (token != null){
                return token;
            }

            //创建分布式锁对象
            JedisLock lock = new JedisLock(jedis, appId, 3000, 12000);

            try {
                //获取锁
                lock.acquire();
                token = jedis.get(AccessTokenConstants.getKey(appId));
                if (token != null){
                    return token;
                }

                //获取 token 重试次数
                int retryCount = 3;
                Result result =  httpService.getAuthService().getToken(AuthPathInfo.QUERY_TOKEN_V1);
                while (result.getCode() == TokenResultStatusEnum.Retry.getStatus()){
                    retryCount--;
                    result = httpService.getAuthService().getToken(AuthPathInfo.QUERY_TOKEN_V1);
                    if (retryCount < 0){
                        break;
                    }
                    try {
                        Thread.sleep(retryCount * 200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (result.getCode() == TokenResultStatusEnum.Ignorable.getStatus()){
                    return null;
                }

                if (result.getCode() == TokenResultStatusEnum.Normal.getStatus()){
                    QueryTokenBody.ResponseBody responseBody = ((JSONObject)result.getData()).toJavaObject(QueryTokenBody.ResponseBody.class);
                    token = responseBody.getAccessToken();
                    String key = AccessTokenConstants.getKey(appId);
                    jedis.set(key, token);
                    jedis.expire(key, AccessTokenConstants.EXPIRE_DATE);
                }

                return token;
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } finally {
                //释放锁
                lock.release();
            }
        }
        return null;
    }
}
