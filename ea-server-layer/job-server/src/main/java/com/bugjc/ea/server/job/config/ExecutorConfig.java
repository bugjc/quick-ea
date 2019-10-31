package com.bugjc.ea.server.job.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * 线程池配置
 * @author aoki
 */
@Slf4j
@Configuration
@EnableAsync
public class ExecutorConfig {

    @Bean("threadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor() {
        log.info("初始化线程池");
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNamePrefix("job-pool").build();
        //允许线程的空闲时间，
        //例如：线程池中最大的线程数为50，而其中只有40个线程任务在跑，相当于有10个空闲线程，这10个空闲线程不能让他一直在开着，
        //因为线程的存在也会特别好资源的，所有就需要设置一个这个空闲线程的存活时间。
        int keepAlive = 60;
        //线程池维护线程的最大数量
        int maxPoolSize = 200;
        //线程池维护线程的最少数量
        int corePoolSize = 10;
        //缓存队列
        int queueCapacity = 1024;
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAlive, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueCapacity), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

}
