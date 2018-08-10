package com.bugjc.ea.qrcode.core.dto;

import com.bugjc.ea.qrcode.core.sdk.qrcode.SdkUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应结果生成工具
 * @author : aoki
 */
public class UnionPayResultGenerator {

    public static String genSuccessResult(String reqType,String version) {
        Map<String,String> respMap = new HashMap<String,String>();
        respMap.put("reqType", reqType);
        respMap.put("version", version);
        respMap.put("respCode", UnionPayResultCode.SUCCESS.getCode());
        respMap.put("respMsg", UnionPayResultCode.SUCCESS.getMessage());
        return SdkUtil.getRequestParamString(respMap);
    }

    public static String genSignFailResult(String reqType,String version) {
        Map<String,String> respMap = new HashMap<String,String>();
        respMap.put("reqType", reqType);
        respMap.put("version", version);
        respMap.put("respCode", UnionPayResultCode.FAIL_11.getCode());
        respMap.put("respMsg", UnionPayResultCode.FAIL_11.getMessage());
        return SdkUtil.getRequestParamString(respMap);
    }

    public static String genBusinessFailResult(String reqType,String version) {
        Map<String,String> respMap = new HashMap<String,String>();
        respMap.put("reqType", reqType);
        respMap.put("version", version);
        respMap.put("respCode", UnionPayResultCode.ERROR.getCode());
        respMap.put("respMsg", UnionPayResultCode.ERROR.getMessage());
        return SdkUtil.getRequestParamString(respMap);
    }

    public static String genFailResult(String reqType,String version,String respCode,String respMsg) {
        Map<String,String> respMap = new HashMap<String,String>();
        respMap.put("reqType", reqType);
        respMap.put("version", version);
        respMap.put("respCode", respCode);
        respMap.put("respMsg", respMsg);
        return SdkUtil.getRequestParamString(respMap);
    }
}
