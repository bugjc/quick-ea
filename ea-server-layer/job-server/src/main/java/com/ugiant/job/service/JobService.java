package com.ugiant.job.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ugiant.job.web.io.job.*;

/**
 * 任务服务
 *
 * @author aoki
 * @date 2019/9/23 16:04
 **/
public interface JobService {

    /**
     * 创建任务
     *
     * @param requestBody
     * @return
     */
    CreateBody.ResponseBody createJob(CreateBody.RequestBody requestBody);

    /**
     * 删除任务
     *
     * @param requestBody
     * @return
     */
    DelBody.ResponseBody delJob(DelBody.RequestBody requestBody);

    /**
     * 修改任务
     *
     * @param requestBody
     * @return
     */
    UpdBody.ResponseBody updJob(UpdBody.RequestBody requestBody);

    /**
     * 获取任务
     *
     * @param requestBody
     * @return
     */
    FindBody.ResponseBody findJob(FindBody.RequestBody requestBody);

    /**
     * 获取任务分页记录
     *
     * @param requestBody
     * @return
     */
    ListBody.ResponseBody findJobPage(ListBody.RequestBody requestBody);



    /**
     * 执行任务
     *
     * @throws Exception
     */
    void execJob() throws Exception;
}
