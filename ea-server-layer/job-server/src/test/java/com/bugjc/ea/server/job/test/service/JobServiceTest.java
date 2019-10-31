package com.bugjc.ea.server.job.test.service;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.bugjc.ea.server.job.model.Job;
import com.bugjc.ea.server.job.mapper.JobMapper;
import com.bugjc.ea.server.job.test.Tester;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j

public class JobServiceTest extends Tester {

    @Resource
    private JobMapper jobMapper;

    @Test
    public void testInsert() {
        log.info(("----- insert method test ------"));
        Job job = new Job();
        Date date = new Date();
        String jobId = "ASDF" + RandomUtil.randomNumbers(28);
        job.setJobId(jobId);
        job.setCreateTime(date);
        job.setExecTime(DateUtil.offsetMinute(date, RandomUtil.randomInt(3)));
        job.setHttpCallbackInfo("{\n" +
                "\"url\":\"https://127.0.0.1:8076/test/callback\",\n" +
                "\"requestBody\":{\n" +
                "\t\"key1\":\"value1\",\n" +
                "\t\"key2\":\"value2\",\n" +
                "}\n" +
                "}");
        int iRet = jobMapper.insert(job);
        Assert.assertEquals(1, iRet);
    }


    @Test
    public void testSelect() {
        log.info(("----- selectAll method test ------"));
        List<Job> userList = jobMapper.selectList(null);
        userList.forEach(System.out::println);
    }


    @Test
    public void testSelRecentlyByStatusAndExecTime() {
        log.info(("----- SelRecentlyByStatusAndExecTime method test ------"));
        List<Job> userList = jobMapper.selRecentlyByStatusAndExecTime(0, DateUtil.formatTime(new Date()), DateUtil.offsetDay(new Date(), 1).toString());
        userList.forEach(System.out::println);
    }
}
