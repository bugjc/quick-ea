package com.bugjc.ea.qrcode.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author qingyang
 * @date 2018/9/5 23:47
 */
@Slf4j
@Data
@Component
public class ApplicationConfig implements ApplicationListener<WebServerInitializedEvent> {

    private String ip;
    private int port;
    private String applicationName;
    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private Environment env;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        //获取实际运行的端口
        this.port = event.getWebServer().getPort();
    }

    /**
     * 获取eureka本地服务实例
     * @return ServiceInstance
     */
    public ServiceInstance getLocalServiceInstance(){

        List<ServiceInstance> list = discoveryClient.getInstances(this.getApplicationName());
        for (ServiceInstance serviceInstance : list) {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(serviceInstance));
            JSONObject instanceInfo = jsonObject.getJSONObject("instanceInfo");
            String instanceId = instanceInfo.getString("instanceId");
            InetAddress inetAddress = new InetUtils(new InetUtilsProperties()).findFirstNonLoopbackAddress();
            String localInstanceId = inetAddress.getHostAddress()+":"+serviceInstance.getHost()+":"+this.getPort();
            if (localInstanceId.equals(instanceId)){
                return serviceInstance;
            }
        }
        return null;
    }

    /**
     * 获取应用名
     * @return application name
     */
    public String getApplicationName(){
        return env.getProperty("spring.application.name");
    }

    /**
     * 获取IP
     * @return ip
     */
    private String getIp(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }
}
