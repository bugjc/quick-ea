package com.bugjc.ea.server.message.web;

import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.server.message.web.io.user.UserLoginBody;
import com.bugjc.ea.server.message.biz.UserLoginBiz;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户服务接口
 * @Author aoki
 * @Date 2019/8/5 10:01
 **/
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserLoginBiz userLoginBiz;


    /**
     * 用户登录接口
     * @param requestBody
     * @return
     */
    @PostMapping("login")
    public Result list(@Validated @RequestBody UserLoginBody.RequestBody requestBody){
        return userLoginBiz.login(requestBody);
    }
}