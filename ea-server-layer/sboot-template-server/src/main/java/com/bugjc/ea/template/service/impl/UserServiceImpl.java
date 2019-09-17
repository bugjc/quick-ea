package com.bugjc.ea.template.service.impl;


import cn.hutool.core.util.RandomUtil;
import com.bugjc.ea.template.core.enums.UserLoginFailCode;
import com.bugjc.ea.template.service.UserService;
import com.bugjc.ea.template.web.io.user.UserLoginBody;

/**
 * @Author yangqing
 * @Date 2019/9/17 17:12
 **/
public class UserServiceImpl implements UserService {

    @Override
    public UserLoginBody.ResponseBody login(UserLoginBody.RequestBody requestBody, UserLoginBody.ResponseBody responseBody) {

        //模拟密码验证成功/失败
        boolean flag = RandomUtil.randomBoolean();
        if (!flag){
            //密码验证失败
            responseBody.setFailCode(UserLoginFailCode.WrongPassword.getCode());
            return responseBody;
        }

        //登录成功返回用户信息
        UserLoginBody.ResponseBody.UserInfo userInfo = new UserLoginBody.ResponseBody.UserInfo();
        userInfo.setAge(RandomUtil.randomNumbers(10));
        userInfo.setNickname(RandomUtil.randomString(10));
        responseBody.setUserInfo(userInfo);

        return responseBody;
    }
}
