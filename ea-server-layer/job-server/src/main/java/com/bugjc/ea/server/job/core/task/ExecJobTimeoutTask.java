package com.bugjc.ea.server.job.core.task;

import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.ResultCode;
import com.bugjc.ea.server.job.core.api.AccessPartyApiClient;
import com.bugjc.ea.server.job.core.enums.business.JobStatus;
import com.bugjc.ea.server.job.core.exception.BizException;
import com.bugjc.ea.server.job.mapper.JobLogMapper;
import com.bugjc.ea.server.job.mapper.JobMapper;
import com.bugjc.ea.server.job.model.Job;
import com.bugjc.ea.server.job.model.JobLog;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 执行任务
 * @author aoki
 * @date 2019/9/24 14:39
 **/
@Slf4j
@Scope("prototype")
@Component
public class ExecJobTimeoutTask implements TimerTask {

    /**
     * 任务参数对象
     */
    @Setter
    @Getter
    private Job job;

    @Resource
    private AccessPartyApiClient accessPartyApiClient;

    @Resource
    private JobMapper jobMapper;

    @Resource
    private JobLogMapper jobLogMapper;

    /**
     * 重试多次还是失败的错误标志
     */
    private final static int RETRY_ERROR_MARK_CODE = 0x00;


    /**
     * 执行标记
     */
    private static boolean MARK = true;

    /**
     * 记录开始时间
     */
    private static long START_TIME = 0;

    /**
     * 执行任务
     * @param timeout
     * @throws Exception
     */
    @Retryable(value = { Exception.class }, maxAttempts = 9, backoff = @Backoff(delay = 3000L, multiplier = 2))
    @Override
    public void run(Timeout timeout) throws BizException {
        if (job == null){
            return;
        }

        //只执行一次，记录执行开始时间
        if (MARK){
            MARK = false;
            START_TIME = System.currentTimeMillis();
        }

        //这里是任务的业务逻辑
        log.info("执行任务ID:{} 的状态从 {} 修改为 {}",job.getJobId(),job.getStatus(), JobStatus.Exec.getStatus());
        //修改任务状态
        updateJobStatus(JobStatus.Exec.getStatus());


        //执行任务
        Result result = accessPartyApiClient.doPost(job.getHttpCallbackInfo());
        //回调业务方的任务
        if (result.getCode() != ResultCode.SUCCESS.getCode()){
            //注：如抛出异常则自动重试
            throw new BizException(RETRY_ERROR_MARK_CODE, JSON.toJSONString(result));
        }


        log.info("执行任务成功!将任务ID:{} 的状态从 {} 修改为 {}",job.getJobId(),job.getStatus(), JobStatus.ExecSuccess.getStatus());
        //修改任务状态
        updateJobStatus(JobStatus.ExecSuccess.getStatus());
        //记录成功日志
        writeLog(result);
    }

    /**
     * 任务执行失败记录日志
     * @param e
     */
    @Recover
    public void recover(BizException e) {
        if (RETRY_ERROR_MARK_CODE == e.getCode()){
            log.info("执行任务失败，执行结果：{}",e.getMsg());
            //修改任务状态
            updateJobStatus(JobStatus.ExecFail.getStatus());
            //记录失败日志
            writeLog(JSON.parseObject(e.getMsg(),Result.class));
        }
    }

    /**
     * 修改任务状态
     * @param status
     */
    private void updateJobStatus(int status){
        try {
            job.setStatus(status);
            job.setUpdateTime(new Date());
            jobMapper.updateById(job);
        }catch (Exception ex){
            log.error("修改任务状态错误信息："+ex.getMessage(),ex);
        }
    }

    /**
     * 记录任务日志
     * @param result
     */
    private void writeLog(Result result){
        try {
            //获取 http 参数回调对象信息
            AccessPartyApiClient.HttpCallbackInfo httpObject = AccessPartyApiClient.HttpCallbackInfo.convert(job.getHttpCallbackInfo());
            //记录成功日志
            JobLog jobLog = new JobLog();
            jobLog.setJobId(job.getJobId());
            jobLog.setHttpAddress(httpObject.getUrl());
            jobLog.setHttpParam(httpObject.getRequestBody());
            jobLog.setTriggerMsg(result.getMessage());
            jobLog.setTriggerCode(String.valueOf(result.getCode()));
            jobLog.setTriggerTime(job.getUpdateTime());
            jobLog.setTimeConsuming(Math.toIntExact((System.currentTimeMillis() - START_TIME) / 1000));
            jobLogMapper.insert(jobLog);
            log.info("记录任务执行日志成功！");
        }catch (Exception ex){
            log.error("记录任务执行日志错误信息："+ex.getMessage(),ex);
        }

    }
}
