package com.bugjc.ea.testb.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;

/**
 * @Auther: qingyang
 * @Date: 2018/7/26 10:20
 * @Description:
 */
public class FastjsonConfiguration {

    @Bean
    public HttpMessageConverters fastjsonConverter() {

        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        //格式化输出
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero);

        FastJsonHttpMessageConverter fastjson = new FastJsonHttpMessageConverter();
        fastjson.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastjson);
    }
}
