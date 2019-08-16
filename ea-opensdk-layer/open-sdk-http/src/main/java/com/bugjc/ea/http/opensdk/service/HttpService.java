package com.bugjc.ea.http.opensdk.service;

import com.bugjc.ea.http.opensdk.core.dto.Result;

import java.io.IOException;

/**
 * http 服务
 * @author aoki
 */
public interface HttpService {

    /**
     * http post方式调用接口
     * @param url       --接口地址
     * @param version   --接口版本
     * @param body      --接口参数
     * @return
     * @throws IOException
     */
    Result post(String url, String version, String body) throws IOException;
}
