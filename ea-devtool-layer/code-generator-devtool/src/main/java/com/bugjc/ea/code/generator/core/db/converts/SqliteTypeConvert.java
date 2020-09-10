
package com.bugjc.ea.code.generator.core.db.converts;

import com.bugjc.ea.code.generator.config.GlobalConfig;
import com.bugjc.ea.code.generator.config.ITypeConvert;
import com.bugjc.ea.code.generator.core.db.rules.DbColumnType;
import com.bugjc.ea.code.generator.core.db.rules.IColumnType;

/**
 * SQLite 字段类型转换
 *
 * @author chen_wj, hanchunlin
 * @since 2019-05-08
 */
public class SqliteTypeConvert implements ITypeConvert {
    public static final SqliteTypeConvert INSTANCE = new SqliteTypeConvert();

    /**
     * @inheritDoc
     * @see MySqlTypeConvert#toDateType(GlobalConfig, String)
     */
    @Override
    public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.contains("bigint").then(DbColumnType.LONG))
            .test(TypeConverts.containsAny("tinyint(1)", "boolean").then(DbColumnType.BOOLEAN))
            .test(TypeConverts.contains("int").then(DbColumnType.INTEGER))
            .test(TypeConverts.containsAny("text", "char", "enum").then(DbColumnType.STRING))
            .test(TypeConverts.containsAny("decimal", "numeric").then(DbColumnType.BIG_DECIMAL))
            .test(TypeConverts.contains("clob").then(DbColumnType.CLOB))
            .test(TypeConverts.contains("blob").then(DbColumnType.BLOB))
            .test(TypeConverts.contains("float").then(DbColumnType.FLOAT))
            .test(TypeConverts.contains("double").then(DbColumnType.DOUBLE))
            .test(TypeConverts.containsAny("date", "time", "year").then(t -> MySqlTypeConvert.toDateType(config, t)))
            .or(DbColumnType.STRING);
    }

}
