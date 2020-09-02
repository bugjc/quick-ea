package com.glcxw.wechat.service.impl;

import com.glcxw.wechat.core.dto.Result;
import com.glcxw.wechat.core.dto.ResultGenerator;
import com.glcxw.wechat.core.enums.TemplateStatusEnum;
import com.glcxw.wechat.model.WxMsgRecord;
import com.glcxw.wechat.service.WeChatService;
import com.glcxw.wechat.service.WxMsgRecordService;
import com.glcxw.wechat.web.reqbody.TemplateMessageDO;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author aoki
 */
@Slf4j
@Service
public class WeChatServiceImpl implements WeChatService {

    @Resource
    private WxMpService wxMpService;
    @Resource
    private WxMsgRecordService wxMsgRecordService;

    @Override
    public boolean isSubscribe(String openId) {
        try {
            WxMpUser user = wxMpService.getUserService().userInfo(openId);
            return user.getSubscribe();
        }catch (WxErrorException ex){
            log.error(ex.getMessage(),ex);
            return false;
        }

    }

    @Override
    public Result sendTemplateMsg(Integer userId, TemplateMessageDO.SendTemplateMsgDO sendTemplateMsgDO) {

        try {
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(sendTemplateMsgDO.getToUser())
                    .templateId(sendTemplateMsgDO.getTemplateId())
                    .url(sendTemplateMsgDO.getUrl())
                    .build();
            if (sendTemplateMsgDO.getMiniProgram() != null){
                templateMessage.setMiniProgram(sendTemplateMsgDO.getMiniProgram());
            }
            templateMessage.setData(sendTemplateMsgDO.getWxMpTemplateData());

            //记录发送数据
            String magId = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            WxMsgRecord wxMsgRecord = new WxMsgRecord();
            wxMsgRecord.setUserId(userId);
            wxMsgRecord.setMsgId(magId);
            wxMsgRecord.setCreateTime(new Date());
            wxMsgRecord.setFormUserName(sendTemplateMsgDO.getToUser());
            wxMsgRecord.setStatus(TemplateStatusEnum.WAIT_PUSH.getStatus());
            wxMsgRecordService.insert(wxMsgRecord);
            log.info("成功发送消息到微信通知服务，下一步由微信推送消息到用户微信公众号内。");
            return ResultGenerator.genSuccessResult();
        }catch (WxErrorException ex){
            log.error(ex.getMessage(),ex);
            log.error("发送消息到微信通知服务失败，原因：{}", ex.getMessage());
            return ResultGenerator.genFailResult(ex.getError().getErrorMsg());
        }

    }
}
