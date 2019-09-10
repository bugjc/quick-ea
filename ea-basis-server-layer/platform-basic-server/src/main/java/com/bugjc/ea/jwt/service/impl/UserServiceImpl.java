package com.bugjc.ea.auth.service.impl;

import com.bugjc.ea.auth.dao.UserDao;
import com.bugjc.ea.auth.model.User;
import com.bugjc.ea.auth.service.UserService;
import com.bugjc.ea.auth.web.reqbody.userregister.UserRegisterRepBody;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public void register(UserRegisterRepBody userRegisterRepBody) {

        //密码哈希编码
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userRegisterRepBody.setPassword(bCryptPasswordEncoder.encode(userRegisterRepBody.getPassword()));

        //创建存储对象并将请求参数属性复制到存储对象中
        User user = new User();
        BeanUtils.copyProperties(userRegisterRepBody,user);

        //插入记录
        userDao.save(user);
    }
}
