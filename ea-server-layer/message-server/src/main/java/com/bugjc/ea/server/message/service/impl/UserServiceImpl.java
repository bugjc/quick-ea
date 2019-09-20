package com.bugjc.ea.server.message.service.impl;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bugjc.ea.server.message.core.enums.UserLoginFailCode;
import com.bugjc.ea.server.message.event.UserLoginSuccessfulEvent;
import com.bugjc.ea.server.message.mapper.UserMapper;
import com.bugjc.ea.server.message.model.User;
import com.bugjc.ea.server.message.service.UserService;
import com.bugjc.ea.server.message.web.io.user.UserLoginBody;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yangqing
 * @date 2019/9/17 17:12
 **/
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public UserLoginBody.ResponseBody login(UserLoginBody.RequestBody requestBody, UserLoginBody.ResponseBody responseBody) {

        //查询用户
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getName, requestBody.getUsername()));

        //验证密码
        if (user.getPwd() != null && !requestBody.getPassword().equals(user.getPwd())){
            //密码验证失败
            responseBody.setFailCode(UserLoginFailCode.WrongPassword.getCode());
            return responseBody;
        }


        //登录成功返回用户信息
        UserLoginBody.ResponseBody.UserInfo userInfo = new UserLoginBody.ResponseBody.UserInfo();
        userInfo.setAge(RandomUtil.randomNumbers(10));
        userInfo.setNickname(RandomUtil.randomString(10));
        responseBody.setUserInfo(userInfo);

        //发布登录成功事件
        UserLoginSuccessfulEvent userLoginSuccessfulEvent = new UserLoginSuccessfulEvent(this,true,user);
        applicationEventPublisher.publishEvent(userLoginSuccessfulEvent);

        return responseBody;
    }
}
