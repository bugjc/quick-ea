package com.bugjc.ea.gateway.core.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.gateway.core.dto.Result;
import com.bugjc.ea.gateway.core.dto.ResultCode;
import com.bugjc.ea.gateway.core.enums.ResultErrorEnum;
import com.bugjc.ea.gateway.model.App;
import com.bugjc.ea.gateway.service.AppService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * API 网关服务调用
 * @author aoki
 */
@Slf4j(topic = "API Gateway")
public class ApiGatewayHttpUtil {

    public static Result post(String url, Map<String,String> appParams, Map<String, Object> businessParams) throws Exception {

        log.info("API Gateway URL:{}", url);

        String body = "{}";
        if(businessParams != null && !businessParams.isEmpty()){
            body = JSON.toJSONString(businessParams);
        }
        /**序列化参数**/
        log.info("Business Params:{}" + body);

        String appId = appParams.get("appId");
        String version = appParams.get("version");
        String rsaPrivateKey = appParams.get("rsaPrivateKey");
        String rsaPublicKey = appParams.get("rsaPublicKey");

        long timestamp = Long.parseLong(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
        String nonce = RandomUtil.randomNumbers(10);
        String sequence = SequenceUtil.genUnionPaySequence();
        String requestSign = "appid="+appId+"&message="+ StrSortUtil.sortString(body)+"&nonce="+nonce+"&timestamp="+timestamp+"&Sequence="+sequence;
        //logger.info("待签名字符串：{}", requestSign);
        /**生成签名**/
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA,rsaPrivateKey,null);
        byte[] signed = sign.sign(requestSign.getBytes(CharsetUtil.CHARSET_UTF_8));
        //logger.info("签名：" + Base64.encode(signed));

        HttpRequest httpRequest = HttpRequest.post(url)
                .timeout(8000)
                .body(body,"application/json;charset=UTF-8")
                .header("Version",version)
                .header("Appid",appId)
                .header("Sequence",sequence)
                .header("Timestamp",""+timestamp)
                .header("Nonce", nonce)
                .header("Signature", Base64.encode(signed));

        String headers = JSON.toJSONString(httpRequest.headers());
        //log.info("请求头："+headers);

        /**执行请求**/
        HttpResponse httpResponse = httpRequest.execute();

        /**获取主要返回结果**/
        String resultJson = httpResponse.body();
        log.info("Response Result:" + resultJson);
        Result result = JSON.parseObject(resultJson, Result.class);
        if (result.getCode() != ResultCode.SUCCESS.getCode()){
            log.info("业务执行失败,错误描述:{}",result.getMessage());
            return result;
        }

        /**获取response header**/
        String headerSequenceValue = httpResponse.header("sequence");
        assert sequence != null;
        if (!sequence.equals(headerSequenceValue)){
            throw new Exception("无效的序列号！");
        }

        /**验签**/
        nonce = httpResponse.header("Nonce");
        String timestampStr = httpResponse.header("Timestamp");
        String responseSign = "appid="+appId+"&message="+StrSortUtil.sortString(resultJson)+"&nonce="+nonce+"&timestamp="+timestampStr+"&Sequence="+sequence;
        String headerSignatureValue = httpResponse.header("Signature");
        sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA,null,rsaPublicKey);
        boolean verify = sign.verify(responseSign.getBytes(CharsetUtil.CHARSET_UTF_8), Base64.decode(headerSignatureValue));
        if (!verify){
            throw new Exception("签名验证失败！");
        }

        /**返回结果**/
        return result;

    }
}
