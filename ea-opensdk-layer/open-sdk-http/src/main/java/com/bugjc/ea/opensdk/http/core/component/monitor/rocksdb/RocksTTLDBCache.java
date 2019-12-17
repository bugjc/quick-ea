package com.bugjc.ea.opensdk.http.core.component.monitor.rocksdb;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.*;

import java.io.File;
import java.util.Comparator;
import java.util.*;

@Slf4j
public class RocksTTLDBCache implements Cache{

    static {
        RocksDB.loadLibrary();
    }

    private static final String ROOT_DIR = "root.dir";
    private TtlDB ttlDB;
    private String rootDir;
    private TreeMap<byte[], ColumnFamilyHandle> windowHandlers = new TreeMap<byte[], ColumnFamilyHandle>(new Comparator<byte[]>() {
        @Override
        public int compare(byte[] o1, byte[] o2) {
            return 1;
        }
    });

    public void initDir(Map<Object, Object> conf) {
        String confDir = (String) conf.get(ROOT_DIR);
        if (StrUtil.isBlank(confDir)) {
            throw new RuntimeException("rootDir of rocksDB is not specified!");
        }

        File file = new File(confDir);
        if (!file.exists()) {
            file = new File(confDir);
        }

        rootDir = file.getAbsolutePath();
    }

    public void initDb(Map<String, Integer> columnFamilyMap) throws Exception {
        List<ColumnFamilyDescriptor> cfNames = new ArrayList<>();
        cfNames.add(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY));

        List<Integer> ttlValues = new ArrayList<>();
        // Default column family with infinite TTL
        // NOTE that the first must be 0, RocksDB.java API has this limitation
        ttlValues.add(0);

        for (Map.Entry<String, Integer> stringIntegerEntry : columnFamilyMap.entrySet()) {
            //添加列族
            cfNames.add(new ColumnFamilyDescriptor(stringIntegerEntry.getKey().getBytes()));
            //new column family with list second ttl
            ttlValues.add(stringIntegerEntry.getValue());
        }

        final List<ColumnFamilyHandle> columnFamilyHandleList = new ArrayList<>();
        final DBOptions dbOptions = new DBOptions().setCreateMissingColumnFamilies(true).setCreateIfMissing(true);
        this.ttlDB = TtlDB.open(dbOptions, rootDir, cfNames, columnFamilyHandleList, ttlValues, false);
        for (ColumnFamilyHandle columnFamilyHandle : columnFamilyHandleList) {
            windowHandlers.put(columnFamilyHandle.getName(), columnFamilyHandle);
        }
        log.info("Successfully init rocksDB of {}", rootDir);
    }

    @Override
    public void init(Map<Object, Object> conf) throws RuntimeException {
        initDir(conf);

        Map<String, Integer>  map = (Map<String, Integer>) conf.get("cfNames");

        // retry
        boolean isSuccess = false;
        for (int i = 0; i < 3; i++) {
            try {
                initDb(map);
                isSuccess = true;
                break;
            } catch (Exception e) {
                log.warn("Failed to init rocksDB " + rootDir, e);
            }
        }

        if (!isSuccess) {
            throw new RuntimeException("Failed to init rocksDB " + rootDir);
        }
    }

    @Override
    public void cleanup() {
        log.info("Begin to close rocketDb of {}", rootDir);
        for (ColumnFamilyHandle columnFamilyHandle : windowHandlers.values()) {
            columnFamilyHandle.close();
        }

        if (ttlDB != null) {
            ttlDB.close();
        }

        log.info("Successfully closed rocketDb of {}", rootDir);
    }

    @Override
    public Object get(String key) {
        try {
            byte[] keyBytes = key.getBytes();
            ColumnFamilyHandle columnFamilyHandle = windowHandlers.get(keyBytes);
            byte[] data = ttlDB.get(columnFamilyHandle, keyBytes);
            if (data != null) {
                try {
                    return JSON.parse(data);
                } catch (Exception e) {
                    log.error("Failed to deserialize obj of " + key);
                    ttlDB.delete(columnFamilyHandle, key.getBytes());
                    return null;
                }
            }
        } catch (Exception ignored) {}
        return null;
    }


    @Override
    public void remove(String key) {
        try {
            byte[] keyBytes = key.getBytes();
            ttlDB.delete(windowHandlers.get(keyBytes), key.getBytes());
        } catch (Exception e) {
            log.error("Failed to remove " + key);
        }
    }


    protected void put(String key, Object value, Map.Entry<byte[], ColumnFamilyHandle> entry) {
        byte[] data = JSON.toJSONBytes(value);
        try {
            ttlDB.put(entry.getValue(), key.getBytes(), data);
        } catch (Exception e) {
            log.error("Failed to put key into cache, " + key, e);
        }
    }

    private Map.Entry<byte[], ColumnFamilyHandle> getHandler(byte[] timeoutSecond) {
        Map.Entry<byte[], ColumnFamilyHandle> ceilingEntry = windowHandlers.ceilingEntry(timeoutSecond);
        if (ceilingEntry != null) {
            return ceilingEntry;
        } else {
            return windowHandlers.firstEntry();
        }
    }

    @Override
    public void put(String key, Object value, byte[] timeoutSecond) {
        put(key, value, getHandler(timeoutSecond));
    }

    @Override
    public void put(String key, Object value) {
        put(key, value, windowHandlers.firstEntry());
    }

    @Override
    public long size() {
        return ttlDB.getLatestSequenceNumber();
    }

    private static Cache cache;
    static {
        Map<Object, Object> conf = new HashMap<>();
        conf.put("root.dir", "D:\\data\\test\\");
        Map<String,Integer> map = new HashMap<>();
        map.put("total", 2);
        conf.put("cfNames", map);
        cache = new RocksTTLDBCache();
        try {
            cache.init(conf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        System.out.println(cache.size());
        cache.put("total", "123456");
        //ThreadUtil.sleep(3000);
        System.out.println(cache.get("total"));
    }
}
