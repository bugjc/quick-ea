package com.bugjc.ea.http.opensdk.service.impl;

import com.alibaba.fastjson.JSON;
import com.bugjc.ea.http.opensdk.api.UserPathInfo;
import com.bugjc.ea.http.opensdk.core.dto.Result;
import com.bugjc.ea.http.opensdk.service.HttpService;
import com.bugjc.ea.http.opensdk.service.UserService;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author yangqing
 * @Date 2019/9/12 14:12
 **/
public class UserServiceImpl implements UserService {

    @Setter
    @Getter
    private HttpService httpService;

    public class RequestField {
        //用户ID
        static final String USER_ID = "userId";
        //消费金额（单位：分）
        static final String AMT = "amt";
    }

    @Override
    public Result getUserInfo(UserPathInfo userPathInfo, String userId) throws IOException {
        Map<String,Object> param = new HashMap<>();
        param.put(RequestField.USER_ID,userId);
        return httpService.post(userPathInfo.getPath(),userPathInfo.getVersion(), JSON.toJSONString(param));
    }

    @Override
    public Result balancePay(UserPathInfo userPathInfo, String userId, int amt) throws IOException {
        Map<String,Object> param = new HashMap<>();
        param.put(RequestField.USER_ID,userId);
        param.put(RequestField.AMT,amt);
        return httpService.post(userPathInfo.getPath(),userPathInfo.getVersion(), JSON.toJSONString(param));
    }
}
