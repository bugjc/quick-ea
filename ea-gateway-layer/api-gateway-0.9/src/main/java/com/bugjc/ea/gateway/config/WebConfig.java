package com.bugjc.ea.gateway.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author qingyang
 * @date 2018/8/4 17:07
 */
@Configuration
public class WebConfig {

    @Value("${server.port}")
    private int serverPort;

    @Value("${server.http.port}")
    private int serverHttpPort;

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
        bean.setOrder(-10);
        return bean;
    }

// http重定向到https
//    @Bean
//    public TomcatServletWebServerFactory tomcatServletWebServerFactory(){
//
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory(){
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint securityConstraint=new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection=new SecurityCollection();
//                //collection.addMethod("post");
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//            }
//
//        };
//
//        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
//        return tomcat;
//
//    }
//
//    private Connector initiateHttpConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        //需要重定向的http端口
//        connector.setPort(serverHttpPort);
//        connector.setSecure(false);
//        //设置重定向到https端口
//        connector.setRedirectPort(serverPort);
//        return connector;
//    }

}
