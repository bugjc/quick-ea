package com.bugjc.ea.server.job.model.bo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务表
 * @author aoki
 */
@Data
public class JobBO implements Serializable {
    /**
     * 主键，业务方提供的唯一key
     */
    private String jobId;

    /**
     * 定时提醒时间
     */
    private Date execTime;

    /**
     * 状态(0:创建任务,1:执行任务中,2:任务执行成功,3:任务执行失败)
     */
    private Integer status;


    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 创建时间
     */
    private String createTime;



}