package com.bugjc.ea.auth.core.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheGetResult;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SequenceLimitUtil {


    @CreateCache(name = "sequence:limit:",expire = 2, cacheType = CacheType.REMOTE, timeUnit = TimeUnit.DAYS)
    @CachePenetrationProtect
    private Cache<String, String> cache;

    /**
     * 请求限制
     * @param sequence
     * @return
     */
    public boolean limit(String sequence){

        String date = sequence.substring(4,12);
        if (DateUtil.offsetDay(new Date(),-2).compareTo(DateUtil.parse(date, DatePattern.PURE_DATE_PATTERN)) >= 0){
            log.info("请求时间大于2天前则不受理请求");
            return true;
        }

        String key = "sequence:"+sequence;
        CacheGetResult<String> cacheGetResult = cache.GET(key);
        if (!cacheGetResult.isSuccess()){
            cache.put(key,key);
            return false;
        }
        return true;
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public String get(String key){
        return cache.get(key);
    }

    public static void main(String[] args) {
        String sequence = "201902130355488982727912";
        log.info(sequence);
        String date = sequence.substring(0,8);
        log.info(date);
        System.out.println(DateUtil.offsetDay(new Date(),-2));
        if (DateUtil.offsetDay(new Date(),-2).compareTo(DateUtil.parse(date, DatePattern.PURE_DATE_PATTERN)) >= 0){
            log.info("请求时间大于3天前则不受理请求");
        }
    }

}
