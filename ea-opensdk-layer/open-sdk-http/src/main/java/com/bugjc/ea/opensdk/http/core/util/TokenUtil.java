package com.bugjc.ea.opensdk.http.core.util;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.opensdk.http.api.AuthPathInfo;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.ResultCode;
import com.bugjc.ea.opensdk.http.model.AppParam;
import com.bugjc.ea.opensdk.http.service.HttpService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aoki
 */
@Slf4j
public class TokenUtil{

    /**
     * 过期时间：秒
     */
    private final static long EXPIRE_DATE = 7200000;

    /**
     * 本地存储 token
     */
    private static TimedCache<String, String> ACCESS_TOKEN_CACHE = CacheUtil.newTimedCache(EXPIRE_DATE);
    private static final String ACCESS_TOKEN_KEY = "AccessTokenKey";

    /**
     * token对象
     */
    @Data
    private static class AccessToken implements Serializable {
        private String token;
    }

    /**
     * 获取平台认证凭据，TODO,需增加远程管理凭证数据
     * @param httpService
     * @param appParam
     * @return
     * @throws IOException
     */
    public static String getToken(HttpService httpService, AppParam appParam) throws IOException {
        String cacheValue = ACCESS_TOKEN_CACHE.get(ACCESS_TOKEN_KEY);
        if (cacheValue == null){
            log.info("未命中缓存");
            AccessToken accessToken = getAccessToken(httpService,appParam);
            int retryCount = 3;
            while (accessToken == null){
                retryCount--;
                accessToken = getAccessToken(httpService, appParam);
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

            cacheValue = accessToken.getToken();
            if (cacheValue != null){
                ACCESS_TOKEN_CACHE.put(ACCESS_TOKEN_KEY,cacheValue, EXPIRE_DATE);
            }

        }
        return cacheValue;
    }

    private static AccessToken getAccessToken(HttpService httpService,AppParam appParam) throws IOException {

        Map<String, Object> params = new HashMap<>();
        params.put("appId", appParam.getAppId());
        params.put("appSecret", appParam.getAppSecret());

        //调用接口
        Result result = httpService.post(AuthPathInfo.QUERY_TOKEN_V1.getPath(), AuthPathInfo.QUERY_TOKEN_V1.getVersion(),null, JSON.toJSONString(params));

        //TODO,根据特定应答码返回正常、重试和忽略状态，获取token在根据状态做相应处理。
        if (result != null && result.getCode() == ResultCode.SUCCESS.getCode()) {
            return ((JSONObject)result.getData()).toJavaObject(AccessToken.class);
        }
        return null;
    }
}
