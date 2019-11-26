package com.bugjc.ea.gateway.zuul.web.task;

import cn.hutool.core.util.RandomUtil;
import com.bugjc.ea.gateway.zuul.env.EnvUtil;
import com.bugjc.ea.gateway.zuul.util.HttpUtil;
import com.bugjc.ea.opensdk.http.api.JobPathInfo;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.ResultCode;
import com.bugjc.ea.opensdk.http.model.job.FindBody;
import com.bugjc.ea.opensdk.test.service.CyclicBarrierTask;

public class JobFindCyclicBarrierTask implements CyclicBarrierTask {
    @Override
    public int execTask() {
        try {
            //创建请求参数对象
            FindBody.RequestBody requestBody = new FindBody.RequestBody();
            requestBody.setJobId(RandomUtil.randomNumbers(20));

            Result result = HttpUtil.getJobService(EnvUtil.getDevServer()).findJob(JobPathInfo.JOB_FIND_PATH_V1, requestBody);
            return result.getCode();
        }catch (Exception ex){
            return ResultCode.INTERNAL_SERVER_ERROR.getCode();
        }

    }
}
