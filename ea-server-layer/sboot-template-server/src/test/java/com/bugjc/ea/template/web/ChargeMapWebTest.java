package com.bugjc.ea.template.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.http.opensdk.core.dto.Result;
import com.bugjc.ea.http.opensdk.core.dto.ResultCode;
import com.bugjc.ea.template.api.ApiClient;
import com.bugjc.ea.template.env.EnvUtil;
import com.bugjc.ea.template.web.io.user.UserLoginBody;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 充电业务
 * @author aoki
 * @date ${date}
 */
@Slf4j
public class ChargeMapWebTest {

    private ApiClient apiClient =  new ApiClient(EnvUtil.EnvType.DEV);

    /**
     * 充电地图
     */
    @Test
    public void testMap() {
        UserLoginBody.RequestBody requestBody = new UserLoginBody.RequestBody();
        requestBody.setLat(25.112000);
        requestBody.setLng(110.121000);

        String bodyData = JSON.toJSONString(requestBody);
        log.info("Parameter:{}",bodyData);

        Result result = apiClient.doPost(ApiClient.ContentPath.CHARGE_MAP_LIST_PATH, bodyData);
        if (result.getCode() != ResultCode.SUCCESS.getCode()){
            log.error("系统错误：{}",result.getMessage());
            return;
        }

        log.info(result.toString());
        JSONObject data = (JSONObject) result.getData();
        log.info("ResponseResult：{}",data.toJSONString());
    }
}
