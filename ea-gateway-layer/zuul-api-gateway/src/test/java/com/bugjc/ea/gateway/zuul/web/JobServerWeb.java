package com.bugjc.ea.gateway.zuul.web;

import com.bugjc.ea.gateway.zuul.web.task.JobFindCyclicBarrierTask;
import com.bugjc.ea.opensdk.test.TestBuilder;
import com.bugjc.ea.opensdk.test.component.CyclicBarrierComponent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author aoki
 */
@Slf4j
public class JobServerWeb {


//    try {
//
//        File file = FileUtil.touch("config1.properties");
//        FileWriter writer = new FileWriter(file);
//        writer.append("eureka.serviceUrl.default=http://eureka:123456@127.0.0.1:8000/eureka/,http://eureka:123456@127.0.0.1:8001/eureka/");
//        writer.close();
//
//        ClassPathResource resource = new ClassPathResource("config1.properties");
//        System.out.println(resource.getPath());
//        System.out.println(resource.getAbsolutePath());
//        System.out.println(resource.getFile().getPath());
//        System.out.println(resource.getName());
//        System.out.println(resource.getFile().getParentFile().getPath());
//        //FileUtil.appendLines(appList,);
//    }catch (Exception ex){
//        System.out.println(FileUtil.touch("config1.properties").getPath());
//    }

    /**
     * 获取任务信息
     */
    @Test
    public void testJob() throws Exception {
        //同时发起 500 个创建任务请求
        int total = 100;
        JobFindCyclicBarrierTask jobFindCyclicBarrierTask = new JobFindCyclicBarrierTask();
        //手动触发一次
        jobFindCyclicBarrierTask.execTask();

        CyclicBarrierComponent cyclicBarrierComponent = new TestBuilder()
                .setTotal(total)
                .setCyclicBarrierTask(jobFindCyclicBarrierTask)
                .build();
        cyclicBarrierComponent.run();


    }

}
