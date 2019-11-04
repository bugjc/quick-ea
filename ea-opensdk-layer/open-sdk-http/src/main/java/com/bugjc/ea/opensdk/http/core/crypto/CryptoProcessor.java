package com.bugjc.ea.opensdk.http.core.crypto;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.bugjc.ea.opensdk.http.core.crypto.input.AccessPartyDecryptParam;
import com.bugjc.ea.opensdk.http.core.crypto.input.AccessPartyEncryptParam;
import com.bugjc.ea.opensdk.http.core.crypto.input.ServicePartyDecryptParam;
import com.bugjc.ea.opensdk.http.core.crypto.input.ServicePartyEncryptParam;
import com.bugjc.ea.opensdk.http.core.crypto.output.AccessPartyDecryptObj;
import com.bugjc.ea.opensdk.http.core.crypto.output.AccessPartyEncryptObj;
import com.bugjc.ea.opensdk.http.core.crypto.output.ServicePartyDecryptObj;
import com.bugjc.ea.opensdk.http.core.crypto.output.ServicePartyEncryptObj;
import com.bugjc.ea.opensdk.http.core.util.SequenceUtil;
import com.bugjc.ea.opensdk.http.core.util.StrSortUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 安全加解密类
 * @author aoki
 * @date 2019/11/4
 * **/
@Slf4j
public class CryptoProcessor {

    /**
     * 接入方加密
     * @param accessPartyEncryptParam
     * @return
     */
    public AccessPartyEncryptObj accessPartyEncrypt(AccessPartyEncryptParam accessPartyEncryptParam){
        AccessPartyEncryptObj accessPartyEncryptObj = new AccessPartyEncryptObj();
        accessPartyEncryptObj.setNonce(RandomUtil.randomNumbers(10));
        accessPartyEncryptObj.setTimestamp(DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN));
        accessPartyEncryptObj.setSequence(SequenceUtil.genUnionPaySequence(accessPartyEncryptParam.getAppId()));
        //TODO 加密body
        String body = accessPartyEncryptParam.getBody();
        accessPartyEncryptObj.setBody(body);

        String requestSign = "appid="+accessPartyEncryptParam.getAppId()+"&message="+ StrSortUtil.sortString(body)+"&nonce="+accessPartyEncryptObj.getNonce()+"&timestamp="+accessPartyEncryptObj.getTimestamp()+"&Sequence="+accessPartyEncryptObj.getSequence();
        log.debug("接入方待签名字符串：{}", requestSign);
        /**生成签名**/
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA,accessPartyEncryptParam.getRsaPrivateKey(),null);
        byte[] signed = sign.sign(requestSign.getBytes(CharsetUtil.CHARSET_UTF_8));
        accessPartyEncryptObj.setSignature(Base64.encode(signed));
        log.debug("接入方签名：{}", Base64.encode(signed));
        return accessPartyEncryptObj;
    }

    /**
     * 服务方解密
     * @param servicePartyDecryptParam
     * @return
     */
    public ServicePartyDecryptObj servicePartyDecrypt(ServicePartyDecryptParam servicePartyDecryptParam){
        String responseSign = "appid="+servicePartyDecryptParam.getAppId()+"&message="+ StrSortUtil.sortString(servicePartyDecryptParam.getBody())+"&nonce="+servicePartyDecryptParam.getNonce()+"&timestamp="+servicePartyDecryptParam.getTimestamp()+"&Sequence="+servicePartyDecryptParam.getSequence();
        log.debug("服务方待签名字符串：{}", responseSign);
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA,null, servicePartyDecryptParam.getRsaPublicKey());
        log.debug("服务方签名：{}", servicePartyDecryptParam.getSignature());
        boolean verify = sign.verify(responseSign.getBytes(CharsetUtil.CHARSET_UTF_8), Base64.decode(servicePartyDecryptParam.getSignature()));
        ServicePartyDecryptObj servicePartyDecryptObj = new ServicePartyDecryptObj();
        //TODO 解密body
        servicePartyDecryptObj.setBody(servicePartyDecryptParam.getBody());
        servicePartyDecryptObj.setSignSuccessful(verify);
        return servicePartyDecryptObj;
    }


    /**
     * 服务方加密
     * @param servicePartyEncryptParam
     * @return
     */
    public ServicePartyEncryptObj servicePartyEncrypt(ServicePartyEncryptParam servicePartyEncryptParam){

        ServicePartyEncryptObj servicePartyEncryptObj = new ServicePartyEncryptObj();
        servicePartyEncryptObj.setNonce(RandomUtil.randomNumbers(10));
        servicePartyEncryptObj.setTimestamp(DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN));
        servicePartyEncryptObj.setSequence(servicePartyEncryptParam.getSequence());
        //TODO 加密body
        String body = servicePartyEncryptParam.getBody();
        servicePartyEncryptObj.setBody(body);

        String responseSign = "appid="+servicePartyEncryptParam.getAppId()+"&message="+ StrSortUtil.sortString(servicePartyEncryptParam.getBody())+"&nonce="+servicePartyEncryptObj.getNonce()+"&timestamp="+servicePartyEncryptObj.getTimestamp()+"&Sequence="+servicePartyEncryptObj.getSequence();
        /**生成签名**/
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA,servicePartyEncryptParam.getRsaPrivateKey(),null);
        byte[] signed = sign.sign(responseSign.getBytes(CharsetUtil.CHARSET_UTF_8));
        servicePartyEncryptObj.setSignature(Base64.encode(signed));
        return servicePartyEncryptObj;
    }

    /**
     * 接入方解密
     * @param accessPartyDecryptParam
     * @return
     */
    public AccessPartyDecryptObj accessPartyDecrypt(AccessPartyDecryptParam accessPartyDecryptParam){
        String responseSign = "appid="+accessPartyDecryptParam.getAppId()+"&message="+ StrSortUtil.sortString(accessPartyDecryptParam.getBody())+"&nonce="+accessPartyDecryptParam.getNonce()+"&timestamp="+accessPartyDecryptParam.getTimestamp()+"&Sequence="+accessPartyDecryptParam.getSequence();
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA,null,accessPartyDecryptParam.getRsaPublicKey());
        boolean verify = sign.verify(responseSign.getBytes(CharsetUtil.CHARSET_UTF_8), Base64.decode(accessPartyDecryptParam.getSignature()));
        AccessPartyDecryptObj accessPartyDecryptObj = new AccessPartyDecryptObj();
        //TODO 解密body
        accessPartyDecryptObj.setBody(accessPartyDecryptParam.getBody());
        accessPartyDecryptObj.setSignSuccessful(verify);
        return accessPartyDecryptObj;
    }
}
