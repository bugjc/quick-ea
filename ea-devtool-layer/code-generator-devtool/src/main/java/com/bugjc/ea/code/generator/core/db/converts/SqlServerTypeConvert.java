
package com.bugjc.ea.code.generator.core.db.converts;

import com.bugjc.ea.code.generator.config.GlobalConfig;
import com.bugjc.ea.code.generator.config.ITypeConvert;
import com.bugjc.ea.code.generator.core.db.rules.IColumnType;
import com.bugjc.ea.code.generator.core.db.rules.DbColumnType;

/**
 * SQLServer 字段类型转换
 *
 * @author hubin, hanchunlin
 * @since 2017-01-20
 */
public class SqlServerTypeConvert implements ITypeConvert {
    public static final SqlServerTypeConvert INSTANCE = new SqlServerTypeConvert();

    /**
     * @inheritDoc
     */
    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("char", "xml", "text").then(DbColumnType.STRING))
            .test(TypeConverts.contains("bigint").then(DbColumnType.LONG))
            .test(TypeConverts.contains("int").then(DbColumnType.INTEGER))
            .test(TypeConverts.containsAny("date", "time").then(DbColumnType.STRING))
            .test(TypeConverts.contains("bit").then(DbColumnType.BOOLEAN))
            .test(TypeConverts.containsAny("decimal", "numeric").then(DbColumnType.DOUBLE))
            .test(TypeConverts.contains("money").then(DbColumnType.BIG_DECIMAL))
            .test(TypeConverts.containsAny("binary", "image").then(DbColumnType.BYTE_ARRAY))
            .test(TypeConverts.containsAny("float", "real").then(DbColumnType.FLOAT))
            .or(DbColumnType.STRING);
    }

}
