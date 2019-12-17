package com.bugjc.ea.opensdk.http.core.component.monitor.rocksdb;

import org.junit.jupiter.api.Test;
import org.rocksdb.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class RocksTTLDBCacheTest {

    private String dbFolder = new File("D:\\data\\test\\").getAbsolutePath();

    @Test
    public void ttlDBOpen() throws RocksDBException, InterruptedException {
        try (final Options options = new Options().setCreateIfMissing(true).setMaxCompactionBytes(0);
             final TtlDB ttlDB = TtlDB.open(options, dbFolder)) {
            ttlDB.put("key".getBytes(), "value".getBytes());
            assertThat(ttlDB.get("key".getBytes())).isEqualTo("value".getBytes());
            assertThat(ttlDB.get("key".getBytes())).isNotNull();
        }
    }

    @Test
    public void ttlDBOpenWithTtl() throws RocksDBException, InterruptedException {
        try (final Options options = new Options().setCreateIfMissing(true).setMaxCompactionBytes(0);
             final TtlDB ttlDB = TtlDB.open(options, dbFolder, 1, false);) {
            ttlDB.put("key".getBytes(), "value".getBytes());
            assertThat(ttlDB.get("key".getBytes())).isEqualTo("value".getBytes());
            TimeUnit.SECONDS.sleep(2);
            ttlDB.compactRange();
            assertThat(ttlDB.get("key".getBytes())).isNull();
        }
    }

    @Test
    public void ttlDbOpenWithColumnFamilies() throws RocksDBException,
            InterruptedException {


        Map<Object, Object> conf = new HashMap<>();
        conf.put("root.dir", "D:\\data\\test\\");
        Map<String,Integer> map = new HashMap<>();
        map.put("total", 2);
        conf.put("cfNames", map);


        final List<ColumnFamilyDescriptor> cfNames = Arrays.asList(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY), new ColumnFamilyDescriptor("new_cf".getBytes()));
        final List<Integer> ttlValues = Arrays.asList(0, 2);

        final List<ColumnFamilyHandle> columnFamilyHandleList = new ArrayList<>();
        try (final DBOptions dbOptions = new DBOptions().setCreateMissingColumnFamilies(true).setCreateIfMissing(true);
             final TtlDB ttlDB = TtlDB.open(dbOptions, dbFolder, cfNames, columnFamilyHandleList, ttlValues, false)) {

            try {
                //ttlDB.put("key".getBytes(), "value".getBytes());

                //assertThat(ttlDB.get("key".getBytes())).isEqualTo("value".getBytes());
                ttlDB.put(columnFamilyHandleList.get(1), "key".getBytes(), "value".getBytes());
                assertThat(ttlDB.get(columnFamilyHandleList.get(1), "key".getBytes())).isEqualTo("value".getBytes());
                TimeUnit.SECONDS.sleep(3);

                ttlDB.compactRange();
                ttlDB.compactRange(columnFamilyHandleList.get(1));
                assertThat(ttlDB.getLatestSequenceNumber()).isEqualTo(0);
                //assertThat(ttlDB.get("key".getBytes())).isNotNull();
                assertThat(ttlDB.get(columnFamilyHandleList.get(1), "key".getBytes())).isNull();
            } finally {
                for (final ColumnFamilyHandle columnFamilyHandle : columnFamilyHandleList) {
                    columnFamilyHandle.close();
                }
            }

        }
    }

    @Test
    public void createTtlColumnFamily() throws RocksDBException, InterruptedException {
        try (final Options options = new Options().setCreateIfMissing(true);
             final TtlDB ttlDB = TtlDB.open(options, dbFolder);
             final ColumnFamilyHandle columnFamilyHandle = ttlDB.createColumnFamilyWithTtl(new ColumnFamilyDescriptor("new_cf".getBytes()), 1)) {

            ttlDB.put(columnFamilyHandle, "key".getBytes(), "value".getBytes());
            assertThat(ttlDB.get(columnFamilyHandle, "key".getBytes())).isEqualTo("value".getBytes());
            TimeUnit.SECONDS.sleep(2);
            ttlDB.compactRange(columnFamilyHandle);
            assertThat(ttlDB.get(columnFamilyHandle, "key".getBytes())).isNull();
        }
    }
}