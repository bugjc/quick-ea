package com.ugiant.job.test.env;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

public class EnvUtil {

    /**
     * 环境类型
     */
    public enum EnvType{
        //开发环境
        DEV,
        //测试环境
        TEST,
        //用户验收环境
        UAT,
        //正式环境
        PROD;
    }


    private static Map<String, Config> configMap = new HashMap<>();
    static {

        //初始化环境配置属性
        Config devConfig = new Config();
        devConfig.setBaseUrl("http://127.0.0.1:8500");
        configMap.put(EnvType.DEV.name(),devConfig);

        Config testConfig = new Config();
        testConfig.setBaseUrl("http://127.0.0.1:8706/");
        configMap.put(EnvType.TEST.name(),testConfig);
        Config uatConfig = new Config();
        uatConfig.setBaseUrl("http://127.0.0.1:8706/");
        configMap.put(EnvType.UAT.name(),uatConfig);

        Config prodConfig = new Config();
        prodConfig.setBaseUrl("http://127.0.0.1:8706/");
        configMap.put(EnvType.PROD.name(),prodConfig);
    }

    /**
     * 获取环境配置属性
     * @param envType
     * @return
     */
    public static Config getConfig(String envType){
        return configMap.get(envType);
    }

    /**
     * 获取服务地址
     * @param envType
     * @return
     */
    public static String getBaseUrl(EnvType envType){
        return getConfig(envType.name()).getBaseUrl();
    }

    @Data
    @NoArgsConstructor
    public static class Config {
        /**
         * url
         */
        private String baseUrl;
    }
}
