package com.bugjc.ea.opensdk.http.service;

import com.bugjc.ea.opensdk.http.api.AuthPathInfo;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.model.auth.QueryTokenBody;
import com.bugjc.ea.opensdk.http.model.auth.VerifyTokenBody;

import java.io.IOException;

/**
 * 平台授权认证服务
 * @author aoki
 * @date 2019/11/4
 * **/
public interface AuthService {

    /**
     * 获取 token
     * @param authPathInfo
     * @param requestBody
     * @return
     */
    Result getToken(AuthPathInfo authPathInfo, QueryTokenBody.RequestBody requestBody) throws IOException;

    /**
     * 校验 token
     * @param authPathInfo
     * @param requestBody
     * @return
     */
    Result verifyToken(AuthPathInfo authPathInfo, VerifyTokenBody.RequestBody requestBody) throws IOException;
}
