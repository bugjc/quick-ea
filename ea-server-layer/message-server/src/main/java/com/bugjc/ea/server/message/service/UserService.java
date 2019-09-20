package com.bugjc.ea.server.message.service;

import com.bugjc.ea.server.message.web.io.user.UserLoginBody;

/**
 * @Author yangqing
 * @Date 2019/9/17 17:12
 **/
public interface UserService {

    /**
     * 登录
     * @param requestBody
     * @param responseBody
     * @return
     */
    UserLoginBody.ResponseBody login(UserLoginBody.RequestBody requestBody, UserLoginBody.ResponseBody responseBody);
}
