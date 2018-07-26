package com.bugjc.ea.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalProperty {

    @Value("${rsa.public-key}")
    public String rsaPublicKey;
    @Value("${rsa.private-key}")
    public String rsaPrivateKey;

}
