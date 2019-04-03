package com.glcxw.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class SignatureVerifyUtil {

    private static final String RSA_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6Fhmu1UVTDsvBP56Qld4b5k0NWlkZ/QyA13K+NKKRCfjk4auWuG7LXzjVCcDmh4dAXbyCtPmtQmHap4Qe75NLg8tEQpGGeAGxGZH1Oho1OycNEJXNpNdGMl1fuVbjfDmvsR2uOKAVlCednikWXrntC/FR4XkK/oCVnh9UfMHcLHkPSEF8Pykvr6vi8aqy3rGl9iGPDZx7r93E0JVhp7dYiy83Qm6KgN/ICYPPWjAC2vtYW2/KqpLNe2388tzqFboFSI2Qh5jZuAl/55x78fhYVz99ndg2YHOTU9zfhSri4hmYIiR43ur6gaDVok1PPuqmx3Q5zr6kema/ijI4ZW8gQIDAQAB";
    private static final String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDoWGa7VRVMOy8E/npCV3hvmTQ1aWRn9DIDXcr40opEJ+OThq5a4bstfONUJwOaHh0BdvIK0+a1CYdqnhB7vk0uDy0RCkYZ4AbEZkfU6GjU7Jw0Qlc2k10YyXV+5VuN8Oa+xHa44oBWUJ52eKRZeue0L8VHheQr+gJWeH1R8wdwseQ9IQXw/KS+vq+LxqrLesaX2IY8NnHuv3cTQlWGnt1iLLzdCboqA38gJg89aMALa+1hbb8qqks17bfzy3OoVugVIjZCHmNm4CX/nnHvx+FhXP32d2DZgc5NT3N+FKuLiGZgiJHje6vqBoNWiTU8+6qbHdDnOvqR6Zr+KMjhlbyBAgMBAAECggEAF7bWHW6sstAjl23lypmk6DcJ1h3Aqr+Iz0Emc0kB5bzwhGEtsIbzlMv3Y+JrgrJqIXkjcSl5laa4VJB3fDnLJNVVszSKPR/ZcPhDgp6J/Jl9w/O1dROLX/lH7cC5HH6m/neM6ofrs2OdGNhtZdieh4yMpSUsSbTFwk+f8tpMOxsrLioqlMYYVFBKZx1HjXYxWOvbDa/C/dYABs3m/b7YGIjslcG+8b0J9QOez7lLED+xScbAX2CoYE7CnqLRGO95b0W9IOmfY0f9XqJAcH6k4A/NyR8OHLpqxnAtsLmZozhx/iOdTR88XcB3LXeCxcZdnpGtaVVjwncK4UHBjtPr1QKBgQD96aUyl0OOXKjw3RhiwYl4OCZ5K+8TQrewcXF6ubccMBxBE/BHe2XIabReR1kw5DJROVU/4m5wspnzFy4KJyGykXc/+Qxr5lhToTcKBDFW2lSBC5V3PCV1eiHJ85TVfkWJpd5doKx0CcYqHOl0bOQ9h3AH6xS01/k3lUg4u4apywKBgQDqQV4st6eESLmsfNzyjLBT9QY2L1zQeCwjraUAx421DITdC9766T/EpD4vIU3MGXTfWAx5YiSEZx2eADDiL2YuycZtBJK9aL/TpWmfdlu90qulfQxsb5sOH7lF2MEJko8D0hBfettohRgqdaVogkeYXWEDO/TIi3wRML8VFR/ZYwKBgQDMZwib33V8508BB5uX9KFL7Dfcl+eQI8dn/QsTdunXgUtdgEWP5eCcI6wVsW41k56GEJd2JDwv8TdJoEFp3vKbo8rR7xV2zyoPuNpr79AgUQZyE/cw03R8S+NjhfksnMgx9Rs7WBs8Fpo2UKwHE5nxhlRDq5UxuCNiAYTwVtYjXQKBgEdm9VWcBSuFH1M8G/yydWyh2urZNOk2N/4IB6tdKAxvbHAUFjtUrWM6zNmAxT3Y5I7PP74f9BGHfLeUR1IqMHNCfYDtS+z/6LiSSseYRA5wEHG40/iZbCG5+bcYgn5wdbcsaGEyCX6qUtteULSyQTBoBhH+THZ0TbTPru62KacRAoGAIjrDcuT+45eJzmJWrtL+6vqkTaonQfb0NisUWdWaCIzi0dzsUdlVIZiPHH1N+qQtDcbN7K+i8I38G+B5AsBJWah/h41HUpPhbYP4oFs1zzVDU5sqAqIEcJYOqwrOrogMW/+yc4TCoXKrnlyeaITHw2X0EkxtYDXDPFE4/Pmbq8M=";
    private static final String APP_ID = "547414464723419138";

    private static JSONObject sign(){
        /**序列化参数**/
        String content = "{\"memberID\":\"5164\"}";
        long timestamp = Long.parseLong(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
        String nonce = RandomUtil.randomNumbers(10);
        log.info("业务参数："+content);
        String requestSign = "appid="+APP_ID+"&message="+content+"&nonce="+nonce+"&timestamp="+timestamp;
        log.info("待签名字符串：{}",requestSign);
        /**生成签名**/
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA,RSA_PRIVATE_KEY,null);
        byte[] signed = sign.sign(requestSign.getBytes(CharsetUtil.CHARSET_UTF_8));
        log.info("签名："+ Base64.encode(signed));
        JSONObject paramMap = new JSONObject();
        paramMap.put("appid", SignatureVerifyUtil.APP_ID);
        paramMap.put("message",content);
        paramMap.put("nonce",nonce);
        paramMap.put("timestamp",timestamp);
        paramMap.put("signature",Base64.encode(signed));
        return paramMap;
    }

    private static boolean verify(JSONObject map){
        Sign signed = SecureUtil.sign(SignAlgorithm.SHA1withRSA,null,RSA_PUBLIC_KEY);
        String nonce = map.getString("nonce");
        String timestampStr = map.getString("timestamp");
        String appId = map.getString("appid");
        String message = map.getString("message");
        String signature = map.getString("signature");
        String responseSign = "appid="+appId+"&message="+message+"&nonce="+nonce+"&timestamp="+timestampStr;
        return signed.verify(responseSign.getBytes(CharsetUtil.CHARSET_UTF_8), Base64.decode(signature));
    }

    public static void main(String[] args) {
        log.info("签名验证结果：{}",SignatureVerifyUtil.verify(SignatureVerifyUtil.sign()));
    }

}
