package com.bugjc.ea.server.message.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.ResultCode;
import com.bugjc.ea.server.message.api.ApiClient;
import com.bugjc.ea.server.message.env.EnvUtil;
import com.bugjc.ea.server.message.web.io.user.UserLoginBody;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 充电业务
 * @author aoki
 * @date ${date}
 */
@Slf4j
public class UserLoginWebTest {

    private ApiClient apiClient =  new ApiClient(EnvUtil.EnvType.DEV);

    /**
     * 充电地图
     */
    @Test
    public void testMap() {
        UserLoginBody.RequestBody requestBody = new UserLoginBody.RequestBody();
        requestBody.setUsername("Jack");
        requestBody.setPassword("123456");

        String bodyData = JSON.toJSONString(requestBody);
        log.info("Parameter:{}",bodyData);

        Result result = apiClient.doPost(ApiClient.ContentPath.USER_LOGIN_PATH, bodyData);
        if (result.getCode() != ResultCode.SUCCESS.getCode()){
            log.error("系统错误：{}",result.getMessage());
            return;
        }

        log.info(result.toString());
        JSONObject data = (JSONObject) result.getData();
        log.info("ResponseResult：{}",data.toJSONString());
    }
}
