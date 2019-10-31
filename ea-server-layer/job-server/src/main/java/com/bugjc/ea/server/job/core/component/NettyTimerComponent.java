package com.bugjc.ea.server.job.core.component;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import com.bugjc.ea.server.job.core.util.SpringContextUtil;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Hash Wheel Timer 使用
  * @author  aoki
  * @date 2017/8/5 10:01
 */
@Slf4j
@Lazy
@Component
public class NettyTimerComponent {

    /**
     * 创建本地任务缓存
     */
    private static Cache<String, Timeout> localTaskCache = null;

    /**
     * 创建本地环形任务队列
     */
    private static Timer timer = null;

    private static final int CAPACITY = 2000;
    public NettyTimerComponent(){
        ThreadPoolExecutor threadPoolExecutor = SpringContextUtil.getBean(ThreadPoolExecutor.class);
        timer = new HashedWheelTimer(threadPoolExecutor.getThreadFactory(),1, TimeUnit.SECONDS, CAPACITY);
        localTaskCache = CacheUtil.newFIFOCache(CAPACITY);
    }




    /**
     * 添加任务
     * @param key
     * @param task
     * @param expTime
     * @param timeUnit
     */
    public void addTask(String key, TimerTask task, long expTime, TimeUnit timeUnit){
        Timeout timeout = timer.newTimeout(task, expTime, timeUnit);
        if (key == null){
            log.error("任务唯一编号不能为空");
            return;
        }

        if (localTaskCache.get(key) != null){
            return;
        }
        localTaskCache.put(key,timeout,timeUnit.convert(expTime, TimeUnit.MILLISECONDS));
    }

    /**
     * 取消任务
     * @param key
     * @return
     */
    public void removeTask(String key){
        if (localTaskCache.get(key) == null){
            log.info("要删除的任务不存在");
            return;
        }
        boolean flag = localTaskCache.get(key).cancel();
        if (flag){
            log.info("删除延迟任务成功。订单号：{}",key);
        }else {
            log.info("任务已执行或已过期");
        }
    }
    
}
