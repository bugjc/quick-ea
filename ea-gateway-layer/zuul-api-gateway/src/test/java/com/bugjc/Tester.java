package com.bugjc;

import com.bugjc.ea.gateway.zuul.ApiGatewayServerApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 单元测试继承该类即可
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiGatewayServerApplication.class)
@Rollback
public abstract class Tester {}



