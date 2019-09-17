package com.bugjc.ea.http.opensdk.service;

import com.bugjc.ea.http.opensdk.api.UserPathInfo;
import com.bugjc.ea.http.opensdk.core.dto.Result;

import java.io.IOException;

/**
 * @Author yangqing
 * @Date 2019/9/12 14:09
 **/
public interface UserService {
    /**
     * 获取用户信息
     * @param userPathInfo
     * @param userId
     * @return
     * @throws IOException
     */
    Result getUserInfo(UserPathInfo userPathInfo, String userId) throws IOException;

    /**
     * 余额支付
     * @param userPathInfo
     * @param userId
     * @param amt
     * @return
     * @throws IOException
     */
    Result balancePay(UserPathInfo userPathInfo, String userId, int amt) throws IOException;
}
