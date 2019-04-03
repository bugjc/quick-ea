package com.glcxw.web;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.glcxw.gateway.core.dto.Result;
import com.glcxw.util.HttpParamUtil;
import com.glcxw.util.WebHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aoki
 */
@Slf4j
public class UpcWeb {

    private static final String API_GATEWAY = "https://ssl.glchuxingwang.com/uat_api_gateway";

    @Test
    public void testGetQrCode() throws Exception {
        //API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/upc/qrcode/get";
        //参数
        Map<String,Object> params = new HashMap<>();
        params.put("userId","5164");

        String content = JSON.toJSONString(params);
        //获取应答结果
        String random = RandomUtil.randomNumbers(24);
        Result result = WebHttpUtil.post(HttpParamUtil.CCM_TYPE, url, content, random);
        //打印
        log.info(result.toString());
    }

}
