package com.glcxw.wechat.core.enums;

/**
 * @author aoki
 */
public enum TemplateStatusEnum {
    /**
     * 模板消息状态
     */
    WAIT_PUSH(0,"等待推送"),
    SUCCESS(0,"发送状态为成功"),
    FAIL_USER(1,"发送状态为用户拒绝接收"),
    FAIL_SYSTEM(2,"发送状态为发送失败（非用户拒绝）");
    private int status;
    private String desc;

    TemplateStatusEnum(int status, String desc){
        this.status = status;
        this.desc = desc;
    }
    public int getStatus(){
        return status;
    }
    public String getDesc(){
        return desc;
    }
}
