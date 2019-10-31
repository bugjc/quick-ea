package com.bugjc.ea.server.job.core.enums.business;

/**
 * 任务类型
 * @author aoki
 */
public enum JobType {
    /**
     * 任务类型
     */
    Reminder(1001,"定时提醒任务"),
    ConfirmOrderReminder(1002,"确认订单提醒"),
    OrderStatusChangeReminder(2001,"订单状态变更提醒");

    private final int type;
    private final String desc;

    JobType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
