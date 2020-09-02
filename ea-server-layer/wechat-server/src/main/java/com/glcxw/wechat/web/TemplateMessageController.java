package com.glcxw.wechat.web;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glcxw.wechat.core.api.MemberApiClient;
import com.glcxw.wechat.core.dto.Result;
import com.glcxw.wechat.core.dto.ResultCode;
import com.glcxw.wechat.core.dto.ResultGenerator;
import com.glcxw.wechat.service.WeChatService;
import com.glcxw.wechat.web.reqbody.TemplateMessageDO;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 微信模板消息
 * @author aoki
 */
@Slf4j
@RequestMapping("/mp")
@RestController
public class TemplateMessageController {

    @Resource
    private MemberApiClient memberApiClient;
    @Resource
    private WeChatService weChatService;

    /**
     * 判断会员是否已关注公众号
     * @param isSubscribeDO
     * @return
     */
    @PostMapping("isSubscribe")
    public Result isSubscribe(@Valid @RequestBody TemplateMessageDO.IsSubscribeDO isSubscribeDO){

        log.info("执行判断会员是否关注公众号方法，参数：{}",JSON.toJSONString(isSubscribeDO));

        Result result = memberApiClient.findByMemberId(isSubscribeDO.getMemberId());
        if (result.getCode() != ResultCode.SUCCESS.code){
            return ResultGenerator.genFailResult(ResultCode.MEMBER_NOT_FOUND.code,"无效的会员");
        }

        JSONObject data = (JSONObject) result.getData();
        String openid = data.getString("PublicNumberOpenID");
        if (StrUtil.isBlank(openid)){
            return ResultGenerator.genFailResult(ResultCode.NOT_BIND_WE_CHAT.code,"还未绑定微信公众号");
        }

        if (!weChatService.isSubscribe(openid)){
            return ResultGenerator.genFailResult(ResultCode.NOT_SUBSCRIBE.code,"还未关注公众号");
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 发送模板消息
     * @return
     * @throws WxErrorException
     */
    @PostMapping("sendTemplateMsg")
    public Result sendTemplateMsg(@Valid @RequestBody TemplateMessageDO.SendTemplateMsgDO sendTemplateMsgDO) {

        log.info("执行发送模板消息方法，参数：{}", JSON.toJSONString(sendTemplateMsgDO));

        //1、查询会员openid
        String userId = sendTemplateMsgDO.getToUser();
        Result result = memberApiClient.findByMemberId(userId);
        if (result.getCode() != ResultCode.SUCCESS.code){
            return ResultGenerator.genFailResult(ResultCode.MEMBER_NOT_FOUND.code,"无效的会员");
        }

        JSONObject data = (JSONObject) result.getData();
        String openid = data.getString("PublicNumberOpenID");
        if (StrUtil.isBlank(openid)){
            return ResultGenerator.genFailResult(ResultCode.NOT_BIND_WE_CHAT.code,"还未绑定微信公众号");
        }

        sendTemplateMsgDO.setToUser(openid);
        if (!weChatService.isSubscribe(openid)){
            return ResultGenerator.genFailResult(ResultCode.NOT_SUBSCRIBE.code,"还未关注公众号");
        }

        return weChatService.sendTemplateMsg(Integer.valueOf(userId),sendTemplateMsgDO);

    }


    /**
     * 发送模板消息（手机号）
     *
     * @return
     * @throws WxErrorException
     */
    @PostMapping("sendTemplateMsgByPhone")
    public Result sendTemplateMsgByPhone(@Valid @RequestBody TemplateMessageDO.SendTemplateMsgDO sendTemplateMsgDO) {

        log.info("执行手机号发送模板消息方法，参数：{}", JSON.toJSONString(sendTemplateMsgDO));

        //1、查询会员openid
        String userId = sendTemplateMsgDO.getToUser();
        Result result = memberApiClient.findByPhone(userId);
        if (result.getCode() != ResultCode.SUCCESS.code) {
            return ResultGenerator.genFailResult(ResultCode.MEMBER_NOT_FOUND.code, "无效的会员");
        }

        JSONObject data = (JSONObject) result.getData();
        String openid = data.getString("PublicNumberOpenID");
        if (StrUtil.isBlank(openid)) {
            return ResultGenerator.genFailResult(ResultCode.NOT_BIND_WE_CHAT.code, "还未绑定微信公众号");
        }

        sendTemplateMsgDO.setToUser(openid);
        if (!weChatService.isSubscribe(openid)) {
            return ResultGenerator.genFailResult(ResultCode.NOT_SUBSCRIBE.code, "还未关注公众号");
        }

        return weChatService.sendTemplateMsg(Integer.valueOf(userId), sendTemplateMsgDO);

    }
}
