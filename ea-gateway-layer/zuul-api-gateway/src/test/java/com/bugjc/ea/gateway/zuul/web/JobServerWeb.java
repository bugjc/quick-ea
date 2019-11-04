package com.bugjc.ea.gateway.zuul.web;

import com.bugjc.ea.gateway.zuul.env.EnvUtil;
import com.bugjc.ea.gateway.zuul.util.HttpUtil;
import com.bugjc.ea.opensdk.http.api.JobPathInfo;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.model.job.FindBody;
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
        //创建请求参数对象
        FindBody.RequestBody requestBody = new FindBody.RequestBody();
        requestBody.setJobId("111111111");
        //执行调用
        Result result = HttpUtil.getJobService(EnvUtil.getDevServer()).findJob(JobPathInfo.JOB_FIND_PATH_V1, requestBody);
        log.info("应答结果：{}",result.toString());

    }

}
