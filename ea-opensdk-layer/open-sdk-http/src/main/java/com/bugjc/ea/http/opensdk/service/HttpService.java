package com.bugjc.ea.http.opensdk.service;

import java.io.IOException;

public interface HttpService {

    /**
     * http post方式调用接口
     * @param url   -- 接口全地址
     * @param version   --接口版本
     * @param body  --接口参数
     * @return
     */
    String post(String url, String version, String body) throws IOException;
}
