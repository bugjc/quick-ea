package com.bugjc.ea.opensdk.http.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.http.core.component.AuthConfig;
import com.bugjc.ea.opensdk.http.core.component.impl.AuthDefaultConfigImpl;
import com.bugjc.ea.opensdk.http.core.component.impl.AuthRedisConfigImpl;
import com.bugjc.ea.opensdk.http.core.constants.HttpHeaderKeyConstants;
import com.bugjc.ea.opensdk.http.core.crypto.CryptoProcessor;
import com.bugjc.ea.opensdk.http.core.crypto.input.AccessPartyDecryptParam;
import com.bugjc.ea.opensdk.http.core.crypto.input.AccessPartyEncryptParam;
import com.bugjc.ea.opensdk.http.core.crypto.output.AccessPartyDecryptObj;
import com.bugjc.ea.opensdk.http.core.crypto.output.AccessPartyEncryptObj;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.exception.HttpSecurityException;
import com.bugjc.ea.opensdk.http.model.AppParam;
import com.bugjc.ea.opensdk.http.service.AuthService;
import com.bugjc.ea.opensdk.http.service.HttpService;
import com.bugjc.ea.opensdk.http.service.JobService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import redis.clients.jedis.JedisPool;

import java.io.IOException;

/**
 * @author aoki
 */
@Slf4j
public class HttpServiceImpl implements HttpService {

    @Getter
    @Setter
    private AppParam appParam;
    @Getter
    @Setter
    private OkHttpClient okHttpClient;
    @Getter
    @Setter
    private JedisPool jedisPool;
    @Getter
    private AuthService authService = new AuthServiceImpl(this);
    @Getter
    private JobService jobService = new JobServiceImpl(this);

    @Override
    public Result post(String path, String version, String body) throws IOException {
        AuthConfig authConfig = null;
        if (jedisPool == null){
            authConfig = AuthDefaultConfigImpl.getInstance(this);
        } else {
            authConfig = AuthRedisConfigImpl.getInstance(this, jedisPool);
        }

        return post(path,version, authConfig.getToken(),body);
    }


    /**
     * http 接口调用方法
     * @param path
     * @param version
     * @param token
     * @param body
     * @return
     * @throws IOException
     */
    @Override
    public Result post(String path, String version,String token,String body) throws IOException {
        if (this.appParam == null){
            throw new HttpSecurityException("参数不能为空");
        }

        if (StrUtil.isBlank(path)){
            throw new HttpSecurityException("PATH 参数未设置");
        }

        //参数判断
        if(StrUtil.isBlank(body)){
            body = "{}";
        }

        //接入方安全处理
        AccessPartyEncryptParam accessPartyEncryptParam = new AccessPartyEncryptParam();
        BeanUtil.copyProperties(appParam, accessPartyEncryptParam);
        accessPartyEncryptParam.setBody(body);
        CryptoProcessor cryptoProcessor = new CryptoProcessor();
        AccessPartyEncryptObj accessPartyEncryptObj = cryptoProcessor.accessPartyEncrypt(accessPartyEncryptParam);

        //创建request body
        MediaType mediaType = MediaType.parse(HttpHeaderKeyConstants.CONTENT_TYPE_APPLICATION_JSON_VALUE);
        RequestBody requestBody = RequestBody.create(mediaType, body.getBytes());

        //完整地址
        String url = appParam.getBaseUrl().concat(path);
        log.debug("URL:{}",url);
        log.debug("RequestBody:{}", body);

        //构建请求
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody);
        builder.header(HttpHeaderKeyConstants.VERSION, version)
                .header(HttpHeaderKeyConstants.CONTENT_TYPE, HttpHeaderKeyConstants.CONTENT_TYPE_APPLICATION_JSON_VALUE)
                .header(HttpHeaderKeyConstants.ACCEPT, HttpHeaderKeyConstants.CONTENT_TYPE_APPLICATION_JSON_VALUE)
                .header(HttpHeaderKeyConstants.APP_ID, appParam.getAppId())
                .header(HttpHeaderKeyConstants.SEQUENCE, accessPartyEncryptObj.getSequence())
                .header(HttpHeaderKeyConstants.TIMESTAMP, accessPartyEncryptObj.getTimestamp())
                .header(HttpHeaderKeyConstants.NONCE, accessPartyEncryptObj.getNonce())
                .header(HttpHeaderKeyConstants.SIGNATURE, accessPartyEncryptObj.getSignature());
        if (StrUtil.isNotBlank(token)){
            builder.header(HttpHeaderKeyConstants.AUTHORIZATION, token);
        }

        Request request =  builder.build();
        //执行请求
        Response httpResponse = this.okHttpClient.newCall(request).execute();

        if (httpResponse.code() != HttpStatus.HTTP_OK){
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
            log.debug("ResponseBody:{}",resultJson);
            if (StrUtil.isNotBlank(httpResponse.header(HttpHeaderKeyConstants.GATEWAY_ERROR_FLAG))){
                //直接返回
                return JSON.parseObject(resultJson,Result.class);
            }

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
            }else {
                log.info("解密失败");
            }

            /**返回结果**/
            return JSON.parseObject(accessPartyDecryptObj.getBody(), Result.class);
        }finally {
            if (httpResponse.body() != null) {
                httpResponse.close();
            }
        }
    }

}
