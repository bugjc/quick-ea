package com.bugjc.ea.opensdk.http.service;

import com.bugjc.ea.opensdk.http.api.AuthPathInfo;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.exception.HttpSecurityException;
import com.bugjc.ea.opensdk.http.model.auth.VerifyTokenBody;

/**
 * 平台授权认证服务
 * @author aoki
 * @date 2019/11/4
 * **/
public interface AuthService {

    /**
     * 获取 token
     * @param authPathInfo
     * @return
     * @throws HttpSecurityException
     */
    Result getToken(AuthPathInfo authPathInfo) throws HttpSecurityException;

    /**
     * 校验 token
     * @param authPathInfo
     * @param requestBody
     * @return
     * @throws HttpSecurityException
     */
    Result verifyToken(AuthPathInfo authPathInfo, VerifyTokenBody.RequestBody requestBody) throws HttpSecurityException;
}
