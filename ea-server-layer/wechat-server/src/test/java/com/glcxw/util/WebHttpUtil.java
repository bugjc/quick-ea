package com.glcxw.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.glcxw.wechat.core.dto.Result;
import com.glcxw.wechat.core.dto.ResultCode;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 银联二维码http调用工具类
 * @author aoki
 */
@Slf4j
public class WebHttpUtil {

    private final static String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB";
    private final static String RSA_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMiAec6fsssguUoRN3oEVEnQaqBLZjeafXAxCbKH3MTJaXPmnXOtqFFqFtcB8J9KqyFI1+o6YBDNIdFWMKqOwDDWPKqtdo90oGav3QMikjGYjIpe/gYYCQ/In/oVMVj326GmKrSpp0P+5LNCx59ajRpO8//rnOLd6h/tNxnfahanAgMBAAECgYEAusouMFfJGsIWvLEDbPIhkE7RNxpnVP/hQqb8sM0v2EkHrAk5wG4VNBvQwWe2QsAuY6jYNgdCPgTNL5fLaOnqkyy8IobrddtT/t3vDX96NNjHP4xfhnMbpGjkKZuljWKduK2FAh83eegrSH48TuWS87LjeZNHhr5x4C0KHeBTYekCQQD5cyrFuKua6GNG0dTj5gA67R9jcmtcDWgSsuIXS0lzUeGxZC4y/y/76l6S7jBYuGkz/x2mJaZ/b3MxxcGQ01YNAkEAzcRGLTXgTMg33UOR13oqXiV9cQbraHR/aPmS8kZxkJNYows3K3umNVjLhFGusstmLIY2pIpPNUOho1YYatPGgwJBANq8vnj64p/Hv6ZOQZxGB1WksK2Hm9TwfJ5I9jDu982Ds6DV9B0L4IvKjHvTGdnye234+4rB4SpGFIFEo+PXLdECQBiOPMW2cT8YgboxDx2E4bt8g9zSM5Oym2Xeqs+o4nKbcu96LipNRkeFgjwXN1708QuNNMYsD0nO+WIxqxZMkZsCQHtS+Jj/LCnQZgLKxXZAllxqSTlBln2YnBgk6HqHLp8Eknx2rUXhoxE1vD9tNmom6PiaZlQyukrQkp5GOMWDMkU=";
    private final static String APP_ID = "glu4s47ces867m2pjhrp1umealz46j";
    private final static String VERSION = "1.0";

    public static Result post(String url, Map<String, Object> paramMap) throws Exception {

        long timestamp = Long.parseLong(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
        String nonce = RandomUtil.randomNumbers(10);
        String sequence = new SequenceUtil().genUnionPaySequence();
        /**序列化参数**/
        String content = JSON.toJSONString(paramMap);
        log.info("业务参数："+content);
        RSA rsa = SecureUtil.rsa(null,RSA_PUBLIC_KEY);
        String randomKey = Base64.encode(rsa.encrypt(RandomUtil.randomNumbers(24), KeyType.PublicKey));
        String requestSign = "appid="+APP_ID+"&message="+content+"&nonce="+nonce+"&timestamp="+timestamp+"&Sequence="+sequence+"&Random="+ randomKey;
        log.info("签名字符串："+requestSign);
        /**生成签名**/
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA,RSA_PRIVATE_KEY,null);
        byte[] signed = sign.sign(requestSign.getBytes(CharsetUtil.CHARSET_UTF_8));
        log.info("签名："+Base64.encode(signed));
        log.info("签名："+SHA1WithRSAUtil.sign(requestSign,RSA_PRIVATE_KEY,"UTF-8"));

        HttpRequest httpRequest = HttpRequest.post(url)
                .timeout(8000)
                .body(content,"application/json;charset=UTF-8")
                .header("Version",VERSION)
                .header("AppId",APP_ID)
                .header("Sequence",sequence)
                .header("Timestamp",""+timestamp)
                .header("Nonce", nonce)
                .header("Signature", Base64.encode(signed));


        String headers =JSON.toJSONString(httpRequest.headers());
        log.info("请求头："+headers);

        /**执行请求**/
        HttpResponse httpResponse = httpRequest.execute();

        /**获取主要返回结果**/
        String resultJson = httpResponse.body();
        log.info("response result:"+resultJson);
        Result result = JSON.parseObject(resultJson, Result.class);
        if (result.getCode() != ResultCode.SUCCESS.code){
            return result;
        }

        /**获取response header**/
        String headerSequenceValue = httpResponse.header("Sequence");
        if (!sequence.equals(headerSequenceValue)) {
            throw new Exception("无效的序列号！");
        }

        /**验签**/
        nonce = httpResponse.header("Nonce");
        String timestampStr = httpResponse.header("Timestamp");
        String responseSign = "appid="+APP_ID+"&message="+resultJson+"&nonce="+nonce+"&timestamp="+timestampStr;
        String headerSignatureValue = httpResponse.header("Signature");
        sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA,null,RSA_PUBLIC_KEY);
        boolean verify = sign.verify(responseSign.getBytes(CharsetUtil.CHARSET_UTF_8), Base64.decode(headerSignatureValue));
        if (!verify){
            throw new Exception("签名验证失败！");
        }

        /**返回结果**/
        return result;
    }


    public static String getUnionPayUserId(String userId) {
        int length = 16;
        if (userId.length() < length){
            int zeroFillNnm = length - userId.length();
            String zero = "";
            for (int i = 0; i < zeroFillNnm; i++) {
                zero += "0";
            }
            userId = zero + userId;
        }
        return userId;
    }

    /**
     * UserID奇数补0
     * @param userId
     * @return
     */
//    public String getUnionPayUserId(String userId){
//        if (userId.length() % 2 == 0){
//        }else {
//            userId = 0 + userId;
//        }
//        return userId;
//    }

}
