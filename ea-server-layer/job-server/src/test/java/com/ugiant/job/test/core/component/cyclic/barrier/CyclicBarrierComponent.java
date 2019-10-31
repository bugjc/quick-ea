package com.ugiant.job.test.core.component.cyclic.barrier;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpStatus;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 循环栅栏组件，主要用于测试接口服务能力，目的是快速构建接口的性能测试用例。
 * @author aoki
 */
@Slf4j
public class CyclicBarrierComponent {

    //循环等待次数
    private int cycleWaitTimes;
    @Getter
    //失败应答结果数量
    private AtomicInteger resultErrorNum = new AtomicInteger(0);
    @Getter
    //成功应答结果数量
    private AtomicInteger resultSuccessNum = new AtomicInteger(0);

    //总任务数
    private long total;
    //任务等待超时时间，单位:秒
    private long timeout;
    //可循环的屏障对象
    private CyclicBarrier cyclicBarrier;
    //可循环屏障具体业务逻辑对象
    private CyclicBarrierTask cyclicBarrierTask;
    //线程池对象
    private ThreadPoolExecutor threadPoolExecutor;

    public CyclicBarrierComponent(int total, long timeout, CyclicBarrierTask cyclicBarrierTask){
        this.total = total;
        this.timeout = timeout;
        this.cyclicBarrierTask = cyclicBarrierTask;
        this.cyclicBarrier = new CyclicBarrier(total, new DefaultBarrierActionTask(this, System.currentTimeMillis(), false, total));
        threadPoolExecutor = new ThreadPoolExecutor(total, total,
                60, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(2048), new ThreadFactoryBuilder().setNamePrefix("job-pool-").build(), new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 循环栅栏组件运行方法
     */
    public void run() {
        //创建与线程同等的任务数
        for (int i = 0; i < total; i++) {
            //创建任务
            threadPoolExecutor.execute(() -> {
                try {

                    //等待所有任务
                    if (timeout == 0){
                        cyclicBarrier.await();
                    }else {
                        //设置任务等待超时时间
                        cyclicBarrier.await(timeout, TimeUnit.SECONDS);
                    }

                    //执行任务
                    int code = cyclicBarrierTask.execTask();
                    if (code == HttpStatus.HTTP_OK){
                        //成功应答数量加 1
                        resultSuccessNum.addAndGet(1);
                    } else {
                        //失败应答数量加 1
                        resultErrorNum.addAndGet(1);
                    }

                    //任务完成触发屏障动作
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                    e.printStackTrace();
                    log.error("屏障运行错误：{}",e.getMessage());
                }
            });
        }

        //启动有序关闭，在该关闭中执行先前提交的任务，但不接受任何新任务。如果调用已经关闭，则调用不会产生任何其他影响。
        threadPoolExecutor.shutdown();

        do {
            cycleWaitTimes++;
        } while (!threadPoolExecutor.isTerminated());
    }


    /**
     * 答应报表
     */
    @Slf4j
    private static class DefaultBarrierActionTask implements Runnable {
        //记录请求开始时间
        private long startTime;
        //标记，false 表示准备测试资源所消耗的时间，true 表示所有任务执行完毕所消耗的总时间（RT）
        private boolean flag;
        //请求总数
        private int numberOfRequests;
        //可循环栅栏组件对象
        private CyclicBarrierComponent cyclicBarrierComponent;


        DefaultBarrierActionTask(CyclicBarrierComponent cyclicBarrierComponent, long startTime, boolean flag, int numberOfRequests){
            this.cyclicBarrierComponent = cyclicBarrierComponent;
            this.startTime = startTime;
            this.flag = flag;
            this.numberOfRequests = numberOfRequests;
        }

        @Override
        public void run() {
            if (flag){
                //到达第二个屏障执行
                long totalTime = (System.currentTimeMillis() - startTime);
                double averageResponseTime = NumberUtil.div(totalTime, numberOfRequests);
                double noSecondsToProcessRequests = NumberUtil.div(1000, averageResponseTime);

                double averageSuccessResponseTime = NumberUtil.div(totalTime, cyclicBarrierComponent.getResultSuccessNum().get());
                double successPerSecond = NumberUtil.div(1000, averageSuccessResponseTime);

                log.info("业务执行成功总数：{}", cyclicBarrierComponent.getResultSuccessNum().get());
                log.info("业务执行失败总数：{}", cyclicBarrierComponent.getResultErrorNum().get());
                log.info("平均响应时间：{} 毫秒", averageResponseTime);
                log.info("每秒处理请求：{} 条", NumberUtil.round(noSecondsToProcessRequests,0));
                log.info("每秒业务处理成功：{} 条", NumberUtil.round(successPerSecond,0));
                log.info("总耗时：{} 毫秒", totalTime);
            }else {
                //到达第一个屏障执行
                log.info("准备资源总耗时：{} 毫秒。", (System.currentTimeMillis() - startTime));
                flag = true;
            }

        }
    }
}
