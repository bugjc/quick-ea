package com.bugjc.ea.opensdk.http.service.impl;

import com.bugjc.ea.opensdk.http.api.AuthPathInfo;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.model.auth.QueryTokenBody;
import com.bugjc.ea.opensdk.http.model.auth.VerifyTokenBody;
import com.bugjc.ea.opensdk.http.service.AuthService;
import com.bugjc.ea.opensdk.http.service.HttpService;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * 平台授权认证服务
 * @author aoki
 * @date 2019/11/4
 * **/
public class AuthServiceImpl implements AuthService {

    @Setter
    @Getter
    private HttpService httpService;

    @Override
    public Result getToken(AuthPathInfo authPathInfo, QueryTokenBody.RequestBody requestBody) throws IOException {
        return httpService.post(authPathInfo.getPath(), authPathInfo.getVersion(), requestBody.toString());
    }

    @Override
    public Result verifyToken(AuthPathInfo authPathInfo, VerifyTokenBody.RequestBody requestBody) throws IOException {
        return httpService.post(authPathInfo.getPath(), authPathInfo.getVersion(), requestBody.toString());
    }
}
