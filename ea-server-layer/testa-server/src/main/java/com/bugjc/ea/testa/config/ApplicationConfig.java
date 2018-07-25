package com.bugjc.ea.testa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: qingyang
 * @Date: 2018/7/23 09:45
 * @Description:
 */
@Configuration
public class ApplicationConfig{

    @Value("${spring.application.name}")
    public String applicationName;

}
