package com.bugjc.ea.server.job.model;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务表
 * @author aoki
 */
@Data
@TableName("tbs_job")
public class Job implements Serializable {
    /**
     * 主键，业务方提供的唯一key
     */
    @TableId(value = "job_id",type = IdType.INPUT)
    private String jobId;

    /**
     * 定时提醒时间
     */
    @TableField("exec_time")
    private Date execTime;

    /**
     * http回调参数信息
     */
    @TableField("http_callback_info")
    private String httpCallbackInfo;

    /**
     * 状态(0:创建任务,1:执行任务中,2:任务执行成功,3:任务执行失败)
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;



}