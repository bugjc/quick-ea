package com.glcxw.web;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.gateway.core.dto.Result;
import com.glcxw.util.HttpParamUtil;
import com.glcxw.util.WebHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author aoki
 */
@Slf4j
public class SmsWeb {

    private static String API_GATEWAY = "https://ssl.glchuxingwang.com/api_gateway";

    @Test
    public void testSendSms() throws Exception {
        API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/sms/trade/notify";
        //参数
        String memberId = "5164";
        String token = "d411068489bb4c4ca91d7d05343a240d";
        JSONObject params = new JSONObject();
        params.put("date","2018年08月16日");
        params.put("bankcard","信用卡1887");
        params.put("amt","0.01");
        params.put("phone","13022010456");
        params.put("id","1001");

        String content = JSON.toJSONString(params);
        //获取应答结果
        String random = RandomUtil.randomNumbers(24);
        Result result = WebHttpUtil.post(HttpParamUtil.CONSUME_QR_CODE_TYPE, url, content, random);
        //打印
        log.info(result.toString());
    }

}
