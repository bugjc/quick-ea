package com.glcxw.util;

import com.bugjc.ea.opensdk.APIBuilder;
import com.bugjc.ea.opensdk.service.HttpService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class HttpUtil {
    /**
     * 获取http服务对象
     * @return
     */
    public static HttpService getHttpService() {
        return new APIBuilder()
                .setAppId("1111")
                .setBaseUrl("http://127.0.0.1")
                .setRsaPrivateKey("")
                .setRsaPublicKey("")
                .setHttpConnTimeout(5000)
                .build();
    }

    @Test
    public void test() throws IOException {
        String path = "/test/v1";
        String version = "1.0";
        String body = "{}";
        String result = getHttpService().post(path,version,body);
        log.info(result);
    }


}
