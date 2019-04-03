package com.glcxw.web;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
public class NewMemberWeb {

    private static String API_GATEWAY = "https://ssl.glchuxingwang.com/test_api_gateway";

    @Test
    public void testMemberCheck() throws Exception {

        //API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/member/v2/member/check ";
        //获取应答结果
        JSONObject param = new JSONObject();
        param.put("userId", "5164");

        String content = JSON.toJSONString(param);
        String random = RandomUtil.randomNumbers(24);
        Result result = WebHttpUtil.post(HttpParamUtil.CCM_TYPE, url, content, random);
        //打印
        log.info(result.toString());
    }

    @Test
    public void testExemptPwdInfo() throws Exception {

        API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/member/exempt/pwd/info";
        //获取应答结果
        JSONObject param = new JSONObject();
        param.put("userId", "5164");

        String content = JSON.toJSONString(param);
        String random = RandomUtil.randomNumbers(24);
        Result result = WebHttpUtil.post(HttpParamUtil.CCM_TYPE, url, content, random);
        //打印
        log.info(result.toString());
    }

    @Test
    public void testUpdPwd() throws Exception {
        API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/member/member/change-pay-password";
        //参数
        String random = RandomUtil.randomNumbers(24);
        String encrypt = ThreeDESUtil.encryptThreeDESECB(random,"123321");
        Map<String,Object> params = new HashMap<>();
        params.put("userId","5164");
        params.put("payPassword",encrypt);
        params.put("confirmPayPassword",encrypt);
        params.put("code","378659");

        String content = JSON.toJSONString(params);
        //获取应答结果
        Result result = WebHttpUtil.post(HttpParamUtil.CONSUME_QR_CODE_TYPE, url, content, random);
        //打印
        log.info(result.toString());
    }

}
