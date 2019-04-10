package com.bugjc.opensdk.util.http;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.bugjc.opensdk.util.http.exception.HttpSecurityException;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * API 网关服务调用
 * @author aoki
 */
@Slf4j(topic = "API Gateway")
public class ApiGatewayHttpClient {

    //超时时间，默认8s
    private int timeout = 8000;
    //URL
    private String url;
    //网关接入参数
    private Map<String,String> appParams;
    //业务接入参数
    private Map<String,Object> businessParams;


    public ApiGatewayHttpClient url(String url){
        this.url = url;
        return this;
    }

    public ApiGatewayHttpClient timeout(int milliseconds){
        this.timeout = milliseconds;
        return this;
    }

    public ApiGatewayHttpClient initAppParams(Map<String,String> appParams){
        this.appParams = appParams;
        return this;
    }

    public ApiGatewayHttpClient initBusinessParams(Map<String,Object> businessParams){
        this.businessParams = businessParams;
        return this;
    }

    public String execute(){
        //1. 初始化参数
        //2. 发送请求
        //3. 获取请求结果
        return this.sendSignPost();
    }

    /**
     * 发送
     * @return
     */
    private String sendSignPost() {

        if (StrUtil.isBlank(this.url)){
            throw new HttpSecurityException("URL参数未设置");
        }

        if (this.appParams == null || this.appParams.isEmpty()){
            throw new HttpSecurityException("appParams参数未设置");
        }

        if (this.businessParams == null || this.businessParams.isEmpty()){
            throw new HttpSecurityException("businessParams参数未设置");
        }

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

        String timestamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String nonce = RandomUtil.randomNumbers(10);
        String sequence = SequenceUtil.genUnionPaySequence();
        String requestSign = "appid="+appId+"&message="+ StrSortUtil.sortString(body)+"&nonce="+nonce+"&timestamp="+timestamp+"&Sequence="+sequence;
        //logger.info("待签名字符串：{}", requestSign);
        /**生成签名**/
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA,rsaPrivateKey,null);
        byte[] signed = sign.sign(requestSign.getBytes(CharsetUtil.CHARSET_UTF_8));
        log.info("签名：" + Base64.encode(signed));

        HttpRequest httpRequest = HttpRequest.post(url)
                .timeout(this.timeout)
                .body(body,"application/json;charset=UTF-8")
                .header("Version",version)
                .header("Appid",appId)
                .header("Sequence",sequence)
                .header("Timestamp",timestamp)
                .header("Nonce", nonce)
                .header("Signature", Base64.encode(signed));

        String headers = JSON.toJSONString(httpRequest.headers());
        log.info("请求头："+headers);

        /**执行请求**/
        HttpResponse httpResponse = httpRequest.execute();

        /**获取主要返回结果**/
        String resultJson = httpResponse.body();
        log.info("Response Result:" + resultJson);

        /**获取response header**/
        String headerSequenceValue = httpResponse.header("sequence");
        assert sequence != null;
        if (!sequence.equals(headerSequenceValue)){
            throw new HttpSecurityException(1600,"无效的序列号！",null);
        }

        /**验签**/
        nonce = httpResponse.header("Nonce");
        String timestampStr = httpResponse.header("Timestamp");
        String responseSign = "appid="+appId+"&message="+StrSortUtil.sortString(resultJson)+"&nonce="+nonce+"&timestamp="+timestampStr+"&Sequence="+sequence;
        String headerSignatureValue = httpResponse.header("Signature");
        sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA,null,rsaPublicKey);
        boolean verify = sign.verify(responseSign.getBytes(CharsetUtil.CHARSET_UTF_8), Base64.decode(headerSignatureValue));
        if (!verify){
            throw new HttpSecurityException(1601,"验签失败！",null);
        }

        /**返回结果**/
        return resultJson;

    }
}
