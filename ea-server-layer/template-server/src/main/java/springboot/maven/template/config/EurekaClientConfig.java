package springboot.maven.template.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
public class EurekaClientConfig implements ApplicationListener<WebServerInitializedEvent> {

    private int port;
    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private LoadBalancerClient loadBalancerClient;
    @Resource
    private Environment env;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        //获取实际运行的端口
        this.port = event.getWebServer().getPort();
    }

    /**
     * 获取 eureka 本地服务实例
     * @return ServiceInstance
     */
    public ServiceInstance getLocalServiceInstance(){

        List<ServiceInstance> list = discoveryClient.getInstances(this.getApplicationName());
        for (ServiceInstance serviceInstance : list) {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(serviceInstance));
            JSONObject instanceInfo = jsonObject.getJSONObject("instanceInfo");
            String instanceId = instanceInfo.getString("instanceId");
            try {
                InetAddress netAddress = InetAddress.getLocalHost();
                String localInstanceId = netAddress.getHostName() +":"+serviceInstance.getHost()+":"+this.getPort();
                if (localInstanceId.equals(instanceId)){
                    return serviceInstance;
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    /**
     * 获取 eureka 服务实例
     * @param serverName
     * @return
     */
    public ServiceInstance getLoadBalanceServiceInstance(String serverName){
        //轮询访问策略
        return loadBalancerClient.choose(serverName);
    }

    /**
     * 获取应用名
     * @return application name
     */
    public String getApplicationName(){
        return env.getProperty("spring.application.name");
    }
}
