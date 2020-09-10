
package com.bugjc.ea.code.generator.core.db.converts;

import com.bugjc.ea.code.generator.config.GlobalConfig;
import com.bugjc.ea.code.generator.config.ITypeConvert;
import com.bugjc.ea.code.generator.core.db.rules.IColumnType;
import com.bugjc.ea.code.generator.core.db.rules.DbColumnType;

import static com.bugjc.ea.code.generator.core.db.converts.TypeConverts.contains;
import static com.bugjc.ea.code.generator.core.db.converts.TypeConverts.containsAny;

/**
 * DB2 字段类型转换
 *
 * @author zhanyao, hanchunlin
 * @since 2018-05-16
 */
public class DB2TypeConvert implements ITypeConvert {
    public static final DB2TypeConvert INSTANCE = new DB2TypeConvert();

    /**
     * @inheritDoc
     */
    @Override
    public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(containsAny("char", "text", "json", "enum").then(DbColumnType.STRING))
            .test(contains("bigint").then(DbColumnType.LONG))
            .test(contains("smallint").then(DbColumnType.BASE_SHORT))
            .test(contains("int").then(DbColumnType.INTEGER))
            .test(containsAny("date", "time", "year").then(DbColumnType.DATE))
            .test(contains("bit").then(DbColumnType.BOOLEAN))
            .test(contains("decimal").then(DbColumnType.BIG_DECIMAL))
            .test(contains("clob").then(DbColumnType.CLOB))
            .test(contains("blob").then(DbColumnType.BLOB))
            .test(contains("binary").then(DbColumnType.BYTE_ARRAY))
            .test(contains("float").then(DbColumnType.FLOAT))
            .test(contains("double").then(DbColumnType.DOUBLE))
            .or(DbColumnType.STRING);
    }

}
