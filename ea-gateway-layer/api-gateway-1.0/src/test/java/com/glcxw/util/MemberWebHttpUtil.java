package com.glcxw.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glcxw.gateway.core.dto.Result;
import com.glcxw.gateway.core.dto.ResultCode;
import com.glcxw.gateway.core.util.StrSortUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 银联二维码http调用工具类
 * @author aoki
 */
@Slf4j
public class MemberWebHttpUtil {

    public static Result post(String type, String url, Map<String, Object> params, String random) throws Exception {

        String content = StrSortUtil.sortString(JSON.toJSONString(params));
        String timestamp = String.valueOf(Long.parseLong(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())));
        String nonce = RandomUtil.randomNumbers(10);
        String sequence = new SequenceUtil().genUnionPaySequence();
        JSONObject app = HttpParamUtil.getAppConfig(type);
        RSA rsa = SecureUtil.rsa(null, app.getString("rsaPublicKey"));
        String randomKey = Base64.encode(rsa.encrypt(random, KeyType.PublicKey));
        log.info("业务参数："+content);

        HttpRequest httpRequest = HttpRequest.post(url)
                .timeout(8000)
                .form(params)
                .header("IsDebug", "true")
                .header("Version", app.getString("version"))
                .header("Appid", app.getString("appId"))
                .header("Sequence",sequence)
                .header("Timestamp", timestamp)
                .header("Nonce", nonce)
                .header("Signature", HttpParamUtil.generateSign(type, content, nonce, timestamp, sequence, randomKey))
                .header("Random",randomKey)
                .header("Token", HttpParamUtil.getToken(random));


        String headers =JSON.toJSONString(httpRequest.headers());
        log.info("请求头："+headers);

        /**执行请求**/
        HttpResponse httpResponse = httpRequest.execute();

        /**获取主要返回结果**/
        String resultJson = httpResponse.body();
        log.info("response result:"+resultJson);
        Result result = JSON.parseObject(resultJson, Result.class);
        if (result.getCode() != ResultCode.SUCCESS.getCode()){
            log.info("业务执行失败,错误描述:{}",result.getMessage());
            return result;
        }

        /**获取response header**/
        String headerSequenceValue = httpResponse.header("sequence");
        if (!sequence.equals(headerSequenceValue)){
            throw new Exception("无效的序列号！");
        }

        /**验签**/
        nonce = httpResponse.header("Nonce");
        String timestampStr = httpResponse.header("Timestamp");
        String headerSignatureValue = httpResponse.header("Signature");
        boolean verify = HttpParamUtil.verifySign(type, resultJson, nonce, timestampStr, headerSignatureValue);
        if (!verify){
            throw new Exception("签名验证失败！");
        }

        /**返回结果**/
        return result;
    }

}
