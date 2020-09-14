
package com.bugjc.ea.code.generator.core.db.converts;

import com.bugjc.ea.code.generator.config.GlobalConfig;
import com.bugjc.ea.code.generator.config.ITypeConvert;
import com.bugjc.ea.code.generator.core.db.rules.IColumnType;
import com.bugjc.ea.code.generator.core.db.rules.DbColumnType;

import static com.bugjc.ea.code.generator.core.db.converts.TypeConverts.contains;
import static com.bugjc.ea.code.generator.core.db.converts.TypeConverts.containsAny;

/**
 * DM 字段类型转换
 *
 * @author halower, hanchunlin
 * @since 2019-06-27
 */
public class DmTypeConvert implements ITypeConvert {
    public static final DmTypeConvert INSTANCE = new DmTypeConvert();

    /**
     * 字符数据类型: CHAR,CHARACTER,VARCHAR
     * <p>
     * 数值数据类型: NUMERIC,DECIMAL,DEC,MONEY,BIT,BOOL,BOOLEAN,INTEGER,INT,BIGINT,TINYINT,BYTE,SMALLINT,BINARY,
     * VARBINARY
     * <p>
     * 近似数值数据类型: FLOAT
     * <p>
     * DOUBLE, DOUBLE PRECISION,REAL
     * <p>
     * 日期时间数据类型
     * <p>
     * 多媒体数据类型: TEXT,LONGVARCHAR,CLOB,BLOB,IMAGE
     *
     * @param config    全局配置
     * @param fieldType 字段类型
     * @return 对应的数据类型
     * @inheritDoc
     */
    @Override
    public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(containsAny("char", "text").then(DbColumnType.STRING))
            .test(containsAny("numeric", "dec", "money").then(DbColumnType.BIG_DECIMAL))
            .test(containsAny("bit", "bool").then(DbColumnType.BOOLEAN))
            .test(contains("bigint").then(DbColumnType.BIG_INTEGER))
            .test(containsAny("int", "byte").then(DbColumnType.INTEGER))
            .test(contains("binary").then(DbColumnType.BYTE_ARRAY))
            .test(contains("float").then(DbColumnType.FLOAT))
            .test(containsAny("double", "real").then(DbColumnType.DOUBLE))
            .test(containsAny("date", "time").then(DbColumnType.DATE))
            .test(contains("clob").then(DbColumnType.CLOB))
            .test(contains("blob").then(DbColumnType.BLOB))
            .test(contains("image").then(DbColumnType.BYTE_ARRAY))
            .or(DbColumnType.STRING);
    }

}
