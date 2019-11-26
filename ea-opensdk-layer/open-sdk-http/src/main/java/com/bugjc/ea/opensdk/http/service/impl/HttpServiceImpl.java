package com.bugjc.ea.opensdk.http.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.http.core.component.eureka.impl.EurekaDefaultConfigImpl;
import com.bugjc.ea.opensdk.http.core.component.eureka.model.Server;
import com.bugjc.ea.opensdk.http.core.component.token.AuthConfig;
import com.bugjc.ea.opensdk.http.core.component.token.impl.AuthDefaultConfigImpl;
import com.bugjc.ea.opensdk.http.core.component.token.impl.AuthRedisConfigImpl;
import com.bugjc.ea.opensdk.http.core.constants.HttpHeaderKeyConstants;
import com.bugjc.ea.opensdk.http.core.crypto.CryptoProcessor;
import com.bugjc.ea.opensdk.http.core.crypto.input.AccessPartyDecryptParam;
import com.bugjc.ea.opensdk.http.core.crypto.input.AccessPartyEncryptParam;
import com.bugjc.ea.opensdk.http.core.crypto.output.AccessPartyDecryptObj;
import com.bugjc.ea.opensdk.http.core.crypto.output.AccessPartyEncryptObj;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.exception.HttpSecurityException;
import com.bugjc.ea.opensdk.http.core.util.IpAddressUtil;
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
import java.util.Objects;

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
        if (jedisPool == null) {
            authConfig = AuthDefaultConfigImpl.getInstance(this);
        } else {
            authConfig = AuthRedisConfigImpl.getInstance(this, jedisPool);
        }

        return post(path, version, authConfig.getToken(), body);
    }



    /**
     * http 接口调用方法
     *
     * @param path
     * @param version
     * @param token
     * @param body
     * @return
     * @throws IOException
     */
    @Override
    public Result post(String path, String version, String token, String body) throws HttpSecurityException {
        if (this.appParam == null) {
            throw new HttpSecurityException("参数不能为空");
        }

        if (StrUtil.isBlank(path)) {
            throw new HttpSecurityException("PATH 参数未设置");
        }

        //参数判断
        if (StrUtil.isBlank(body)) {
            body = "{}";
        }

        boolean flag = IpAddressUtil.internalIp(appParam.getBaseUrl());
        if (flag){
            return execInternalHttpRequest(path, version, token, body);
        } else {
            return execHttpRequest(path, version, token, body);
        }
    }

    /**
     * 内网调用
     * @param path
     * @param version
     * @param token
     * @param body
     * @return
     */
    private Result execInternalHttpRequest(String path, String version, String token, String body){


        //TODO 1.网关初始化时向 redis 服务插入映射关系，例如：/job --> job-server,然后在加上元数据，例如是否需要截断前缀
        //2.sdk 初始化增加内部调用检测，检测到内部调用提示注入 eureka 和 redis(存有映射关系的redis)
        //3.缓存内部调用路径
        //4.内部调用增加断路器
        //5.增加监控，使用适配器、桥接等方式
        //todo 统一捕获异常
        Server server = EurekaDefaultConfigImpl.getInstance(jedisPool).chooseServer(path);
        if (server == null){
            log.warn("查找不到对应的内部服务，转外网调用方式。");
            return execHttpRequest(path, version, token, body);
        }
        String url = server.getUrl();
        log.debug("URL:{}", url);
        log.debug("RequestBody:{}", body);

        //创建request body
        MediaType mediaType = MediaType.parse(HttpHeaderKeyConstants.CONTENT_TYPE_APPLICATION_JSON_VALUE);
        RequestBody requestBody = RequestBody.create(mediaType, body.getBytes());

        //构建请求
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody);

        //执行请求
        try (Response httpResponse = this.okHttpClient.newCall(builder.build()).execute()) {

            Response response = optionalOf(httpResponse);
            String resultJson = Objects.requireNonNull(response.body()).string();
            log.info("ResponseBody:{}", resultJson);
            if (StrUtil.isNotBlank(httpResponse.header(HttpHeaderKeyConstants.GATEWAY_ERROR_FLAG))) {
                //直接返回
                return JSON.parseObject(resultJson, Result.class);
            }

            /**返回结果**/
            return JSON.parseObject(resultJson, Result.class);
        } catch (IOException e) {
            log.error("调用接口异常信息：{}", e.getMessage());
            throw new HttpSecurityException("接口调用异常！错误信息: "+e.getMessage());
        }
    }

    /**
     * 外网调用
     * @param path
     * @param version
     * @param token
     * @param body
     * @return
     */
    private Result execHttpRequest(String path, String version, String token, String body){
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
        log.debug("URL:{}", url);
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
        if (StrUtil.isNotBlank(token)) {


            builder.header(HttpHeaderKeyConstants.AUTHORIZATION, token);
        }

        //执行请求
        try (Response httpResponse = this.okHttpClient.newCall(builder.build()).execute()) {

            Response response = optionalOf(httpResponse);
            String resultJson = Objects.requireNonNull(response.body()).string();
            log.debug("ResponseBody:{}", resultJson);
            if (StrUtil.isNotBlank(httpResponse.header(HttpHeaderKeyConstants.GATEWAY_ERROR_FLAG))) {
                //直接返回
                return JSON.parseObject(resultJson, Result.class);
            }

            /**获取response header**/
            String headerSequenceValue = httpResponse.header(HttpHeaderKeyConstants.SEQUENCE);
            if (!accessPartyEncryptObj.getSequence().equals(headerSequenceValue)) {
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
            if (accessPartyDecryptObj.isSignSuccessful()) {
                log.info("解密成功");
            } else {
                log.info("解密失败");
            }

            /**返回结果**/
            return JSON.parseObject(accessPartyDecryptObj.getBody(), Result.class);
        } catch (IOException e) {
            log.error("调用接口异常信息：{}", e.getMessage());
            throw new HttpSecurityException("接口调用异常！错误信息: "+ e.getMessage());
        }
    }

    /**
     * Response null 检测
     * @param httpResponse
     * @return
     * @throws HttpSecurityException
     */
    private Response optionalOf(Response httpResponse) throws HttpSecurityException{

        if (!httpResponse.isSuccessful()) {
            throw new HttpSecurityException(httpResponse.code(),"服务器端错误: " + httpResponse.message());
        }

        if (httpResponse.body() == null) {
            throw new HttpSecurityException("接口异常！");
        }
        return httpResponse;
    }

}
