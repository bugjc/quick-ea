package com.bugjc.ea.qrcode.web;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author aoki
 * @date ${date}
 */
@Slf4j
public class ServiceCallWebTest {

    /**
     * 会员服务测试
     */
    @Test
    public void testGetServiceInstance() {
        String json =  HttpUtil.get("http://127.0.0.1:2225/getMemberServiceInstance");
        JSONObject result = JSON.parseObject(json);
        log.info(result.toJSONString());

        json =  HttpUtil.get("http://127.0.0.1:2225/getServiceInstance");
        result = JSON.parseObject(json);
        log.info(result.toJSONString());
    }
}
