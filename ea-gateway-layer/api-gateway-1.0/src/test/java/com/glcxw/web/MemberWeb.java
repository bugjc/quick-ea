package com.glcxw.web;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.glcxw.gateway.core.dto.Result;
import com.glcxw.gateway.core.util.StrSortUtil;
import com.glcxw.util.Des3Util;
import com.glcxw.util.HttpParamUtil;
import com.glcxw.util.MemberWebHttpUtil;
import com.glcxw.util.WebHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aoki
 */
@Slf4j
public class MemberWeb {

    private static String API_GATEWAY = "https://ssl.glchuxingwang.com/uat_api_gateway";

//    @Resource
//    private MemberApiClient memberApiClient;
//
//    @Test
//    public void testCheckToken(){
//        String memberId = "5164";
//        String token = "4a56df41a4fe42bb9a5278604295d22c";
//        Result result = memberApiClient.checkToken(memberId, token);
//        log.info("校验Token应答结果：{}", JSON.toJSONString(result));
//    }


    @Test
    public void testCheckSmsCode() throws Exception {

        API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/memberapi/common/sms/newCheckCode";

        Map<String,Object> params = new HashMap<>();
        params.put("mobileNo","13022010456");
        params.put("code","245047");
        params.put("type","5");

        String random = RandomUtil.randomNumbers(24);
        Result result = MemberWebHttpUtil.post(HttpParamUtil.MEMBER_TYPE, url, params, random);
        log.info("校验SMS应答结果：{}", JSON.toJSONString(result));

    }


    @Test
    public void testRefreshToken() throws Exception {

        API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/memberapi/api/new/refreshToken";

        Map<String,Object> params = new HashMap<>();
        params.put("sign","123456");

        String content = StrSortUtil.sortString(JSON.toJSONString(params));
        String random = RandomUtil.randomNumbers(24);
        Result result = WebHttpUtil.post(HttpParamUtil.MEMBER_TYPE, url, content, random);
        log.info("应答结果：{}", JSON.toJSONString(result));

    }

    @Test
    public void testNewLogin() throws Exception {

        API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/memberapi/api/new/login";

        String random = RandomUtil.randomNumbers(24);
        Map<String,Object> params = new HashMap<>();
        params.put("loginName", "13022010456");
        params.put("password", Des3Util.encode(random, "123456"));

        Map<String, Object> riskInfo = new HashMap<>();
        riskInfo.put("deviceName", "NEM-TL00H");
        riskInfo.put("deviceId", "efa4040ff9db5af4069d3aa821449c29");
        riskInfo.put("type", 0);
        riskInfo.put("ip", "117.141.32.202");

        params.put("riskInfo", JSON.toJSONString(riskInfo));

        Result result = MemberWebHttpUtil.post(HttpParamUtil.MEMBER_TYPE, url, params, random);
        log.info("应答结果：{}", JSON.toJSONString(result));

    }


}
