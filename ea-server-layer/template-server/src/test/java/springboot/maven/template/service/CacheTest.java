package springboot.maven.template.service;

import cn.hutool.core.util.RandomUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import springboot.maven.template.Tester;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 缓存组件测试
 */
@Slf4j
public class CacheTest extends Tester {

    @CreateCache(cacheType = CacheType.REMOTE)
    @CacheRefresh(refresh = 10,stopRefreshAfterLastAccess = 20)
    @CachePenetrationProtect
    private Cache<String, String> processControlCache;

    @Test
    public void testSelOrderByQrNoAndUserId() throws InterruptedException {
        int count = 5;
        for (;;) {
            if (count > 0){
                log.info(getValue());
                count--;
            }
            Thread.sleep(5000);
        }
    }

    public String getValue(){
        String key = "test1-1";
        String value = processControlCache.get(key);
        if (value == null){
            value = RandomUtil.randomNumber() + "";
            processControlCache.put(key,value);
        }
        return value;
    }
}
