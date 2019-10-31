package com.bugjc.ea.server.job.core.enums;

/**
 * 任务业务失败应答码
 * @author aoki
 */
public enum JobFailCode {
    /**
     * 业务应答码
     */
    Success(0,"操作成功"),
    REPETITION_KEY_ERROR(1,"任务已创建"),
    NOT_FOUND_ERROR(2,"任务不存在"),
    REMINDER_TIME_SHORT_ERROR(3,"提醒时间不能早于当前系统时间"),
    CALLBACK_PARAMETER_MISSING_ERROR(4,"http 回调参数缺失"),
    Error(99,"操作失败");

    private final int code;
    private final String desc;

    JobFailCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
