package com.bugjc.ea.gateway.core.util;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

/**
 * @author aoki
 */
public class SignatureCacheUtil {

    /**
     * 默认5毫秒检查一次过期
     */
    private static TimedCache<String, String> timedCache = CacheUtil.newTimedCache(1000);

    public static void setTimedCache(String key,String value){
        timedCache.put(key,value,30000);
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public static String getCache(String key){
        return timedCache.get(key);
    }

}
