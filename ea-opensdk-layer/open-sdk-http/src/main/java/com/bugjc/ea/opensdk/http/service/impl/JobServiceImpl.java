package com.bugjc.ea.opensdk.http.service.impl;

import com.bugjc.ea.opensdk.http.api.JobPathInfo;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.model.job.CreateBody;
import com.bugjc.ea.opensdk.http.model.job.DelBody;
import com.bugjc.ea.opensdk.http.model.job.FindBody;
import com.bugjc.ea.opensdk.http.model.job.UpdBody;
import com.bugjc.ea.opensdk.http.service.HttpService;
import com.bugjc.ea.opensdk.http.service.JobService;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * 任务调度服务
 * @author aoki
 * @date 2019/11/4
 * **/
public class JobServiceImpl implements JobService {

    @Setter
    @Getter
    private HttpService httpService;
    
    @Override
    public Result createJob(JobPathInfo jobPathInfo, CreateBody.RequestBody requestBody) throws IOException {
        return httpService.post(jobPathInfo.getPath(), jobPathInfo.getVersion(), requestBody.toString());
    }

    @Override
    public Result delJob(JobPathInfo jobPathInfo, DelBody.RequestBody requestBody) throws IOException {
        return httpService.post(jobPathInfo.getPath(), jobPathInfo.getVersion(), requestBody.toString());
    }

    @Override
    public Result updJob(JobPathInfo jobPathInfo, UpdBody.RequestBody requestBody) throws IOException {
        return httpService.post(jobPathInfo.getPath(), jobPathInfo.getVersion(), requestBody.toString());
    }

    @Override
    public Result findJob(JobPathInfo jobPathInfo, FindBody.RequestBody requestBody) throws IOException {
        return httpService.post(jobPathInfo.getPath(), jobPathInfo.getVersion(), requestBody.toString());
    }
}
