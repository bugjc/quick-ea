package com.bugjc.ea.opensdk.http.service;

import com.bugjc.ea.opensdk.http.api.JobPathInfo;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.model.job.CreateBody;
import com.bugjc.ea.opensdk.http.model.job.DelBody;
import com.bugjc.ea.opensdk.http.model.job.FindBody;
import com.bugjc.ea.opensdk.http.model.job.UpdBody;

import java.io.IOException;

/**
 * 任务调度服务接口
 * @author aoki
 * @date 2019/11/4
 * **/
public interface JobService {

    /**
     * 创建任务
     * @param jobPathInfo       --接口地址信息对象
     * @param requestBody       --接口参数
     * @return Result.class     --应答对象
     * @throws IOException      --抛出的异常
     */
    Result createJob(JobPathInfo jobPathInfo, CreateBody.RequestBody requestBody) throws IOException;

    /**
     * 删除任务
     * @param jobPathInfo       --接口地址信息对象
     * @param requestBody       --接口参数
     * @return Result.class     --应答对象
     * @throws IOException      --抛出的异常
     */
    Result delJob(JobPathInfo jobPathInfo, DelBody.RequestBody requestBody) throws IOException;

    /**
     * 修改任务
     * @param jobPathInfo       --接口地址信息对象
     * @param requestBody       --接口参数
     * @return Result.class     --应答对象
     * @throws IOException      --抛出的异常
     */
    Result updJob(JobPathInfo jobPathInfo, UpdBody.RequestBody requestBody) throws IOException;

    /**
     * 获取任务
     * @param jobPathInfo       --接口地址信息对象
     * @param requestBody       --接口参数
     * @return Result.class     --应答对象
     * @throws IOException      --抛出的异常
     */
    Result findJob(JobPathInfo jobPathInfo, FindBody.RequestBody requestBody) throws IOException;
}
