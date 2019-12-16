package com.bugjc.ea.opensdk.http.core.component.monitor.rocksdb;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.*;

import java.io.File;
import java.util.*;

@Slf4j
public class RocksTTLDBCache implements Cache{

    static {
        RocksDB.loadLibrary();
    }

    public static final String ROCKSDB_ROOT_DIR = "rocksdb.root.dir";
    protected TtlDB ttlDB;
    protected String rootDir;
    protected TreeMap<Integer, ColumnFamilyHandle> windowHandlers = new TreeMap<>();

    public void initDir(Map<Object, Object> conf) {
        String confDir = (String) conf.get(ROCKSDB_ROOT_DIR);
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
        for (int i = 0; i < ttlValues.size(); i++) {
            windowHandlers.put(ttlValues.get(i), columnFamilyHandleList.get(i));
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
        //TODO 直接列族名稱獲取ColumnFamilyHandle
        for (Map.Entry<Integer, ColumnFamilyHandle> entry : windowHandlers.entrySet()) {
            try {
                byte[] data = ttlDB.get(entry.getValue(), key.getBytes());
                if (data != null) {
                    try {
                        System.out.println(JSON.parse(data));
                        //return JSON.parse(data);
                    } catch (Exception e) {
                        log.error("Failed to deserialize obj of " + key);
                        ttlDB.delete(entry.getValue(), key.getBytes());
                        return null;
                    }
                }
            } catch (Exception ignored) {
            }
        }

        return null;
    }


    @Override
    public void remove(String key) {
        for (Map.Entry<Integer, ColumnFamilyHandle> entry : windowHandlers.entrySet()) {
            try {
                ttlDB.delete(entry.getValue(), key.getBytes());

            } catch (Exception e) {
                log.error("Failed to remove " + key);
            }
        }
    }


    protected void put(String key, Object value, Map.Entry<Integer, ColumnFamilyHandle> entry) {
        byte[] data = JSON.toJSONBytes(value);
        try {
            ttlDB.put(entry.getValue(), key.getBytes(), data);
        } catch (Exception e) {
            log.error("Failed to put key into cache, " + key, e);
            return;
        }

//        try {
//            ttlDB.compactRange();
//            ttlDB.compactRange(entry.getValue());
//        } catch (RocksDBException e) {
//            e.printStackTrace();
//        }


        for (Map.Entry<Integer, ColumnFamilyHandle> removeEntry : windowHandlers.entrySet()) {
            if (removeEntry.getKey().equals(entry.getKey())) {
                continue;
            }

            try {
                ttlDB.delete(removeEntry.getValue(), key.getBytes());
                System.out.println(Arrays.toString(ttlDB.get(removeEntry.getValue(), key.getBytes())));
            } catch (Exception e) {
                log.warn("Failed to remove other " + key);
            }
        }
    }

    protected Map.Entry<Integer, ColumnFamilyHandle> getHandler(int timeoutSecond) {
        Map.Entry<Integer, ColumnFamilyHandle> ceilingEntry = windowHandlers.ceilingEntry(timeoutSecond);
        if (ceilingEntry != null) {
            return ceilingEntry;
        } else {
            return windowHandlers.firstEntry();
        }
    }

    @Override
    public void put(String key, Object value, int timeoutSecond) {
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
        conf.put("rocksdb.root.dir", "D:\\data\\test\\");
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
        System.out.println(cache.size());
        ThreadUtil.sleep(3000);
        System.out.println(cache.size());
        cache.put("20", "555555");
        System.out.println(cache.get("total"));
    }
}
