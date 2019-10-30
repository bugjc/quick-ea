package com.ugiant.job.web.io.job;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ugiant.job.model.bo.JobBO;
import lombok.Data;

import java.io.Serializable;

/**
 * 获取任务列表分页请求应答对象
 * @author aoki
 * @date 2019/9/23 15:57
 **/
@Data
public class ListBody implements Serializable {

    /**
     * 请求参数对象
     */
    @Data
    public static class RequestBody implements Serializable {
        //当前页
        private long current;
        //每页查询数量，最大500
        private long size = 20;
        //任务状态
        private String status;
    }

    /**
     * 应答参数对象
     */
    @Data
    public static class ResponseBody implements Serializable {

        //业务执行应答码
        private int failCode = 0;

        //总任务数
        private int totalNumberOfTasks;

        //今日新增数
        private int newNumberAddedToday;

        //等待执行任务数
        private int numberOfTasksWaitingToBeExecuted;

        //分页对象
        private Page<JobBO> page;
    }


}
