package com.bugjc.ea.template.api;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.http.opensdk.core.dto.Result;
import com.bugjc.ea.template.env.EnvUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 业务接口调用工具
 * @author qingyang
 * @date 2019/6/20 14:48
 */
@Slf4j
public class ApiClient {

    /**
     * 环境配置属性
     */
    private EnvUtil.Config config;

    public ApiClient(EnvUtil.EnvType envType){
        config = EnvUtil.getConfig(envType.name());
    }

    /**
     * 接口路径
     */
    public class ContentPath{

        /**
         * 地图列表
         */
        public static final String USER_LOGIN_PATH = "/user/login";
    }

    /**
     * 调用业务接口
     * @param path
     * @param bodyData
     * @return
     */
    public Result doPost(String path, String bodyData){
        //调用接口
        String url = config.getBaseUrl().concat(path);
        log.info("URL{}",url);
        HttpRequest httpRequest = HttpUtil.createPost(url);
        httpRequest.contentType("application/json;charset=utf-8");
        String result = httpRequest.timeout(8000).body(bodyData).execute().body();
        return JSON.parseObject(result,Result.class);
    }

}
