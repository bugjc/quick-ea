package com.bugjc.ea.qrcode.web;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

/**
 * @author aoki
 * @date ${date}
 */
public class DependencyWebTest {

    /**
     * 会员服务测试
     */
    @Test
    public void testMemberCardList() {
        String json =  HttpUtil.get("http://127.0.0.1:8202/member/card/get/6831");
        JSONObject result = JSON.parseObject(json);
        if (result.getInteger("code") == 200){
            JSONObject data = result.getJSONObject("db");
            System.out.println(data.getString("token"));
            System.out.println(data.getString("phoneno"));
            System.out.println(data.getString("tokentype"));
            System.out.println(data.toJSONString());
        }
    }
}
