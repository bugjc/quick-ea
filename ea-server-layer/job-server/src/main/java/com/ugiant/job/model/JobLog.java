package com.ugiant.job.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务日志表
 * @author aoki
 */
@Data
@TableName("tbs_job_log")
public class JobLog implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "job_log_id",type = IdType.AUTO)
    private int jobLogId;

    /**
     * 任务ID
     */
    @TableField("job_id")
    private String jobId;

    /**
     * http回调地址
     */
    @TableField("http_address")
    private String httpAddress;

    /**
     * http回调参数
     */
    @TableField("http_param")
    private String httpParam;

    /**
     * 调度时间
     */
    @TableField("trigger_time")
    private Date triggerTime;

    /**
     * 调度结果
     */
    @TableField("trigger_code")
    private String triggerCode;

    /**
     * 调度日志
     */
    @TableField("trigger_msg")
    private String triggerMsg;

    /**
     * 回调耗时
     */
    @TableField("time_consuming")
    private int timeConsuming;
}