package com.bugjc.ea.jwt.service;

import com.bugjc.ea.jwt.model.User;
import com.bugjc.ea.jwt.web.reqbody.userregister.UserRegisterRepBody;

public interface UserService {

    /**
     * 注册用户
     * @param userRegisterRepBody
     */
    void register(UserRegisterRepBody userRegisterRepBody);
}
