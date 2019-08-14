package com.glcxw.env;

import java.util.HashMap;
import java.util.Map;

public class EnvUtil {

    /**
     * 环境类型
     */
    enum EnvType{
        //开发环境
        DEV,
        //测试环境
        TEST,
        //用户验收环境
        UAT,
        //正式环境
        PROD;
    }


    private static Map<String,String> servers = new HashMap<>();
    static {
        servers.put(EnvType.DEV.name(),"http://127.0.0.1:7900");
        servers.put(EnvType.TEST.name(),"http://192.168.8.17:31381");
        servers.put(EnvType.UAT.name(),"http://127.0.0.1:7900");
        servers.put(EnvType.PROD.name(),"http://127.0.0.1:7900");
    }

    /**
     * 获取环境对应的URL
     * @param envType
     * @return
     */
    public static String getServer(String envType){
        return servers.get(envType);
    }

    public static String getDevServer(){
        return servers.get(EnvType.DEV.name());
    }

    public static String getTestServer(){
        return servers.get(EnvType.TEST.name());
    }

    public static String getUatServer(){
        return servers.get(EnvType.UAT.name());
    }

    public static String getProdServer(){
        return servers.get(EnvType.PROD.name());
    }
}
