package com.glcxw.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 3类卡号混淆工具类
 */
@Slf4j
public class SubAccNoObfuscationUtil {

    /**
     * 混淆ID
     * @param encryptValue
     * @return
     */
    public static JSONObject obfuscate(String encryptValue){
        String key = encryptValue.substring(encryptValue.length() - 4);
        IDObfuscation ob = new IDObfuscation(Long.parseLong(key));
        String obfuscatedId = ob.obfuscate(Long.parseLong(encryptValue));
        JSONObject data = new JSONObject();
        data.put("cardNo", key);
        data.put("subAccNo", obfuscatedId);
        ob = null;
        return data;
    }

    /**
     * 混淆ID
     *
     * @param encryptValue
     * @return
     */
    public static String obfuscate(String key, String encryptValue) {
        return new IDObfuscation(Long.parseLong(key)).obfuscate(Long.parseLong(encryptValue));
    }

    /**
     * 恢复ID
     * @param key
     * @param value
     * @return
     */
    public static String restore(String key, String value){
        IDObfuscation ob = new IDObfuscation(Long.parseLong(key));
        long obfuscatedId = ob.restore(value);
        return String.valueOf(obfuscatedId);
    }

}
