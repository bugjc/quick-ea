package com.bugjc.ea.gateway.core.dto;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.gateway.config.GlobalProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * rsa
 * @author aoki
 */
@Slf4j
@RunWith(SpringRunner.class)
public class RSASignTest {

    @Autowired
    GlobalProperty globalProperty;

    @Test
    public void rsaSHA1() throws Exception {
        //-----------------------------------------客户端----------------------------------------------
        //业务参数、请求体body
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("userId","10001");
        //业务参数序列化
        String content = JSON.toJSONString(map);
        System.out.println("业务参数："+content);
        //用私钥对业务参数进行签名
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA,globalProperty.rsaPrivateKey,null);
        byte[] signed = sign.sign(content.getBytes(CharsetUtil.CHARSET_UTF_8));
        log.info("签名字符串："+Base64.encode(signed));
        //-----------------------------------------服务端----------------------------------------------
        //验签
        sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA,null,globalProperty.rsaPublicKey);
        boolean verify = sign.verify(content.getBytes(CharsetUtil.CHARSET_UTF_8), signed);
        log.info(verify ? "签名验证成功" : "签名验证失败");
    }
}
