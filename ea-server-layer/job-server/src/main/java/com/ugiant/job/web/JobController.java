package com.ugiant.job.web;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.ugiant.job.core.dto.Result;
import com.ugiant.job.core.dto.ResultGenerator;
import com.ugiant.job.service.JobService;
import com.ugiant.job.web.io.job.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 消息推送服务接口
 *
 * @author aoki
 * @date 2019/8/5 10:01
 **/
@Slf4j
@RestController
@RequestMapping("job")
public class JobController {

    @Resource
    private JobService jobService;

    /**
     * 创建任务接口
     *
     * @param requestBody
     * @return
     */
    @PostMapping("/create")
    public Result create(@Validated @RequestBody CreateBody.RequestBody requestBody) {
        log.info("接收到创建任务请求参数：{}", JSON.toJSONString(requestBody));
        return ResultGenerator.genSuccessResult(jobService.createJob(requestBody));
    }


    /**
     * 分页列表接口
     * @param requestBody
     * @return
     */
    @PostMapping("/list")
    public Result list(@Validated @RequestBody ListBody.RequestBody requestBody) {
        log.info("接收到查询分页列表请求参数: {}", JSON.toJSONString(requestBody));
        return ResultGenerator.genSuccessResult(jobService.findJobPage(requestBody));
    }

    /**
     * 删除任务接口
     *
     * @param requestBody
     * @return
     */
    @PostMapping("/del")
    public Result del(@Validated @RequestBody DelBody.RequestBody requestBody) {
        log.info("接收到删除任务请求参数：{}", JSON.toJSONString(requestBody));
        return ResultGenerator.genSuccessResult(jobService.delJob(requestBody));
    }

    /**
     * 修改任务接口
     *
     * @param requestBody
     * @return
     */
    @PostMapping("/upd")
    public Result upd(@Validated @RequestBody UpdBody.RequestBody requestBody) {
        log.info("接收到修改任务请求参数：{}", JSON.toJSONString(requestBody));
        return ResultGenerator.genSuccessResult(jobService.updJob(requestBody));
    }


    /**
     * 获取任务信息接口
     *
     * @param requestBody
     * @return
     */
    @PostMapping("/find")
    public Result find(@Validated @RequestBody FindBody.RequestBody requestBody) {
        log.info("接收到获取任务信息请求参数：{}", JSON.toJSONString(requestBody));
        return ResultGenerator.genSuccessResult(jobService.findJob(requestBody));
    }

    /**
     * 获取任务状态列表
     *
     * @return
     */
    @PostMapping("/status/list")
    public Result statusList() {
        return ResultGenerator.genSuccessResult(new StatusListBody.ResponseBody());
    }


    /**
     * 每隔5分钟扫描出待执行的任务并加入到执行队列中
     *
     * @throws Exception
     */
    @Async
    @Scheduled(cron = "0 0/5 * * * ? ")
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 3000L, multiplier = 2))
    public synchronized void sync() throws Exception {
        log.info("将待执行的任务加入到执行队列中，扫描时间:{}", DateUtil.formatDateTime(new Date()));
        jobService.execJob();
    }
}
