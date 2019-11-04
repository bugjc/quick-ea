package com.bugjc.ea.opensdk.http.core.component;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.opensdk.http.api.AuthPathInfo;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.ResultCode;
import com.bugjc.ea.opensdk.http.service.impl.HttpServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口令牌组件
 * @author aoki
 * @date 2019/11/4
 * **/
@Slf4j
public class AccessTokenComponent {
    /**
     * 本地内存存储 token
     */
    private static TimedCache<String, String> ramStorage;
    /**
     * 远程 redis存储 token
     */
    private JedisPool redisStorage;

    /**
     * http 客户端
     */
    private HttpServiceImpl httpService;

    private AccessTokenComponent(){

    }

    public AccessTokenComponent(HttpServiceImpl httpService,JedisPool jedisPool){
        this.httpService = httpService;
        ramStorage = CacheUtil.newTimedCache(EXPIRE_DATE);
        this.redisStorage = jedisPool;
    }

    /**
     * 过期时间：7200000 秒
     */
    private final static int EXPIRE_DATE = 7200000;


    private static final String ACCESS_TOKEN_KEY = "AccessToken:";
    private static final String HASH_VALUE_FIELD = "value";
    private static final String HASH_EXPIRE_FIELD = "expire";

    private String getRedisKey(String key) {
        return ACCESS_TOKEN_KEY + key;
    }

    /**
     * 获取 token
     * @param key
     * @return
     */
    private String getValueFromRedis(String key){
        if (redisStorage != null){
            try (Jedis jedis = redisStorage.getResource()) {
                //从redis 中获取 token
                return jedis.get(getRedisKey(key));
            }
        }

        //从内存中获取 token
        return ramStorage.get(key);
    }

    /**
     * 存储 token
     * @param key
     * @param expiresTime
     * @param value
     */
    private void setValueToRedis(String key, String value, int expiresTime){
        if (redisStorage != null){
            try (Jedis jedis = redisStorage.getResource()) {
                jedis.set(getRedisKey(key), value);
                jedis.expire(getRedisKey(key), expiresTime);
            }
        }

        //无论有没有指定 redis 存储，都会在本地内存存一份。
        ramStorage.put(key, value, expiresTime);
    }


    /**
     * token对象
     */
    @Data
    private static class AccessToken implements Serializable {
        private String accessToken;
    }

    /**
     * 获取平台认证凭据
     * @return
     * @throws IOException
     */
    public synchronized String getToken() throws IOException {
        String cacheValue = getValueFromRedis(httpService.getAppParam().getAppId());
        if (cacheValue == null){
            log.info("未命中缓存");
            AccessToken accessToken = getAccessToken();
            //获取 token 重试次数
            int retryCount = 3;
            while (accessToken == null){
                retryCount--;
                accessToken = getAccessToken();
                if (retryCount < 0){
                    break;
                }
                try {
                    Thread.sleep(retryCount * 200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (accessToken == null){
                return null;
            }

            cacheValue = accessToken.getAccessToken();
            if (cacheValue != null){
                setValueToRedis(httpService.getAppParam().getAppId(), cacheValue, EXPIRE_DATE);
            }

        }
        return cacheValue;
    }

    private AccessToken getAccessToken() throws IOException {

        //构建请求对象
        Map<String, Object> params = new HashMap<>();
        params.put("appId", httpService.getAppParam().getAppId());
        params.put("appSecret", httpService.getAppParam().getAppSecret());

        //调用接口
        Result result = httpService.post(AuthPathInfo.QUERY_TOKEN_V1.getPath(), AuthPathInfo.QUERY_TOKEN_V1.getVersion(),null, JSON.toJSONString(params));

        //TODO,根据特定应答码返回正常、重试和忽略状态，获取token在根据状态做相应处理。
        if (result != null && result.getCode() == ResultCode.SUCCESS.getCode()) {
            return ((JSONObject)result.getData()).toJavaObject(AccessToken.class);
        }
        return null;
    }

}
