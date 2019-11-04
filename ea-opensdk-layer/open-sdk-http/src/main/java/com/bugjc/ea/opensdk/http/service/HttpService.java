package com.bugjc.ea.opensdk.http.service;

import com.bugjc.ea.opensdk.http.core.dto.Result;

import java.io.IOException;

/**
 * http 服务
 * @author aoki
 */
public interface HttpService {

    /**
     * http post方式调用接口(自动获取token)
     * @param path       --接口地址
     * @param version    --接口版本
     * @param body       --接口参数
     * @return
     * @throws IOException
     */
    Result post(String path, String version, String body) throws IOException;

    /**
     * http post方式调用接口（手动设置token）
     * @param path       --接口地址
     * @param version    --接口版本
     * @param token      --访问令牌
     * @param body       --接口参数
     * @return
     * @throws IOException
     */
    Result post(String path, String version,String token,String body) throws IOException;
}
