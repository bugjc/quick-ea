package com.bugjc.ea.server.job.core.enums.business;

/**
 * 任务状态
 * @author aoki
 */
public enum JobStatus {
    /**
     * 任务状态
     */
    Create(0,"创建任务"),
    AwaitExec(1,"待执行任务"),
    Exec(2,"执行任务中"),
    ExecSuccess(3,"任务执行成功"),
    ExecFail(4,"任务执行失败");

    private final int status;
    private final String desc;

    JobStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
