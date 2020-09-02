package com.glcxw.wechat.service;

import com.glcxw.wechat.core.dto.Result;
import com.glcxw.wechat.web.reqbody.TemplateMessageDO;

/**
 * @author aoki
 */
public interface WeChatService {

    /**
     * 判断用户是否已关注公众号
     * @param openId
     * @return
     */
    boolean isSubscribe(String openId);

    /**
     * 发送模板消息
     * @param userId
     * @param sendTemplateMsgDO
     * @return
     */
    Result sendTemplateMsg(Integer userId,TemplateMessageDO.SendTemplateMsgDO sendTemplateMsgDO);

}
