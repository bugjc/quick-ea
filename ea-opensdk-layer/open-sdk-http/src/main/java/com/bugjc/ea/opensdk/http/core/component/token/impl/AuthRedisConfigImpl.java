package com.bugjc.ea.opensdk.http.core.component.token.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.opensdk.http.api.AuthPathInfo;
import com.bugjc.ea.opensdk.http.core.component.token.AuthConfig;
import com.bugjc.ea.opensdk.http.core.component.token.constants.AccessTokenConstants;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.TokenResultCode;
import com.bugjc.ea.opensdk.http.core.exception.HttpSecurityException;
import com.bugjc.ea.opensdk.http.model.auth.QueryTokenBody;
import com.bugjc.ea.opensdk.http.service.AuthService;
import com.github.jedis.lock.JedisLock;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * 平台认证服务 redis 实现
 * @author aoki
 * @date 2019/11/5
 * **/
@Slf4j
public class AuthRedisConfigImpl implements AuthConfig {

    /**
     *  平台接口授权服务 http客户端
     */
    @Inject
    private AuthService authService;

    /**
     * 私有化构造函数
     */
    private AuthRedisConfigImpl(){}

    /**
     * 定义一个静态枚举类
     */
    enum SingletonEnum {
        //创建一个枚举对象，该对象天生为单例
        INSTANCE;
        private AuthConfig authConfig;

        /**
         * 私有化枚举的构造函数
         */
        SingletonEnum(){
            authConfig = new AuthRedisConfigImpl();
        }

        public AuthConfig getInstance(){
            return authConfig;
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
    public String getToken() {
        if (authService == null){
            return null;
        }

        if (authService.getHttpService().getAppParam().getJedisPool() == null){
            return null;
        }

        String appId = authService.getHttpService().getAppParam().getAppId();

        try (Jedis jedis = authService.getHttpService().getAppParam().getJedisPool().getResource()) {
            //从 redis 中获取 token
            String token = getAccessToken(jedis, appId);
            if (StrUtil.isNotBlank(token)){
                return token;
            }

            //创建分布式锁对象
            JedisLock lock = new JedisLock(jedis, appId, 3000, 12000);

            try {
                //获取锁
                lock.acquire();
                //再一次尝试获取token
                token = getAccessToken(jedis, appId);;
                if (StrUtil.isNotBlank(token)){
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
                    jedis.set(key, token);
                    jedis.expire(key, AccessTokenConstants.EXPIRE_DATE);
                }

                return token;
            } catch (InterruptedException | HttpSecurityException e) {
                e.printStackTrace();
            } finally {
                //释放锁
                lock.release();
            }
        }
        return null;
    }

    /**
     * 从 redis 中获取token
     * @param jedis
     * @param appId
     * @return
     */
    private String getAccessToken(Jedis jedis, String appId){
        return jedis.get(AccessTokenConstants.getKey(appId));
    }
}
