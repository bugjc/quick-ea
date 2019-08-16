package com.bugjc.ea.http.opensdk.core.util;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.http.opensdk.core.dto.Result;
import com.bugjc.ea.http.opensdk.model.AppParam;
import com.bugjc.ea.http.opensdk.service.HttpService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
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
     * 接口路径信息，路径：版本
     */
    public static class ContentPathInfo{


        /**
         * JWT授权接口
         */
        final static String JWT_AUTH_PATH_INFO = "/authenticate/query_token:1.0";

        @Data
        private static class PathInfo implements Serializable {
            private String path;
            private String version;
        }

        /**
         * 解析token路径
         * @return
         */
        static PathInfo resolveTokenPath(){
            String[] pathInfoArr = ContentPathInfo.JWT_AUTH_PATH_INFO.split(":");
            PathInfo pathInfo = new PathInfo();
            pathInfo.setPath(pathInfoArr[0]);
            pathInfo.setVersion(pathInfoArr[1]);
            return pathInfo;
        }
    }

    /**
     * token对象
     */
    @Data
    private static class AccessToken implements Serializable {
        private String token;
    }

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


        ContentPathInfo.PathInfo pathInfo = ContentPathInfo.resolveTokenPath();

        //调用接口
        String url = appParam.getBaseUrl().concat(pathInfo.getPath());
        Result result = httpService.post(url,pathInfo.getVersion(), JSON.toJSONString(params));

        if (result != null) {
            return ((JSONObject)result.getData()).toJavaObject(AccessToken.class);
        }
        return null;
    }
}
