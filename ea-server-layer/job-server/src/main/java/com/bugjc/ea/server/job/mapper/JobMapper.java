package com.bugjc.ea.server.job.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugjc.ea.server.job.model.Job;
import com.bugjc.ea.server.job.model.bo.JobBO;
import com.bugjc.ea.server.job.model.page.JobPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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