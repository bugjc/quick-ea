package com.bugjc.ea.qrcode.web;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuzh
 * @since 2017/9/2.
 */
@Slf4j
@RunWith(SpringRunner.class)
public class CodeWebTest {

    @Test
    public void testGetQrCode() {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("userId","6831");

        String result = HttpUtil.createPost("http://127.0.0.1:2225/qrcode/get")
                .body(new JSONObject(paramMap).toJSONString())
                .contentType("application/json")
                .execute()
                .body();
        System.out.println(result);
    }


    @Test
    public void testMemberCardList() {
        String json =  HttpUtil.get("http://127.0.0.1:8202/member/card/get/6831");
        JSONObject result = JSON.parseObject(json);
        if (result.getInteger("code") == 200){
            JSONObject data = result.getJSONObject("data");
            System.out.println(data.getString("token"));
            System.out.println(data.getString("phoneno"));
            System.out.println(data.getString("tokentype"));
            System.out.println(data.toJSONString());
        }
        System.out.println(result);
    }

    @Test
    public void test() {
        String json =  HttpUtil.get("http://127.0.0.1:2225/test");
        System.out.println(json);
    }
}
