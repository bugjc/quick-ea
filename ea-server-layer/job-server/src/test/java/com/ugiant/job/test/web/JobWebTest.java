package com.ugiant.job.test.web;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.ugiant.job.core.dto.Result;
import com.ugiant.job.core.enums.business.JobStatus;
import com.ugiant.job.core.enums.business.JobType;
import com.ugiant.job.test.api.ApiClient;
import com.ugiant.job.test.env.EnvUtil;
import com.ugiant.job.test.web.task.CreateJobTask;
import com.ugiant.job.web.io.job.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * JOB业务
 * @author aoki
 * @date ${date}
 */
@Slf4j
public class JobWebTest {

    private ApiClient apiClient =  new ApiClient(EnvUtil.EnvType.DEV);

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 200,
            60, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), new ThreadFactoryBuilder().setNamePrefix("job-pool").build(), new ThreadPoolExecutor.AbortPolicy());

    /**
     * 任务创建测试
     */
    @Test
    public void testJobCreate() throws InterruptedException {
        int count = 20;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(count);
        for (int i = 0; i < count; i++) {
            threadPoolExecutor.execute(new CreateJobTask(apiClient,cyclicBarrier));
        }
        threadPoolExecutor.shutdown();

        while (!threadPoolExecutor.isTerminated()){
            Thread.sleep(10);
            log.info("任务暂停10毫秒");
        }
    }

    /**
     * 删除任务
     */
    @Test
    public void testJobDel(){
        DelBody.RequestBody requestBody = new DelBody.RequestBody();
        requestBody.setJobId("--------------");
        String content = JSON.toJSONString(requestBody);
        log.info("请求参数：{}",content);
        Result result = apiClient.doPost(ApiClient.ContentPath.JOB_DEL_PATH,content);
        log.info("应答数据：{}",result != null ? result.toString() : null);
    }

    /**
     * 修改任务
     */
    @Test
    public void testJobUpd(){
        UpdBody.RequestBody requestBody = new UpdBody.RequestBody();
        String jobId = "ASDF"+ RandomUtil.randomNumbers(28);
        requestBody.setJobId(jobId);
        requestBody.setExecTime(DateUtil.offsetSecond(new Date(),RandomUtil.randomInt(2,30)).toString());
        //回调参数对象
        Map<String,Object> httpCallBackInfo = new HashMap<>();
        httpCallBackInfo.put("url","http://127.0.0.1:8081/".concat(ApiClient.ContentPath.JOB_FIND_PATH));
        FindBody.RequestBody findRequestBody = new FindBody.RequestBody();
        findRequestBody.setJobId(jobId);
        httpCallBackInfo.put("requestBody",findRequestBody);
        requestBody.setHttpCallbackInfo(JSON.toJSONString(httpCallBackInfo));
        String content = JSON.toJSONString(requestBody);
        log.info("请求参数：{}",content);
        Result result = apiClient.doPost(ApiClient.ContentPath.JOB_UPD_PATH,content);
        log.info("应答数据：{}",result != null ? result.toString() : null);
    }

    /**
     * 获取任务信息
     */
    @Test
    public void testJobFind(){
        FindBody.RequestBody requestBody = new FindBody.RequestBody();
        requestBody.setJobId("ASDF3029738213005968458222281279");
        String content = JSON.toJSONString(requestBody);
        log.info("请求参数：{}",content);
        Result result = apiClient.doPost(ApiClient.ContentPath.JOB_FIND_PATH,content);
        log.info("应答数据：{}",result != null ? result.toString() : null);
    }

    /**
     * 获取任务分页列表
     */
    @Test
    public void testGetJobList() {
        ListBody.RequestBody requestBody = new ListBody.RequestBody();
        requestBody.setStatus(String.valueOf(JobStatus.AwaitExec.getStatus()));
        String content = JSON.toJSONString(requestBody);
        log.info("请求参数：{}",content);
        Result result = apiClient.doPost(ApiClient.ContentPath.JOB_GET_LIST,content);
        log.info("应答数据：{}",result != null ? result.toString() : null);
    }

    /**
     * 获取任务状态列表
     */
    @Test
    public void testGetJobStatusList() {
        Result result = apiClient.doPost(ApiClient.ContentPath.JOB_GET_STATUS_LIST,null);
        log.info("应答数据：{}",result != null ? result.toString() : null);
    }
}
