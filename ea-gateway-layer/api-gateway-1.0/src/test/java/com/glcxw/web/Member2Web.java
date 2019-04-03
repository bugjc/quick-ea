package com.glcxw.web;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.gateway.core.dto.Result;
import com.glcxw.util.Des3Util;
import com.glcxw.util.HttpParamUtil;
import com.glcxw.util.MemberWebHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aoki
 */
@Slf4j
public class Member2Web {

    private static String API_GATEWAY = "https://ssl.glchuxingwang.com/test_api_gateway";

    @Test
    public void testNewLogin() throws Exception {

        //API_GATEWAY = "http://127.0.0.1:8086";

        //请求地址
        String url = API_GATEWAY + "/memberapi/api/new/login";

        String random = RandomUtil.randomNumbers(24);
        Map<String,Object> params = new HashMap<>();
        params.put("loginName", "13022010456");
        params.put("password", Des3Util.encode(random, "aa123456"));

        Map<String, Object> riskInfo = new HashMap<>();
        riskInfo.put("deviceName", "NEM-TL00H");
        riskInfo.put("deviceId", "efa4040ff9db5af4069d3aa821449c29");
        riskInfo.put("type", 0);
        riskInfo.put("ip", "117.141.32.202");

        params.put("riskInfo", JSON.toJSONString(riskInfo));

        Result result = MemberWebHttpUtil.post(HttpParamUtil.MEMBER_TYPE, url, params, random);
        log.info("应答结果：{}", JSON.toJSONString(result));

    }

    @Test
    public void testGetMemberInfo() throws Exception {

        //API_GATEWAY = "http://192.168.35.15:8085";
        //请求地址
        String url = API_GATEWAY + "/memberapi/api/member/logined/getMemberInfo";

        String memberId = "5164";
        String token = "7b19c160a14b4761acea55010abbe6bd";

        Map<String,Object> params = new HashMap<>();
        //params.put("memberId", "330955");
        String str = "memberId="+memberId + "&token=" + token;
        String cryptStr = SecureUtil.md5(str);
        params.put("sign", cryptStr);

        String random = RandomUtil.randomNumbers(24);
        Result result = MemberWebHttpUtil.post(HttpParamUtil.MEMBER_TYPE, url, params, random);
        log.info("应答结果：{}", JSON.toJSONString(result));

    }

    @Test
    public void testPrepay() throws Exception {

        API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/memberapi/third/gateWay/prepay";

        Map<String,Object> params = new HashMap<>();
        params.put("business ", "70");
        params.put("fromType", "APP");
        params.put("notify_type", "1");
        params.put("orderNo", "557201425873924096");
        params.put("orderType", "recharge");
        params.put("order_no", "557201425873924096");
        params.put("payment_method", "30");
        params.put("payment_terminal", "10");
        params.put("payment_type", "30");

        String random = RandomUtil.randomNumbers(24);
        Result result = MemberWebHttpUtil.post(HttpParamUtil.MEMBER_TYPE, url, params, random);
        log.info("应答结果：{}", JSON.toJSONString(result));

    }



    @Test
    public void testLogin() throws Exception {

        API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/memberapi/api/new/login";

        Map<String,Object> params = new HashMap<>();
        params.put("loginName","13422224695");
        params.put("password","nGNNf3bUsl8=");
        Map<String, Object> riskInfo = new HashMap<>();
        riskInfo.put("deviceName", "NEM-TL00H");
        riskInfo.put("deviceId", "efa4040ff9db5af4069d3aa821449c29");
        riskInfo.put("type", 0);
        riskInfo.put("ip", "117.141.32.202");
        params.put("riskInfo", JSON.toJSONString(riskInfo));

        String random = RandomUtil.randomNumbers(24);
        Result result = MemberWebHttpUtil.post(HttpParamUtil.MEMBER_TYPE, url, params, random);
        log.info("应答结果：{}", JSON.toJSONString(result));

    }

    @Test
    public void testUnionPayList() throws Exception {

        API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/memberapi/unionpay/paySortList";

        Map<String,Object> params = new HashMap<>();
        String random = RandomUtil.randomNumbers(24);
        Result result = MemberWebHttpUtil.post(HttpParamUtil.MEMBER_TYPE, url, params, random);
        log.info("应答结果：{}", JSON.toJSONString(result));

    }

}
