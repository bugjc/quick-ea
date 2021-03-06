package com.bugjc.ea.auth.service;


import com.bugjc.ea.auth.model.AppToken;

/**
  * 令牌服务
  * @author yangqing
  * @date 2019/7/4
  **/
public interface AppTokenService {

    /**
     * 获取token
     * @param operatorId
     * @return
     */
    AppToken findToken(String operatorId);

    /**
     * 校验token
     * @param token
     * @return
     */
    boolean verifyToken(String token);
}
