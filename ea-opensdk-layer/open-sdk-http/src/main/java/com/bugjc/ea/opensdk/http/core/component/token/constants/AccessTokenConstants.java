package com.bugjc.ea.opensdk.http.core.component.token.constants;

/**
 * 授权认证 token 常量类
 * @author aoki
 * @date 2019/11/5
 * **/
public class AccessTokenConstants {

    /**
     * 过期时间：7200 秒
     */
    public final static int EXPIRE_DATE = 7200;


    private static final String ACCESS_TOKEN_KEY = "AccessToken:";

    public static String getKey(String key) {
        return ACCESS_TOKEN_KEY + key;
    }

}
