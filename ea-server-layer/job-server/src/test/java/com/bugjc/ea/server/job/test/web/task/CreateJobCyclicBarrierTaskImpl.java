package com.bugjc.ea.server.job.test.web.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.test.service.CyclicBarrierTask;
import com.bugjc.ea.server.job.test.api.ApiClient;
import com.bugjc.ea.server.job.test.env.EnvUtil;
import com.bugjc.ea.server.job.web.io.job.CreateBody;
import com.bugjc.ea.server.job.web.io.job.FindBody;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认测试的业务逻辑
 */
@Slf4j
public class CreateJobCyclicBarrierTaskImpl implements CyclicBarrierTask {

    private ApiClient apiClient;
    public CreateJobCyclicBarrierTaskImpl(ApiClient apiClient){
        this.apiClient =  apiClient;
    }

    @Override
    public int execTask() {
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
        log.info("工作线程：{}，接口请求参数：{}", Thread.currentThread().getName(), content);
        Result result = apiClient.doPost(ApiClient.ContentPath.JOB_CREATE_PATH,content);
        log.info("工作线程：{}，接口应答数据：{}", Thread.currentThread().getName(), result.toString());
        return result.getCode();
    }
}
