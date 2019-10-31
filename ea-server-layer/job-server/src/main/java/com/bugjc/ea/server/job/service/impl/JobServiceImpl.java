package com.bugjc.ea.server.job.service.impl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bugjc.ea.server.job.core.api.AccessPartyApiClient;
import com.bugjc.ea.server.job.core.component.NettyTimerComponent;
import com.bugjc.ea.server.job.core.constants.JobConstants;
import com.bugjc.ea.server.job.core.enums.JobFailCode;
import com.bugjc.ea.server.job.core.enums.business.JobStatus;
import com.bugjc.ea.server.job.core.task.ExecJobTimeoutTask;
import com.bugjc.ea.server.job.core.util.SpringContextUtil;
import com.bugjc.ea.server.job.mapper.JobMapper;
import com.bugjc.ea.server.job.model.Job;
import com.bugjc.ea.server.job.model.bo.JobBO;
import com.bugjc.ea.server.job.model.page.JobPage;
import com.bugjc.ea.server.job.service.JobService;
import com.bugjc.ea.server.job.web.io.job.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 任务实现类
 *
 * @author aoki
 * @date 2019/9/23 16:06
 **/
@Slf4j
@Service
public class JobServiceImpl implements JobService {

    @Resource
    private JobMapper jobMapper;
    @Resource
    private NettyTimerComponent nettyTimerComponent;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CreateBody.ResponseBody createJob(CreateBody.RequestBody requestBody) {
        CreateBody.ResponseBody responseBody = new CreateBody.ResponseBody();
        try {
            //获取回调参数信息对象
            AccessPartyApiClient.HttpCallbackInfo httpObject = AccessPartyApiClient.HttpCallbackInfo.convert(requestBody.getHttpCallbackInfo());
            if (StrUtil.isBlank(httpObject.getUrl())) {
                responseBody.setFailCode(JobFailCode.CALLBACK_PARAMETER_MISSING_ERROR.getCode());
                return responseBody;
            }

            if (StrUtil.isBlank(httpObject.getRequestBody())) {
                responseBody.setFailCode(JobFailCode.CALLBACK_PARAMETER_MISSING_ERROR.getCode());
                return responseBody;
            }

            Date currentDate = new Date();
            Date execDate = DateUtil.parseDateTime(requestBody.getExecTime());
            if (execDate.compareTo(currentDate) < 0) {
                log.info("创建任务失败，任务执行时间不能早于系统当前时间！任务执行时间：{}，系统时间：{}", DateUtil.formatDateTime(execDate), DateUtil.formatDateTime(currentDate));
                responseBody.setFailCode(JobFailCode.REMINDER_TIME_SHORT_ERROR.getCode());
                return responseBody;
            }

            Job job = jobMapper.selectById(requestBody.getJobId());
            if (job == null) {
                job = new Job();
                BeanUtil.copyProperties(requestBody, job);
                job.setCreateTime(new Date());
                jobMapper.insert(job);
                log.info("创建任务成功，任务ID：{}", job.getJobId());
            } else {
                BeanUtil.copyProperties(requestBody, job);
                job.setUpdateTime(new Date());
                jobMapper.updateById(job);
                log.info("更新任务成功，任务ID：{}", job.getJobId());
            }
            this.execJob(job);
            return responseBody;
        } catch (DuplicateKeyException ex) {
            responseBody.setFailCode(JobFailCode.REPETITION_KEY_ERROR.getCode());
            return responseBody;
        }
    }

    @Override
    public DelBody.ResponseBody delJob(DelBody.RequestBody requestBody) {
        jobMapper.deleteById(requestBody.getJobId());
        return new DelBody.ResponseBody();
    }

    @Override
    public UpdBody.ResponseBody updJob(UpdBody.RequestBody requestBody) {
        UpdBody.ResponseBody responseBody = new UpdBody.ResponseBody();
        Job job = jobMapper.selectById(requestBody.getJobId());
        if (job == null) {
            responseBody.setFailCode(JobFailCode.NOT_FOUND_ERROR.getCode());
            return responseBody;
        }

        Date execDate = DateUtil.parseDateTime(requestBody.getExecTime());
        Date currentDate = new Date();
        if (execDate.compareTo(new Date()) < 0) {
            log.info("创建任务失败，任务执行时间不能早于系统当前时间！任务执行时间：{}，系统时间：{}", DateUtil.formatDateTime(execDate), DateUtil.formatDateTime(currentDate));
            responseBody.setFailCode(JobFailCode.REMINDER_TIME_SHORT_ERROR.getCode());
            return responseBody;
        }

        //更新方法恢复到待执行状态以及更新执行时间。
        job.setUpdateTime(new Date());
        job.setExecTime(execDate);
        job.setHttpCallbackInfo(requestBody.getHttpCallbackInfo());
        job.setStatus(JobStatus.AwaitExec.getStatus());
        jobMapper.updateById(job);
        this.execJob(job);
        return responseBody;
    }

    @Override
    public FindBody.ResponseBody findJob(FindBody.RequestBody requestBody) {
        FindBody.ResponseBody responseBody = new FindBody.ResponseBody();
        Job job = jobMapper.selectById(requestBody.getJobId());
        if (job == null) {
            responseBody.setFailCode(JobFailCode.NOT_FOUND_ERROR.getCode());
            return responseBody;
        }

        FindBody.ResponseBody.Job responseJob = new FindBody.ResponseBody.Job();
        responseJob.setExecTime(DateUtil.formatDateTime(job.getExecTime()));
        responseJob.setHttpCallbackInfo(job.getHttpCallbackInfo());
        requestBody.setJobId(job.getJobId());
        responseBody.setJob(responseJob);
        return responseBody;
    }

    @Override
    public ListBody.ResponseBody findJobPage(ListBody.RequestBody requestBody) {

        //创建应答对象
        ListBody.ResponseBody responseBody = new ListBody.ResponseBody();

        try {
            //总任务数
            int totalNumberOfTasks = jobMapper.selectCount(null);
            responseBody.setTotalNumberOfTasks(totalNumberOfTasks);

            //统计今日今日新增数
            int newNumberAddedToday = jobMapper.countNewNumberAddedToday();
            responseBody.setNewNumberAddedToday(newNumberAddedToday);

            //统计等待执行任务数
            int numberOfTasksWaitingToBeExecuted = jobMapper.countNumberOfTasksWaitingToBeExecuted();
            responseBody.setNumberOfTasksWaitingToBeExecuted(numberOfTasksWaitingToBeExecuted);

            //查询分页数据
            JobPage<JobBO> jobPage = new JobPage<>(requestBody.getCurrent(),requestBody.getSize());
            jobPage.setStatus(StrUtil.isBlank(requestBody.getStatus()) ? null : Integer.valueOf(requestBody.getStatus()));
            Page<JobBO> jobListPage = jobMapper.selPage(jobPage);
            responseBody.setPage(jobListPage);

        }catch (Exception ex){
            log.error("获取分页列表数据异常："+ex.getMessage(),ex);
            responseBody.setFailCode(JobFailCode.Error.getCode());
        }

        return responseBody;
    }

    @Override
    public void execJob() throws Exception {
        //查询即将要执行的任务
        Date currentDate = new Date();
        String startTime = DateUtil.formatDateTime(currentDate);
        String endTime = DateUtil.offsetMinute(currentDate, JobConstants.REMINDER_TIME_THRESHOLD).toString();
        List<Job> jobList = jobMapper.selRecentlyByStatusAndExecTime(JobStatus.Create.getStatus(), startTime, endTime);
        if (jobList.isEmpty()) {
            //没有快要执行的任务，忽略
            return;
        }

        for (Job job : jobList) {
            this.execJob(job);
        }
    }


    /**
     * 添加任务到待执行队列中
     *
     * @param job
     */
    private void execJob(Job job) {
        Date currentDate = new Date();
        if (DateUtil.offsetMinute(currentDate, JobConstants.REMINDER_TIME_THRESHOLD).compareTo(job.getExecTime()) <= 0) {
            //任务执行时间不在即将执行的范围内（5分钟内执行）
            return;
        }

        //待执行时间低于5分钟，加入到执行队列中
        //计算执行时间，创建执行任务，添加到执行队列中
        long expTime = DateUtil.betweenMs(job.getExecTime(), currentDate);
        ExecJobTimeoutTask execJobTimeoutTask = SpringContextUtil.getBean(ExecJobTimeoutTask.class);
        execJobTimeoutTask.setJob(job);
        nettyTimerComponent.addTask(job.getJobId(), execJobTimeoutTask, expTime, TimeUnit.MILLISECONDS);
        log.info("添加待执行任务到队列中，任务ID:{},{} 秒后执行！", job.getJobId(), expTime / 1000);

        //更新任务状态
        job.setStatus(JobStatus.AwaitExec.getStatus());
        job.setUpdateTime(currentDate);
        jobMapper.updateById(job);
    }
}
