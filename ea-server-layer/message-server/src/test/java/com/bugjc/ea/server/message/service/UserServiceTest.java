package com.bugjc.ea.server.message.service;

import com.bugjc.ea.server.message.Tester;
import com.bugjc.ea.server.message.mapper.UserMapper;
import com.bugjc.ea.server.message.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

@Slf4j

public class UserServiceTest extends Tester {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        log.info(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }
}
