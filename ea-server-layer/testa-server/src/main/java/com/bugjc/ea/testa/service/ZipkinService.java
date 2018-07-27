package com.bugjc.ea.testa.service;

/**
 * zipkin service test
 * @author qingyang
 */
public interface ZipkinService {

    /**
     * 测试第一步
     * @param userId
     */
    void orderStep1(String userId);

    /**
     * 测试第二步
     * @param userId
     */
    void orderStep2(String userId);

}
