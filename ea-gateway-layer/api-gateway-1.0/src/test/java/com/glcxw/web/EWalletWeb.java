package com.glcxw.web;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.gateway.core.dto.Result;
import com.bugjc.ea.gateway.core.util.StrSortUtil;
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
public class EWalletWeb {

    private static String API_GATEWAY = "https://ssl.glchuxingwang.com/test_api_gateway";

    @Test
    public void tesGetQrCode() throws Exception {

        while (true) {
            try {
                API_GATEWAY = "http://127.0.0.1:8085";
                //请求地址
                String url = API_GATEWAY + "/ewallet/v2/epp/get";

                Map<String, Object> params = new HashMap<>();

                String content = StrSortUtil.sortString(JSON.toJSONString(params));
                String random = RandomUtil.randomNumbers(24);
                Result result = MemberWebHttpUtil.post(HttpParamUtil.TFM_TYPE, url, params, random);
                log.info("应答结果：{}", JSON.toJSONString(result));
                Thread.sleep(2000);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }

        }

    }


    @Test
    public void tesGetQrCodeV2() throws Exception {

        API_GATEWAY = "http://127.0.0.1:8086";
        //请求地址
        String url = API_GATEWAY + "/ewallet/v2/epp/get";

        Map<String,Object> params = new HashMap<>();

        while (true) {
            try {
                String random = RandomUtil.randomNumbers(24);
                Result result = MemberWebHttpUtil.post(HttpParamUtil.TFM_TYPE, url, params, random);
                log.info("应答结果：{}", JSON.toJSONString(result));
                Thread.sleep(1000);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }

        }


    }

}

