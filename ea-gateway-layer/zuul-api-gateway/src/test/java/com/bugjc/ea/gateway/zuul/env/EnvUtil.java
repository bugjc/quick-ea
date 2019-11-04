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
        devAppParam.setAppId("JOB0640875180528373760");
        devAppParam.setAppSecret("634740256807569915329426");
        devAppParam.setRsaPrivateKey("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCSoqCF84TUxb3Ec+U9/jaHALZguArU29gqAFbcP33KzX9jEaFU/WBC8mOwBTlY+bwbIE704HL2B1iTc0ZtoyKsiAA9/UHjRmHZhyuuLtfR2tZ8m0YDUOXLKA46gibU6hhJCdOhtqWmT3ovokLb1eMkqJvFDg4bkX+9MOq3S1eBlTUiRINZJzIfHILP7wto6zG/Vw8wnPzCT/axH501/XUetBu3g0+wp2g9CxFl0fvArGypOyh7aM58iOVfiyM7ZEhtN5SMce4gR9AN8nyxgFLPWgKm/WTRFBhET7nVfJAqbsdYfD0RJ2T+nvHt7mNB2DJUietEaWoZKAFpxZgZrOpbAgMBAAECggEAdnIbDFiasWxRT4D2ac8QqJhwuUuwpCQ5RiQkBaDbsZexEItLMGXC/hDhaWuu2SpRQfAlFzS+Z/GjIOf6udEOzyLEX05VjIpru9St+fMorgEBRIIAWelc1W+znptHVrEJkOX5JyVsRRzp69Q8AWuSISh5OwGRb79l2aLL1edr9eUhZ67KqhrPCXWfVyaTeLIz0kKR3qliwFvxoGnTKgbW+ANM5C23CUlKhA/Ekqx54Hv3mbhzWA0XaRyqmLhrHKmv+h0Y1MdjvCVUdSzRQ7sJYU0zGDrZkwXZCDDVVyj4L6jRQuqaJsGXcMvQdObmJbdUMZ1n0LonoaWuh96Di/W0gQKBgQDIdG3f32nJd3jIcAIx8pZOFzKtk8jKI4BEhmCan6OiU0jkJpSWhCNm8ZmY4D/4GovIqGKmzKN+QQTDDy4G69X1KcYodzTmZV/UBojiL0NgbWCe8WbM4Ue6svY2d1tuH4sXnQilwOpDq0Y5PQ+wHVRqxx2/9ot9x85iC1NmoynjIQKBgQC7RGz7vC1h00jjO96ZnP2tWRfu5oB0EeatDIZWBpTG+L+s8b5DJTi7MomKYhvfM4w37HUibH0Rh6k3eoIOqKXQj6kUBdcKK2vnR95AbwJhHL3M31TgFBbbSjcLpsqVjGrh8HP7gW4lNjMfHFNV1jwQePCYwhAPNtVo3wG+R80Z+wKBgDveumphGgF4U3TaL22nnMjaGOLK/u+QxuCwme3UvFThNyYdd8e007YNdui4IaMCmrllwYE8IaC13qw7vm2GxrKgtXSklgIySPrUsNGNeDw2ruMKCsAqkPfsN6lw4Lf1Z9+3oTuQcOehIio6WJMrT2lM60/IXUVGBhg2dcRz+zDBAoGATCCq9UMwZ75OuSGTgnsRnTDEy/ekbIsCbYLT8ZDHVoO02AeGQyGUiHUo+LqdfgUkkW9sxsu3pDK0FbuVt4PTtI9SOrONEbNJMEJFFYiLPRNEDU8U3+sKx6bJ7dCmNllY8fSgEbQyJKp96b3IGvL2g/PGSzNI+AZghztxJ/tkY5UCgYBHwX8H8T5Vkslj7Tzc1PWaNNbaD4tyiypvRwPcaA+B0/k0ufuLdqwvegV5A03AvgEzlRAYn4NosU+YsR6zD5VC1jto1IAhkHK0H18goCyK6Nn2TfJY44f4N1j4ru5avnyLpMsAN4/p44sG1Q28UYC9jLFmHLRFgq9OwsGbElTMDw==");
        devAppParam.setRsaPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkqKghfOE1MW9xHPlPf42hwC2YLgK1NvYKgBW3D99ys1/YxGhVP1gQvJjsAU5WPm8GyBO9OBy9gdYk3NGbaMirIgAPf1B40Zh2Ycrri7X0drWfJtGA1DlyygOOoIm1OoYSQnTobalpk96L6JC29XjJKibxQ4OG5F/vTDqt0tXgZU1IkSDWScyHxyCz+8LaOsxv1cPMJz8wk/2sR+dNf11HrQbt4NPsKdoPQsRZdH7wKxsqTsoe2jOfIjlX4sjO2RIbTeUjHHuIEfQDfJ8sYBSz1oCpv1k0RQYRE+51XyQKm7HWHw9ESdk/p7x7e5jQdgyVInrRGlqGSgBacWYGazqWwIDAQAB");
        servers.put(EnvType.DEV.name(),devAppParam);

//        servers.put(EnvType.TEST.name(),);
//        servers.put(EnvType.UAT.name(),);
//        servers.put(EnvType.PROD.name(),);

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
