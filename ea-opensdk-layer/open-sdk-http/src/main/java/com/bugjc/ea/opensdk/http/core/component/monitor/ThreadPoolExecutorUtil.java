package com.bugjc.ea.opensdk.http.core.component.monitor;

import cn.hutool.core.thread.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类
 * @author aoki
 * @date 2019/11/13
 * **/
public class ThreadPoolExecutorUtil {

    /**
     * 初始化线程池
     */
    private final static ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(2, 3,
            20, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(2048), new ThreadFactoryBuilder().setNamePrefix("monitor-pool-").build(), new ThreadPoolExecutor.AbortPolicy());

    /**
     * 从线程池中获取一个可用线程执行指令
     * @param command
     */
    public static void execute(Runnable command){
        THREAD_POOL_EXECUTOR.execute(command);
    }
}
