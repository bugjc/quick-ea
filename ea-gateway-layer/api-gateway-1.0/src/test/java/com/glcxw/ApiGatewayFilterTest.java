package com.glcxw;

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
 * A --> B --> C
 * @author aoki
 */
@Slf4j
public class ApiGatewayFilterTest {

    private final static String API_GATEWAY = "http://127.0.0.1:8085";

    @Test
    public void testFilterChain() throws Exception {

        //请求地址
        String url = API_GATEWAY + "/test/user";
        //参数
        Map<String, Object> params = new HashMap<>();

        String content = JSON.toJSONString(params);
        //获取应答结果
        String random = RandomUtil.randomNumbers(24);
        Result result = WebHttpUtil.post(HttpParamUtil.CONSUME_QR_CODE_TYPE, url, content, random);
        //打印
        log.info(result.toString());
    }


}
