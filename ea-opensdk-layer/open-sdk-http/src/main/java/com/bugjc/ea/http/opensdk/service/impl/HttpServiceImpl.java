package com.bugjc.ea.http.opensdk.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.core.constants.HttpHeaderKeyConstants;
import com.bugjc.ea.opensdk.core.crypto.CryptoProcessor;
import com.bugjc.ea.opensdk.core.crypto.input.AccessPartyDecryptParam;
import com.bugjc.ea.opensdk.core.crypto.input.AccessPartyEncryptParam;
import com.bugjc.ea.opensdk.core.crypto.output.AccessPartyDecryptObj;
import com.bugjc.ea.opensdk.core.crypto.output.AccessPartyEncryptObj;
import com.bugjc.ea.opensdk.core.exception.HttpSecurityException;
import com.bugjc.ea.opensdk.model.AppParam;
import com.bugjc.ea.opensdk.service.HttpService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class HttpServiceImpl implements HttpService {

    /**
     * 应用接入参数
     */
    @Getter
    @Setter
    private AppParam appParam;
    @Getter
    @Setter
    private OkHttpClient httpClient;;

    @Override
    public String post(String url, String version, String body) throws IOException {
        if (this.appParam == null){
            throw new HttpSecurityException("参数不能为空");
        }

        if (StrUtil.isBlank(url)){
            throw new HttpSecurityException("URL参数未设置");
        }

        //,.,参数判断
        if(StrUtil.isBlank(body)){
            body = "{}";
        }

        //接入方安全处理
        AccessPartyEncryptParam accessPartyEncryptParam = new AccessPartyEncryptParam();
        BeanUtil.copyProperties(appParam,accessPartyEncryptParam);
        accessPartyEncryptParam.setBody(body);
        CryptoProcessor cryptoProcessor = new CryptoProcessor();
        AccessPartyEncryptObj accessPartyEncryptObj = cryptoProcessor.accessPartyEncrypt(accessPartyEncryptParam);

        //创建request body
        MediaType mediaType = MediaType.parse(HttpHeaderKeyConstants.CONTENT_TYPE_APPLICATION_JSON_VALUE);
        RequestBody requestBody = RequestBody.create(mediaType, body.getBytes());

        //构建请求
        Request httpRequest = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header(HttpHeaderKeyConstants.VERSION, version)
                .header(HttpHeaderKeyConstants.CONTENT_TYPE,HttpHeaderKeyConstants.CONTENT_TYPE_APPLICATION_JSON_VALUE)
                .header(HttpHeaderKeyConstants.ACCEPT, HttpHeaderKeyConstants.CONTENT_TYPE_APPLICATION_JSON_VALUE)
                .header(HttpHeaderKeyConstants.APP_ID,appParam.getAppId())
                .header(HttpHeaderKeyConstants.SEQUENCE,accessPartyEncryptObj.getSequence())
                .header(HttpHeaderKeyConstants.TIMESTAMP,accessPartyEncryptObj.getTimestamp())
                .header(HttpHeaderKeyConstants.NONCE, accessPartyEncryptObj.getNonce())
                .header(HttpHeaderKeyConstants.SIGNATURE, accessPartyEncryptObj.getSignature())
                .build();

        //执行请求
        Response httpResponse = this.httpClient.newCall(httpRequest).execute();

        if (httpResponse.code() != 200){
            throw new HttpSecurityException(httpResponse.code(),"服务器端错误: " + httpResponse.message());
        }

        if (!httpResponse.isSuccessful()) {
            throw new HttpSecurityException("服务器端错误: " + httpResponse);
        }

        if (httpResponse.body() == null){
            throw new HttpSecurityException("接口异常！");
        }

        try {
            String resultJson = httpResponse.body().string();
            /**获取response header**/
            String headerSequenceValue = httpResponse.header(HttpHeaderKeyConstants.SEQUENCE);
            if (!accessPartyEncryptObj.getSequence().equals(headerSequenceValue)){
                throw new HttpSecurityException("无效的序列号！");
            }

            //接入方解密处理
            AccessPartyDecryptParam accessPartyDecryptParam = new AccessPartyDecryptParam();
            accessPartyDecryptParam.setAppId(appParam.getAppId());
            accessPartyDecryptParam.setRsaPublicKey(appParam.getRsaPublicKey());
            accessPartyDecryptParam.setNonce(httpResponse.header(HttpHeaderKeyConstants.NONCE));
            accessPartyDecryptParam.setTimestamp(httpResponse.header(HttpHeaderKeyConstants.TIMESTAMP));
            accessPartyDecryptParam.setSequence(headerSequenceValue);
            accessPartyDecryptParam.setBody(resultJson);
            accessPartyDecryptParam.setSignature(httpResponse.header(HttpHeaderKeyConstants.SIGNATURE));
            AccessPartyDecryptObj accessPartyDecryptObj = cryptoProcessor.accessPartyDecrypt(accessPartyDecryptParam);
            if (accessPartyDecryptObj.isSignSuccessful()){
                log.info("解密成功");
            }

            /**返回结果**/
            return accessPartyDecryptObj.getBody();
        }finally {
            if (httpResponse.body() != null) {
                httpResponse.close();
            }
        }
    }


}
