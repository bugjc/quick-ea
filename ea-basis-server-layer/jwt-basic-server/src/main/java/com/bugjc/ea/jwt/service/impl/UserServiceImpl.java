package com.bugjc.ea.jwt.service.impl;

import com.bugjc.ea.jwt.dao.UserDao;
import com.bugjc.ea.jwt.model.User;
import com.bugjc.ea.jwt.service.UserService;
import com.bugjc.ea.jwt.web.reqbody.userregister.UserRegisterRepBody;
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
        userRegisterRepBody.getUser().setPassword(bCryptPasswordEncoder.encode(userRegisterRepBody.getUser().getPassword()));

        //创建存储对象并将请求参数属性复制到存储对象中
        User user = new User();
        BeanUtils.copyProperties(userRegisterRepBody,user);

        //插入记录
        userDao.save(user);
    }
}
