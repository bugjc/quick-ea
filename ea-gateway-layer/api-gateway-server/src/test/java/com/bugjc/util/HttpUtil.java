package com.bugjc.util;

import com.bugjc.ea.http.opensdk.ApiBuilder;
import com.bugjc.ea.http.opensdk.core.dto.Result;
import com.bugjc.ea.http.opensdk.model.AppParam;
import com.bugjc.ea.http.opensdk.service.HttpService;
import com.bugjc.env.EnvUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class HttpUtil {
    /**
     * 获取http服务对象
     * @return
     */
    public static HttpService getHttpService(AppParam appParam) {
        return new ApiBuilder()
                .setAppParam(appParam)
                .setHttpConnTimeout(5000)
                .build();
    }

    @Test
    public void test() throws IOException {
        String path = "/test/v1";
        String version = "1.0";
        String body = "{}";
        Result result = getHttpService(EnvUtil.getTestServer()).post(path,version,body);
        log.info(result.toString());
    }


}
