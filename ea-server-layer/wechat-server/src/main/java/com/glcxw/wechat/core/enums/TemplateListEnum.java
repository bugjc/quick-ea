package com.glcxw.wechat.core.enums;

/**
 * @author aoki
 */
public enum TemplateListEnum {
    /**
     * 团购成功通知模板
     */
    GROUP_PURCHASE_SUCCESS_NOTICE(0,"Z-ixboIzbaKPn0Uv0Sg8wiOTm5B_sYon3FvuLCUL9tU", "团购成功通知");
    private int code;
    private String msgNumber;
    private String label;

    TemplateListEnum(int code, String msgNumber,String label){
        this.code = code;
        this.msgNumber = msgNumber;
        this.label = label;
    }
    public int getCode(){
        return code;
    }
    public String getMsgNumber(){
        return msgNumber;
    }
    public String getLabel(){
        return label;
    }
}
