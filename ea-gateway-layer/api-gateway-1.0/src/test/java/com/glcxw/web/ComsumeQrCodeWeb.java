package com.glcxw.web;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.glcxw.gateway.core.dto.Result;
import com.glcxw.util.HttpParamUtil;
import com.glcxw.util.ThreeDESUtil;
import com.glcxw.util.WebHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aoki
 */
@Slf4j
public class ComsumeQrCodeWeb {

    private static String API_GATEWAY = "https://ssl.glchuxingwang.com/uat_api_gateway";

    /**
     * 获取二维码 20181115162444
     */
    @Test
    public void testGetQrCode() throws Exception {
        //API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/qrcode/bs/get";
        while (true) {
            try {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("userId", "6831");
                //测试账户2:崔念梦
                String payerInfoStr = "{\"deviceid\":\"8.6106903542812E14\",\"devicetype\":\"1\",\"phoneno\":\"13022010456\",\"sourceip\":\"192.168.36.2\",\"token\":\"6235240000729498614\",\"tokentype\":\"01\"}";
                String random = RandomUtil.randomString(24);
                String payerInfo = ThreeDESUtil.encryptThreeDESECB(random, payerInfoStr);
                paramMap.put("payerInfo", Base64.encode(payerInfo));
                Map<String, Object> riskInfo = new HashMap<>();
                riskInfo.put("deviceName", "NEM-TL00H");
                riskInfo.put("deviceId", "efa4040ff9db5af4069d3aa821449c29");
                riskInfo.put("type", 0);
                riskInfo.put("ip", "117.141.32.202");
                paramMap.put("riskInfo", riskInfo);

                String content = JSON.toJSONString(paramMap);
                Result result = WebHttpUtil.post(HttpParamUtil.CONSUME_QR_CODE_TYPE, url, content, random);
                log.info(result.toString());
                Thread.sleep(5000);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }

    }


    @Test
    public void testZsInfo() throws Exception {
        API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/qrcode/zs/info";
        //参数
        Map<String, Object> params = new HashMap<>();
        params.put("qrCode", RandomUtil.simpleUUID());
        params.put("userId","5433");

        String random = RandomUtil.randomNumbers(24);
        String content = JSON.toJSONString(params);
        //获取应答结果
        Result result = WebHttpUtil.post(HttpParamUtil.CONSUME_QR_CODE_TYPE, url, content, random);
        //打印
        log.info(result.toString());
    }


}
