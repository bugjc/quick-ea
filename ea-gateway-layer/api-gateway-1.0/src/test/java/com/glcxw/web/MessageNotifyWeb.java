package com.glcxw.web;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.gateway.core.dto.Result;
import com.glcxw.util.HttpParamUtil;
import com.glcxw.util.WebHttpUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author aoki
 */
@Slf4j
public class MessageNotifyWeb {

    private static final String API_GATEWAY = "http://api.gateway.glchuxingwang.com";

    @Test
    public void testsSubscribe() throws Exception {
        //API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/wechat/mp/isSubscribe";
        String memberId = "1500";
        //参数
        Map<String, Object> params = new HashMap<>();
        params.put("memberId",memberId);

        String random = RandomUtil.randomNumbers(24);
        String content = JSON.toJSONString(params);
        //获取应答结果
        Result result = WebHttpUtil.post(HttpParamUtil.CONSUME_QR_CODE_TYPE, url, content, random);
        //打印
        log.info(result.toString());
    }

    @Test
    public void testMpMessageTemplate() throws Exception {

        /**会员消费通知**/
        String memberMessageTemplateId = "3lIIGotmHqYjCgTs-IM9_l9_csTj_XO3XJiCXbVyYGM";
        TemplateMessageDO.SendTemplateMsgDO sendTemplateMsgDO = new TemplateMessageDO.SendTemplateMsgDO();
        sendTemplateMsgDO.setToUser("5164");
        //sendTemplateMsgDO.setUrl("http://wechat.glchuxingwang.com/record/pages/ylpaidcodeconsumerrecords.html");
        sendTemplateMsgDO.setTemplateId(memberMessageTemplateId);
        List<TemplateMessageDO.WxMpTemplateData> list = new ArrayList<>();
        list.add(new TemplateMessageDO.WxMpTemplateData("productType","消费业务"));
        list.add(new TemplateMessageDO.WxMpTemplateData("name","乘车码"));
        list.add(new TemplateMessageDO.WxMpTemplateData("accountType","金额"));
        list.add(new TemplateMessageDO.WxMpTemplateData("account",new BigDecimal("1.8") + "元","#f08080"));
        list.add(new TemplateMessageDO.WxMpTemplateData("time", DateUtil.formatDateTime(new Date())));
        sendTemplateMsgDO.setWxMpTemplateData(list);

        //API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/wechat/mp/sendTemplateMsg";

        String random = RandomUtil.randomNumbers(24);
        String content = JSON.toJSONString(sendTemplateMsgDO);
        //获取应答结果
        Result result = WebHttpUtil.post(HttpParamUtil.CONSUME_QR_CODE_TYPE, url, content, random);
        log.info(result.toString());
    }


    @Data
    static class TemplateMessageDO{
        /**
         * 发送模板消息
         * @author aoki
         */
        @Data
        static class SendTemplateMsgDO {

            /**
             * 接收者id(会员编号、openid)
             */
            @NotBlank(message = "消息接收者会员编号不能为空")
            private String toUser;
            /**
             * 模板id
             */
            @NotBlank(message = "消息模板id不能为空")
            private String templateId;
            /**
             * 模板跳转链接.
             */
            private String url;

            /**
             * 跳小程序所需数据，不需跳小程序可不用传该数据.
             *
             * @see #url
             */
            private MiniProgram miniProgram;

            /**
             * 模板数据
             */
            @NotEmpty(message = "消息模板数据不能为空")
            private List<WxMpTemplateData> wxMpTemplateData;

        }

        @Data
        static class WxMpTemplateData implements Serializable{
            private static final long serialVersionUID = -5647505266364762002L;
            private String name;
            private String value;
            private String color;

            public WxMpTemplateData() {
            }

            WxMpTemplateData(String name, String value) {
                this.name = name;
                this.value = value;
            }

            WxMpTemplateData(String name, String value, String color) {
                this.name = name;
                this.value = value;
                this.color = color;
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        class MiniProgram implements Serializable {
            private static final long serialVersionUID = -7945254706501974849L;

            private String appid;
            private String pagePath;

            /**
             * 是否使用path，否则使用pagepath.
             * 加入此字段是基于微信官方接口变化多端的考虑
             */
            private boolean usePath = true;
        }
    }


}
