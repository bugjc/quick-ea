package com.glcxw.web;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glcxw.gateway.core.dto.Result;
import com.glcxw.util.HttpParamUtil;
import com.glcxw.util.SubAccNoObfuscationUtil;
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
public class SassWeb {

    private static String API_GATEWAY = "https://ssl.glchuxingwang.com/uat_api_gateway";

    @Test
    public void testSassCoBankList() throws Exception {
        API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/sass/co/bank/list";
        Map<String,Object> params = new HashMap<>();
        String content = JSON.toJSONString(params);
        //获取应答结果
        String random = RandomUtil.randomNumbers(24);
        Result result = WebHttpUtil.post(HttpParamUtil.CONSUME_QR_CODE_TYPE, url, content, random);
        //打印
        log.info(result.toString());
    }

    @Test
    public void testGetCardInfo() throws Exception {
        //API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/sass/co/bank/card/info";
        String subAccNo = "6217922989000014";
        String priAccNo = "6228480088453763174";
        JSONObject params = SubAccNoObfuscationUtil.obfuscate(subAccNo);
        //params.put("priAccNo", SubAccNoObfuscationUtil.obfuscate(params.getString("cardNo"), priAccNo));
        params.put("priAccNo","1kXnq2sfbp7PydvzSwDa2sf0d");
        params.put("cardNo","0022");
        String content = JSON.toJSONString(params);
        //获取应答结果
        String random = RandomUtil.randomNumbers(24);
        Result result = WebHttpUtil.post(HttpParamUtil.CONSUME_QR_CODE_TYPE, url, content, random);
        //打印
        log.info(result.toString());
    }

    @Test
    public void testSassCardApply() throws Exception {
        API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/sass/card/apply";
        //参数
        Map<String,Object> params = new HashMap<>();
        params.put("coBankId", "1001");
        params.put("priAccNo", "62162461000000002485");
        params.put("customerNm","杨一");
        params.put("certifId","431126199004252317");
        params.put("phoneNo","13022010456");


        Map<String, Object> applyCardData = new HashMap<>();
        //6228480088453763174,6216261000000000018
        applyCardData.put("priAccNo", "6216261000000000018");
        applyCardData.put("customerNm", "蒙试法");
        // 440421200101012214,307584007998
        applyCardData.put("certifId", "420101196504276658");
        applyCardData.put("phoneNo", "13022010456");
        applyCardData.put("validUntil", "20991231");
        for (int i = 0; i < 20; i++) {
            applyCardData.put("test"+i, RandomUtil.simpleUUID());
        }
        params.put("applyCardData", applyCardData);


        String random = RandomUtil.randomNumbers(24);
        String content = JSON.toJSONString(params);
        content = Base64.encode(ThreeDESUtil.encryptThreeDESECB(random,content));
        //获取应答结果
        Result result = WebHttpUtil.post(HttpParamUtil.CONSUME_QR_CODE_TYPE, url, content, random);
        //打印
        log.info(result.toString());
    }

}
