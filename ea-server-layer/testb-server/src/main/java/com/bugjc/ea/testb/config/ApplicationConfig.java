package com.bugjc.ea.testb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 应用常量
 * @author qingyang
 */
@Configuration
public class ApplicationConfig {

    @Value("${spring.application.name}")
    public String applicationName;

}
