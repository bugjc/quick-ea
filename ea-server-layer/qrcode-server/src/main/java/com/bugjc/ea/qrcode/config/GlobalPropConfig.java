package com.bugjc.ea.qrcode.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 全局配置
 * @author aoki
 */
@Data
@Configuration
public class GlobalPropConfig {

    /**  版本 **/
    @Value("${member.server.address}")
    public String memberServerAddress;


    public String getMemberServerAddress(String content){
        return memberServerAddress+content;
    }

}
