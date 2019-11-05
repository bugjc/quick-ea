package com.bugjc.ea.gateway.zuul.util;

import com.bugjc.ea.gateway.zuul.env.EnvUtil;
import com.bugjc.ea.opensdk.http.ApiBuilder;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.model.AppParam;
import com.bugjc.ea.opensdk.http.service.HttpService;
import com.bugjc.ea.opensdk.http.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

@Slf4j
public class HttpUtil {
    /**
     * 获取http服务对象
     * @return
     */
    public static HttpService getHttpService(AppParam appParam) {
        return new ApiBuilder()
                .setAppParam(appParam)
                .setHttpConnTimeout(5000)
                .build();
    }

    /**
     * 获取http 任务调度服务对象
     * @return
     */
    public static JobService getJobService(AppParam appParam) {

        //redis 配置
        String host = "192.168.0.146";
        int port = 6379;
        int timeout = 6000;
        int maxIdle = 60;
        long maxWaitMillis = -1;
        int database = 5;

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        jedisPoolConfig.setBlockWhenExhausted(true);
        // 是否启用pool的jmx管理功能, 默认true
        jedisPoolConfig.setJmxEnabled(true);

        return new ApiBuilder()
                .setAppParam(appParam)
                .setHttpConnTimeout(5000)
                .setJedisPool(new JedisPool(jedisPoolConfig, host, port, timeout,null,database))
                .build().getJobService();
    }

    @Test
    public void test() throws IOException {
        String path = "/test/v1";
        String version = "1.0";
        String body = "{}";
        Result result = getHttpService(EnvUtil.getTestServer()).post(path,version,body);
        log.info(result.toString());
    }


}
