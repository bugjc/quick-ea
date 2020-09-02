package com.glcxw.wechat.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信模板消息记录
 * @author aoki
 */
@Data
public class WxMsgRecord implements Serializable {

    /**
     * 消息ID
     */
    private String msgId;
    /**
     * 消息状态
     */
    private int status;
    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 公众号微信号
     */
    private String toUserName;

    /**
     * 接收模板消息的用户的openid
     */
    private String formUserName;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

}
