package com.ugiant.job.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ugiant.job.model.Job;
import com.ugiant.job.model.bo.JobBO;
import com.ugiant.job.model.page.JobPage;
import com.ugiant.job.web.io.job.ListBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 任务 Mapper
 * @author aoki
 */
public interface JobMapper extends BaseMapper<Job> {

    /**
     * 查询指定状态且最近（时间范围内）待执行的任务
     * @param status
     * @param startTime
     * @param endTime
     * @return
     */
    List<Job> selRecentlyByStatusAndExecTime(@Param("status") int status,@Param("startTime")String startTime,@Param("endTime") String endTime);

    /**
     * 统计今日新增任务数
     * @return
     */
    int countNewNumberAddedToday();

    /**
     * 统计等待执行任务数
     * @return
     */
    int countNumberOfTasksWaitingToBeExecuted();

    /**
     * 查询任务分页列表
     * @param jobPage
     * @return
     */
    JobPage<JobBO> selPage(@Param("pg") JobPage<JobBO> jobPage);

}