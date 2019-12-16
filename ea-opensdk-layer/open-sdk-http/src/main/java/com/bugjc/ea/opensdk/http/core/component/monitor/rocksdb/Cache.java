package com.bugjc.ea.opensdk.http.core.component.monitor.rocksdb;

import java.util.Map;

public interface Cache {

    /**
     * 初始化存储引擎
     * @param conf
     * @throws Exception
     */
    void init(Map<Object, Object> conf) throws Exception;

    void cleanup();

    Object get(String key);

    void remove(String key);

    void put(String key, Object value, int timeoutSecond);

    void put(String key, Object value);

    long size();
}
