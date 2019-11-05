package com.bugjc.ea.opensdk.http.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.opensdk.http.api.AuthPathInfo;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.ResultCode;
import com.bugjc.ea.opensdk.http.core.enums.TokenResultStatusEnum;
import com.bugjc.ea.opensdk.http.model.auth.QueryTokenBody;
import com.bugjc.ea.opensdk.http.model.auth.VerifyTokenBody;
import com.bugjc.ea.opensdk.http.service.AuthService;
import com.bugjc.ea.opensdk.http.service.HttpService;

import java.io.IOException;

/**
 * 平台授权认证服务
 * @author aoki
 * @date 2019/11/4
 * **/
public class AuthServiceImpl implements AuthService {

    private HttpService httpService;
    public AuthServiceImpl(HttpService httpService){
        this.httpService = httpService;
    }

    @Override
    public Result getToken(AuthPathInfo authPathInfo) throws IOException {

        if (httpService == null){
            throw new IllegalStateException("httpService object not set");
        }

        //构建请求对象
        QueryTokenBody.RequestBody requestBody = new QueryTokenBody.RequestBody();
        requestBody.setAppId(httpService.getAppParam().getAppId());
        requestBody.setAppSecret(httpService.getAppParam().getAppSecret());

        //调用接口
        Result result = httpService.post(AuthPathInfo.QUERY_TOKEN_V1.getPath(), AuthPathInfo.QUERY_TOKEN_V1.getVersion(),null, requestBody.toString());

        //根据特定应答码返回正常、重试和忽略状态，获取token在根据状态做相应处理。
        if (result == null){
            result = new Result();
            result.setCode(TokenResultStatusEnum.Retry.getStatus());
            return result;
        }

        if (result.getCode() == ResultCode.SUCCESS.getCode()){
            QueryTokenBody.ResponseBody responseBody = ((JSONObject)result.getData()).toJavaObject(QueryTokenBody.ResponseBody.class);
            if (responseBody.getFailCode() == 0){
                result.setCode(TokenResultStatusEnum.Normal.getStatus());
            } else {
                result.setCode(TokenResultStatusEnum.Ignorable.getStatus());
            }
        }

        return result;
    }

    @Override
    public Result verifyToken(AuthPathInfo authPathInfo, VerifyTokenBody.RequestBody requestBody) throws IOException {
        return httpService.post(authPathInfo.getPath(), authPathInfo.getVersion(), requestBody.toString());
    }
}
