package com.bugjc.ea.auth.web;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.junit.Test;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aoki
 * @date ${date}
 */
@Slf4j
public class AuthWebTest {

    private final static String SERVER_ADDRESS = "http://127.0.0.1:8001";


    /**
     * 测试账户注册
     */
    @Test
    public void testJwtAuth() {
        //地址
        String url = SERVER_ADDRESS.concat("/jwt/auth");

        Map<String, Object> param = new HashMap<>();
        param.put("username", "admin");
        param.put("password", "password");


        String body = JSON.toJSONString(param);
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody requestBody = RequestBody.create(mediaType, body.getBytes());

        //构建请求
        Request httpRequest = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        //执行请求
        try {
            Response httpResponse = new OkHttpClient().newCall(httpRequest).execute();
            if (httpResponse.isSuccessful()){
                assert httpResponse.body() != null;
                System.out.println(httpResponse.body().string());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        //请求接口
//        String result = HttpUtil.createPost(url).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).body(JSON.toJSONString(param)).execute().body();
//        log.info("应答结果：{}",result);
    }

}
