package com.ugiant.job.test.web.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.ugiant.job.core.dto.Result;
import com.ugiant.job.test.api.ApiClient;
import com.ugiant.job.test.env.EnvUtil;
import com.ugiant.job.web.io.job.CreateBody;
import com.ugiant.job.web.io.job.FindBody;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Slf4j
public class CreateJobTask implements Runnable {

    private ApiClient apiClient;
    private CyclicBarrier cyclicBarrier;

    public CreateJobTask(ApiClient apiClient, CyclicBarrier cyclicBarrier){
        this.apiClient = apiClient;
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        //等待所有任务
        try {
            cyclicBarrier.await();

            CreateBody.RequestBody requestBody = new CreateBody.RequestBody();
            String jobId = "ASDF"+ RandomUtil.randomNumbers(28);
            requestBody.setJobId(jobId);
            requestBody.setExecTime(DateUtil.offsetSecond(new Date(),RandomUtil.randomInt(2,30)).toString());
            //回调参数对象
            Map<String,Object> httpCallBackInfo = new HashMap<>();
            httpCallBackInfo.put("URL", EnvUtil.getBaseUrl(EnvUtil.EnvType.DEV).concat(ApiClient.ContentPath.JOB_FIND_PATH));
            FindBody.RequestBody findRequestBody = new FindBody.RequestBody();
            findRequestBody.setJobId(jobId);
            httpCallBackInfo.put("requestBody",findRequestBody);
            requestBody.setHttpCallbackInfo(JSON.toJSONString(httpCallBackInfo));

            String content = JSON.toJSONString(requestBody);
            log.info("请求参数：{}",content);
            Result result = apiClient.doPost(ApiClient.ContentPath.JOB_CREATE_PATH,content);
            log.info("应答数据：{}",result != null ? result.toString() : null);
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

}
