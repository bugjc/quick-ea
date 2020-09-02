package com.glcxw.web;

import com.alibaba.fastjson.JSON;
import com.glcxw.util.WebHttpUtil;
import com.glcxw.wechat.core.dto.Result;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aoki
 */
@Slf4j
public class TemplateMessageWeb {

    @Test
    public void testUUID(){
        System.out.println();
    }

    /**
     * 发送模板消息
     */
    @Test
    public void testSendTemplateMsg(){
//        Map<String,Object> params = new HashMap<>();
//        params.put("toUser",5164);
//        //prod:	T8hTiY6Iv8H1WmUt5wAXT1MFfBE2242OVCop4-OD_2Y    test:Z-ixboIzbaKPn0Uv0Sg8wiOTm5B_sYon3FvuLCUL9tU
//        params.put("templateId","T8hTiY6Iv8H1WmUt5wAXT1MFfBE2242OVCop4-OD_2Y");
//        //params.put("templateId","Z-ixboIzbaKPn0Uv0Sg8wiOTm5B_sYon3FvuLCUL9tU");
//        params.put("url","http://wechat-web.ngrok.bugjc.com/pages/index.html");
//
//        //模板数据
//        List<WxMpTemplateData> list = new ArrayList<>();
//        list.add(new WxMpTemplateData("first", DateUtil.now()));
//        list.add(new WxMpTemplateData("hotelName","X00000000001"));
//        list.add(new WxMpTemplateData("voucher number","X00000000001,X00000000002"));
//        list.add(new WxMpTemplateData("remark", RandomStringUtils.randomAlphanumeric(100)));
//        params.put("wxMpTemplateData",list);


        Map<String,Object> params = new HashMap<>();
        params.put("toUser",5164);
        params.put("templateId","3lIIGotmHqYjCgTs-IM9_l9_csTj_XO3XJiCXbVyYGM");
        //params.put("url","http://wechat-web.ngrok.bugjc.com/pages/index.html");

        //模板数据
        List<WxMpTemplateData> list = new ArrayList<>();

        list.add(new WxMpTemplateData("accountType", "类型","#Ffffff"));
        list.add(new WxMpTemplateData("account","通付码","#Ffffff"));
        list.add(new WxMpTemplateData("time","2018-08-08","#Ffffff"));
        list.add(new WxMpTemplateData("remark", "活动名：敲打五色石比赛如有疑问，请资讯13745456787。","#Ffffff"));
        params.put("wxMpTemplateData",list);

        String json = JSON.toJSONString(params);
        log.info(json);
        String url = "http://127.0.0.1:8085/wechat/mp/sendTemplateMsg";
        url = "http://192.168.35.17:8085/wechat/mp/sendTemplateMsg";
        try {
            Result result = WebHttpUtil.post(url,params);
            log.info(result.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
