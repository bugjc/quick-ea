package com.bugjc.ea.opensdk.http.core.component.token.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.FIFOCache;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.opensdk.http.api.AuthPathInfo;
import com.bugjc.ea.opensdk.http.core.component.token.AuthConfig;
import com.bugjc.ea.opensdk.http.core.component.token.constants.AccessTokenConstants;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.TokenResultCode;
import com.bugjc.ea.opensdk.http.core.exception.HttpSecurityException;
import com.bugjc.ea.opensdk.http.model.auth.QueryTokenBody;
import com.bugjc.ea.opensdk.http.service.AuthService;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 平台认证服务 默认应用内内存 实现
 * @author aoki
 * @date 2019/11/5
 * **/
@Slf4j
public class AuthDefaultConfigImpl implements AuthConfig {

    /**
     *  平台接口授权服务 http客户端
     */
    @Inject
    private AuthService authService;

    /**
     * 缓存容量
     */
    private static final int CACHE_CAPACITY = 10;

    /**
     * 私有化构造函数
     */
    private AuthDefaultConfigImpl(){}

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
        private AuthDefaultConfigImpl authConfig;
        private FIFOCache<String, String> ramStorage;
        private Lock accessTokenLock;

        /**
         * 私有化枚举的构造函数
         */
        SingletonEnum(){
            authConfig = new AuthDefaultConfigImpl();
            ramStorage = CacheUtil.newFIFOCache(CACHE_CAPACITY);
            accessTokenLock = new ReentrantLock();
        }

        public AuthConfig getInstance(){
            return authConfig;
        }

        public FIFOCache<String, String> getCacheInstance(){
            return ramStorage;
        }

        public Lock getLockInstance(){
            return accessTokenLock;
        }
    }

    /**
     * 暴露获取实例的静态方法
     * @return
     */
    public static AuthConfig getInstance(){
        return SingletonEnum.INSTANCE.getInstance();
    }

    @Override
    public String getToken() throws HttpSecurityException {
        if (authService == null){
            return null;
        }

        Lock lock = SingletonEnum.LOCK_INSTANCE.getLockInstance();
        FIFOCache<String, String> cache = SingletonEnum.CACHE_INSTANCE.getCacheInstance();
        String appId = authService.getHttpService().getAppParam().getAppId();

        //从 ram 中获取 token
        String token = cache.get(AccessTokenConstants.getKey(appId));
        if (token != null){
            return token;
        }

        try {
            //加独占锁，并在一次获取 token
            lock.lock();
            token = cache.get(AccessTokenConstants.getKey(appId));
            if (token != null){
                return token;
            }

            //获取 token 重试次数
            int retryCount = 3;
            Result result =  authService.getToken(AuthPathInfo.QUERY_TOKEN_V1);
            while (result.getCode() == TokenResultCode.Retry.getCode()){
                retryCount--;
                log.info(retryCount +"");
                result = authService.getToken(AuthPathInfo.QUERY_TOKEN_V1);
                if (retryCount <= 0){
                    break;
                }
                try {
                    Thread.sleep(retryCount * 200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (result.getCode() == TokenResultCode.Ignorable.getCode()){
                return null;
            }

            if (result.getCode() == TokenResultCode.Normal.getCode()){
                QueryTokenBody.ResponseBody responseBody = ((JSONObject)result.getData()).toJavaObject(QueryTokenBody.ResponseBody.class);
                token = responseBody.getAccessToken();
                String key = AccessTokenConstants.getKey(appId);
                cache.put(key, token, AccessTokenConstants.EXPIRE_DATE);
            }

            return token;
        } finally {
            lock.unlock();
        }

    }
}
