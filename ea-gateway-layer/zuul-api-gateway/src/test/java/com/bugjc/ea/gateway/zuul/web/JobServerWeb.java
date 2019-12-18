package com.bugjc.ea.gateway.zuul.web;

import com.bugjc.ea.gateway.zuul.web.task.JobFindCyclicBarrierTask;
import com.bugjc.ea.opensdk.http.core.component.monitor.data.CountInfoTable;
import com.bugjc.ea.opensdk.test.TestBuilder;
import com.bugjc.ea.opensdk.test.component.CyclicBarrierComponent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author aoki
 */
@Slf4j
public class JobServerWeb {

    /**
     * 获取任务信息
     */
    @Test
    public void testJob() throws Exception {
        //1s 打印一次
        //MetricUtil.consolePrint(1, TimeUnit.SECONDS);

        //同时发起 500 个创建任务请求
        int total = 1000;
        JobFindCyclicBarrierTask jobFindCyclicBarrierTask = new JobFindCyclicBarrierTask();
        //手动触发一次
        jobFindCyclicBarrierTask.execTask();

        CyclicBarrierComponent cyclicBarrierComponent = new TestBuilder()
                .setTotal(total)
                .setCyclicBarrierTask(jobFindCyclicBarrierTask)
                .build();
        cyclicBarrierComponent.run();

        log.info("实时统计信息：{}", CountInfoTable.getInstance().getCountInfo());
        Thread.sleep(5000);
    }

}
