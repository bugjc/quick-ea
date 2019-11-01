package com.bugjc.ea.gateway.zuul.service;

/**
 * @author aoki
 */
public interface AppSecurityConfigService {


    /**
     * 检查服务使用的签名的版本
     *
     * @param path
     * @return 1：消费码、二三类账户和乘车码等业务，2：会员服务和通付码业务
     */
    int checkSignatureVersion(String path);

    /**
     * 排除需要签名的路径
     *
     * @param path
     * @return
     */
    boolean excludeNeedSignaturePath(String path);


    /**
     * 排除需要Token认证的路径
     * @param path
     * @return
     */
    boolean excludeNeedAuthTokenPath(String path);
}
