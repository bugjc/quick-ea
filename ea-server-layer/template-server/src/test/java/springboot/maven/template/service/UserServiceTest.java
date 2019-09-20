package springboot.maven.template.service;

import springboot.maven.template.Tester;
import springboot.maven.template.mapper.UserMapper;
import springboot.maven.template.model.User;
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
