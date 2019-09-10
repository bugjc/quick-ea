package com.bugjc.ea.auth.service;

import com.bugjc.ea.auth.model.User;
import com.bugjc.ea.auth.web.reqbody.userregister.UserRegisterRepBody;

public interface UserService {

    /**
     * 注册用户
     * @param userRegisterRepBody
     */
    void register(UserRegisterRepBody userRegisterRepBody);
}
