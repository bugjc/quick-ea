package springboot.maven.template.core.component;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import springboot.maven.template.core.util.SpringContextUtil;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 延迟执行任务（业务逻辑）组件
 * @author aoki
 * @date 2019/10/30
 * **/
@Slf4j
@Lazy
@Component
public class NettyTimerComponent {


    @Data
    @Configuration
    private static class Config{
        @Value("${netty-timer.component.capacity}")
        private int capacity = 2000;
    }

    /**
     * 创建本地任务缓存
     */
    private static Cache<String, Timeout> localTaskCache = null;

    /**
     * 创建本地环形任务队列
     */
    private static Timer timer = null;

    /**
     * 初始化
     */
    public NettyTimerComponent(){
        Config config = SpringContextUtil.getBean(Config.class);
        ThreadPoolExecutor threadPoolExecutor = SpringContextUtil.getBean(ThreadPoolExecutor.class);
        timer = new HashedWheelTimer(threadPoolExecutor.getThreadFactory(),1, TimeUnit.SECONDS, config.getCapacity());
        localTaskCache = CacheUtil.newFIFOCache(config.getCapacity());
    }

    /**
     * 添加任务
     * @param key
     * @param task
     * @param expTime
     * @param timeUnit
     */
    public void addTask(String key, TimerTask task, int expTime, TimeUnit timeUnit){

        if (key == null){
            log.error("任务唯一编号不能为空");
            return;
        }

        if (localTaskCache.containsKey(key)){
            //存在相同的任务则不允许创建
            return;
        }

        //添加一个延迟任务
        localTaskCache.put(key,timer.newTimeout(task, expTime, timeUnit),timeUnit.convert(expTime, TimeUnit.MILLISECONDS));
    }

    /**
     * 取消任务
     * @param key
     * @return
     */
    public void removeTask(String key){
        if (!localTaskCache.containsKey(key)){
            log.info("要删除的任务不存在");
            return;
        }

        boolean flag = localTaskCache.get(key).cancel();
        if (flag){
            log.info("删除延迟任务成功。KEY：{}",key);
        }else {
            log.info("任务已执行或已过期");
        }
    }
    
}
