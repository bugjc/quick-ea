package com.bugjc.ea.gateway.zuul.env;

import com.bugjc.ea.opensdk.http.model.AppParam;

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

    private static Map<String,AppParam> servers = new HashMap<>();
    static {
        String devEnv = "http://127.0.0.1:7900";
        String testEnv = "http://192.168.8.17:31381";
        String uatEnv = "http://127.0.0.1:7900";
        String prodEnv = "http://127.0.0.1:7900";

        AppParam devAppParam = new AppParam();
        devAppParam.setBaseUrl(devEnv);
        devAppParam.setAppId("USER621030659107983360");
        devAppParam.setAppSecret("622642168597960131661519");
        devAppParam.setRsaPrivateKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCp8keCQHWlfHaYufhKBCWF6/tTv0xEUAYAKxyVGhUBPcr6cVapfwMny7j2HUwcs7gI0SMdOt+61YlgLWstnmpnTINPpi3VUGVJM2tVs04NTYURTg7dTcuTWu0fPuFqgeFB4mAxV82M/sjpi3oDFnFzJr26ceB+MYYNEAD1TYIsce5kdWT+zZg4+qJOfTbyZS5YSG9guXpKm098mp1H8KNd4kY+PXFVaU0mK2hKBpYoqa9WLLnVULQTwlKCjbTPJ07OjFNYOQIzHZ9ffEN7TR8mhNu30PvhUGUJuqYCpugDCk3gIRbsAAHETUzdUtgH+/ex7y3siIPbicQzBlHLuy1XAgMBAAECggEAJSLlT2gojSX6gHs672mijXY4sF9AGjlmZkkFcOuHJYNMRJfZuSYhGWQiId3bEPaQw5R32N5Rn3EkGNkz738Lf1Ev7IrCvzdXOry6urp41t1Ws6ZcBy4Fy0h2QnqqJW0GPTBrlry6FRJj8uzgl6Yi2mff08sopErMITSbEP+D75QF+PK35fx3+jKVzUuLZHWFVRo2A5CjVNxAP6BiNXVzFl1onsDoYXCha4ddHMZSEiqPU0XdJfNTWakJKC5j+HSqwdOzG0zb94nMenBGVxwl+5fbhhJV9XtjJBUNgwSSM2mqcqYrbKZVe6BrOiIrLB48gUE9PHIRtFNIlJr/PIJo8QKBgQDtXyukJfw03YOrZDmjMmTRx1d+9WYaEfK8JEetoHr/iA0JMVeozqAMyjywL7U5pW7SgAlV7bdpAxqV+PbLOcMF/zCeR0Jwp0tFBPuIKCJP3lZQUrdlvq/Io9Qbznt4b0/hzw1hVvEfbjeUxR8sg51sImgND5DXRY4fkvirT2E7iQKBgQC3SIZRxY8ffRjUj1LG9tiXcCWrufMrfx1bRUry1r9L7Nkiu/bVn4/WMQ1dYWJq7gJLb/TeqVQpdW6uFwNvGdu1ijljCLSDM+BwVinXWOodh3cVJcAeB9xuJRRKmarQsmM+ftwBwSTwx9k2RTAyVOI+RkuKFgWPnrTkY+0wzMyJ3wKBgGH8EDrBR1vXONwOElDQo5eI7xBEYnJOiGGrpD8C6OMEmUT7LlFLgfvlm6mq9+ck6BtSqQfkifp6QoY38EAxxtR54+riRYXVORfzl5U74/YONJkRnA0O6ucq4Yrc8FDluhbfbomI5x3vVFhACr9IUTpq9YK2szZ5ytIzzByDNwLpAoGBAI8LlnGJm+t6if+ToIpSq7Z9u5jsdwISoonSae1fd0u9nbZDNyGA+BS2kfS4hcRo3/eaeGWziFEXKvvKUE7MwrJMH//QEnp36YojZWKCLa5ARMuHfAq0HfEFFXInvq5FG7nx7qmb4cXeZAO8OiJ+J1ltKZWrHEn8FrhN2RgGLvjHAoGAQFy7OBfNx9iybd1nUHZN66aTfUlrnVTHkZKKWXqixi9Kc/YJcfXewzYr8hRyBy3PN5YvjX66sZb9s8bgdfgxLvJ7dZ4Z3OPaQHQzdjgvDHveDsAwbJSPeBJa4C3Yo4UX6zarmkH04+9WbT0Rh3IJ63R//8/eiF+7mZuw16YtPak=");
        devAppParam.setRsaPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqfJHgkB1pXx2mLn4SgQlhev7U79MRFAGACsclRoVAT3K+nFWqX8DJ8u49h1MHLO4CNEjHTrfutWJYC1rLZ5qZ0yDT6Yt1VBlSTNrVbNODU2FEU4O3U3Lk1rtHz7haoHhQeJgMVfNjP7I6Yt6AxZxcya9unHgfjGGDRAA9U2CLHHuZHVk/s2YOPqiTn028mUuWEhvYLl6SptPfJqdR/CjXeJGPj1xVWlNJitoSgaWKKmvViy51VC0E8JSgo20zydOzoxTWDkCMx2fX3xDe00fJoTbt9D74VBlCbqmAqboAwpN4CEW7AABxE1M3VLYB/v3se8t7IiD24nEMwZRy7stVwIDAQAB");
        servers.put(EnvType.DEV.name(),devAppParam);

        AppParam testAppParam = new AppParam();
        testAppParam.setBaseUrl(testEnv);
        testAppParam.setAppId("USER621030659107983360");
        testAppParam.setAppSecret("622642168597960131661519");
        testAppParam.setRsaPrivateKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCp8keCQHWlfHaYufhKBCWF6/tTv0xEUAYAKxyVGhUBPcr6cVapfwMny7j2HUwcs7gI0SMdOt+61YlgLWstnmpnTINPpi3VUGVJM2tVs04NTYURTg7dTcuTWu0fPuFqgeFB4mAxV82M/sjpi3oDFnFzJr26ceB+MYYNEAD1TYIsce5kdWT+zZg4+qJOfTbyZS5YSG9guXpKm098mp1H8KNd4kY+PXFVaU0mK2hKBpYoqa9WLLnVULQTwlKCjbTPJ07OjFNYOQIzHZ9ffEN7TR8mhNu30PvhUGUJuqYCpugDCk3gIRbsAAHETUzdUtgH+/ex7y3siIPbicQzBlHLuy1XAgMBAAECggEAJSLlT2gojSX6gHs672mijXY4sF9AGjlmZkkFcOuHJYNMRJfZuSYhGWQiId3bEPaQw5R32N5Rn3EkGNkz738Lf1Ev7IrCvzdXOry6urp41t1Ws6ZcBy4Fy0h2QnqqJW0GPTBrlry6FRJj8uzgl6Yi2mff08sopErMITSbEP+D75QF+PK35fx3+jKVzUuLZHWFVRo2A5CjVNxAP6BiNXVzFl1onsDoYXCha4ddHMZSEiqPU0XdJfNTWakJKC5j+HSqwdOzG0zb94nMenBGVxwl+5fbhhJV9XtjJBUNgwSSM2mqcqYrbKZVe6BrOiIrLB48gUE9PHIRtFNIlJr/PIJo8QKBgQDtXyukJfw03YOrZDmjMmTRx1d+9WYaEfK8JEetoHr/iA0JMVeozqAMyjywL7U5pW7SgAlV7bdpAxqV+PbLOcMF/zCeR0Jwp0tFBPuIKCJP3lZQUrdlvq/Io9Qbznt4b0/hzw1hVvEfbjeUxR8sg51sImgND5DXRY4fkvirT2E7iQKBgQC3SIZRxY8ffRjUj1LG9tiXcCWrufMrfx1bRUry1r9L7Nkiu/bVn4/WMQ1dYWJq7gJLb/TeqVQpdW6uFwNvGdu1ijljCLSDM+BwVinXWOodh3cVJcAeB9xuJRRKmarQsmM+ftwBwSTwx9k2RTAyVOI+RkuKFgWPnrTkY+0wzMyJ3wKBgGH8EDrBR1vXONwOElDQo5eI7xBEYnJOiGGrpD8C6OMEmUT7LlFLgfvlm6mq9+ck6BtSqQfkifp6QoY38EAxxtR54+riRYXVORfzl5U74/YONJkRnA0O6ucq4Yrc8FDluhbfbomI5x3vVFhACr9IUTpq9YK2szZ5ytIzzByDNwLpAoGBAI8LlnGJm+t6if+ToIpSq7Z9u5jsdwISoonSae1fd0u9nbZDNyGA+BS2kfS4hcRo3/eaeGWziFEXKvvKUE7MwrJMH//QEnp36YojZWKCLa5ARMuHfAq0HfEFFXInvq5FG7nx7qmb4cXeZAO8OiJ+J1ltKZWrHEn8FrhN2RgGLvjHAoGAQFy7OBfNx9iybd1nUHZN66aTfUlrnVTHkZKKWXqixi9Kc/YJcfXewzYr8hRyBy3PN5YvjX66sZb9s8bgdfgxLvJ7dZ4Z3OPaQHQzdjgvDHveDsAwbJSPeBJa4C3Yo4UX6zarmkH04+9WbT0Rh3IJ63R//8/eiF+7mZuw16YtPak=");
        testAppParam.setRsaPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqfJHgkB1pXx2mLn4SgQlhev7U79MRFAGACsclRoVAT3K+nFWqX8DJ8u49h1MHLO4CNEjHTrfutWJYC1rLZ5qZ0yDT6Yt1VBlSTNrVbNODU2FEU4O3U3Lk1rtHz7haoHhQeJgMVfNjP7I6Yt6AxZxcya9unHgfjGGDRAA9U2CLHHuZHVk/s2YOPqiTn028mUuWEhvYLl6SptPfJqdR/CjXeJGPj1xVWlNJitoSgaWKKmvViy51VC0E8JSgo20zydOzoxTWDkCMx2fX3xDe00fJoTbt9D74VBlCbqmAqboAwpN4CEW7AABxE1M3VLYB/v3se8t7IiD24nEMwZRy7stVwIDAQAB");
        servers.put(EnvType.TEST.name(),testAppParam);
        servers.put(EnvType.UAT.name(),testAppParam);
        servers.put(EnvType.PROD.name(),testAppParam);

    }

    /**
     * 获取环境对应的URL
     * @param envType
     * @return
     */
    public static AppParam getServer(EnvType envType){
        return servers.get(envType.name());
    }

    public static AppParam getDevServer(){
        return servers.get(EnvType.DEV.name());
    }

    public static AppParam getTestServer(){
        return servers.get(EnvType.TEST.name());
    }

    public static AppParam getUatServer(){
        return servers.get(EnvType.UAT.name());
    }

    public static AppParam getProdServer(){
        return servers.get(EnvType.PROD.name());
    }
}
