package com.bugjc.ea.server.job.test.web;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.server.job.core.enums.business.JobStatus;
import com.bugjc.ea.server.job.test.api.ApiClient;
import com.bugjc.ea.server.job.test.core.component.cyclic.barrier.CyclicBarrierComponent;
import com.bugjc.ea.server.job.test.env.EnvUtil;
import com.bugjc.ea.server.job.test.web.task.CreateJobCyclicBarrierTaskImpl;
import com.bugjc.ea.server.job.web.io.job.DelBody;
import com.bugjc.ea.server.job.web.io.job.FindBody;
import com.bugjc.ea.server.job.web.io.job.ListBody;
import com.bugjc.ea.server.job.web.io.job.UpdBody;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JOB业务
 * @author aoki
 * @date ${date}
 */
@Slf4j
public class JobWebTest {

    private ApiClient apiClient = new ApiClient(EnvUtil.EnvType.DEV);


    /**
     * 任务创建测试
     */
    @Test
    public void testJobCreate() throws InterruptedException {
        //同时发起 10 个创建任务请求
        int total = 5000;
        //任务等待超时时间
        int timeout = 1;
        CreateJobCyclicBarrierTaskImpl createJobCyclicBarrierTask = new CreateJobCyclicBarrierTaskImpl(apiClient);
        CyclicBarrierComponent cyclicBarrierComponent = new CyclicBarrierComponent(total, timeout, createJobCyclicBarrierTask);
        cyclicBarrierComponent.run();
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
