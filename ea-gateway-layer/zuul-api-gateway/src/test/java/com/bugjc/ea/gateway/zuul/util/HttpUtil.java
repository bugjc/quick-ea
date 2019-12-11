package com.bugjc.ea.gateway.zuul.util;

import cn.hutool.core.lang.Singleton;
import com.bugjc.ea.opensdk.http.core.util.HttpClient;
import com.bugjc.ea.opensdk.http.model.AppInternalParam;
import com.bugjc.ea.opensdk.http.model.AppParam;
import com.bugjc.ea.opensdk.http.service.HttpService;
import com.bugjc.ea.opensdk.http.service.JobService;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HttpUtil {
    private HttpUtil(){};
    private JedisPool jedisPool;

    private JedisPool getJedisPool(){
        if (jedisPool == null){
            synchronized (HttpUtil.class){
                if (jedisPool == null){
                    //redis 配置
                    String host = "127.0.0.1";
                    int port = 6379;
                    int timeout = 2000;
                    int maxIdle = 10;
                    //获取连接时的最大等待毫秒数，默认：-1 长时间等待
                    long maxWaitMillis = 2000;
                    int database = 10;

                    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
                    jedisPoolConfig.setMaxIdle(maxIdle);
                    jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
                    // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
                    jedisPoolConfig.setBlockWhenExhausted(true);
                    // 是否启用pool的jmx管理功能, 默认true
                    jedisPoolConfig.setJmxEnabled(true);
                    jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout,null, database);
                }
            }
        }

        return jedisPool;
    }

    /**
     * 获取 http 任务调度服务对象
     * @param appParam          --接入应用调用接口协议所需参数
     * @return
     */
    public static HttpService getHttpService(AppParam appParam) {

        JedisPool jedisPool = Singleton.get(HttpUtil.class).getJedisPool();

        AppInternalParam appInternalParam = new AppInternalParam();
        List<AppInternalParam.EurekaEntity> eurekaEntities = new ArrayList<>();
        eurekaEntities.add(new AppInternalParam.EurekaEntity("eureka.serviceUrl.default=http://eureka:123456@127.0.0.1:8000/eureka/,http://eureka:123456@127.0.0.1:8001/eureka/"));
        appInternalParam.setEurekaEntities(eurekaEntities);
        appInternalParam.setJedisPool(jedisPool);

        return HttpClient.getInstance().getHttpService(appParam, appInternalParam);
    }

    /**
     * 获取 http 任务调度服务对象
     * @param appParam          --接入应用调用接口协议所需参数
     * @return
     */
    public static JobService getJobService(AppParam appParam) {

        JedisPool jedisPool = Singleton.get(HttpUtil.class).getJedisPool();

        AppInternalParam appInternalParam = new AppInternalParam();
        List<AppInternalParam.EurekaEntity> eurekaEntities = new ArrayList<>();
        eurekaEntities.add(new AppInternalParam.EurekaEntity("eureka.serviceUrl.default=http://eureka:123456@127.0.0.1:8000/eureka/,http://eureka:123456@127.0.0.1:8001/eureka/"));
        appInternalParam.setEurekaEntities(eurekaEntities);
        appInternalParam.setJedisPool(jedisPool);

        return HttpClient.getInstance().getJobService(appParam, appInternalParam);
    }
}
