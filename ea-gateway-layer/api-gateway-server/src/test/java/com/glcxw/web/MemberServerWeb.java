package com.glcxw.web;

import com.alibaba.fastjson.JSON;
import com.bugjc.ea.http.opensdk.core.dto.Result;
import com.glcxw.env.EnvUtil;
import com.glcxw.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aoki
 */
@Slf4j
public class MemberServerWeb {

    /**
     * 获取二维码 20181115162444
     */
    @Test
    public void testAuth() throws Exception {
        //请求地址
        Map<String, Object> param = new HashMap<>();
        param.put("memberId", "10001");
        Result result = HttpUtil.getHttpService(EnvUtil.getDevServer()).post("/member/info/get","1.0", JSON.toJSONString(param));
        log.info("应答结果：{}",result.toString());

    }

}
