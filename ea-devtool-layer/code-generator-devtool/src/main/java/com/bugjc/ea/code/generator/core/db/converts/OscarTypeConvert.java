
package com.bugjc.ea.code.generator.core.db.converts;

import com.bugjc.ea.code.generator.config.GlobalConfig;
import com.bugjc.ea.code.generator.config.ITypeConvert;
import com.bugjc.ea.code.generator.core.db.rules.DateType;
import com.bugjc.ea.code.generator.core.db.rules.DbColumnType;
import com.bugjc.ea.code.generator.core.db.rules.IColumnType;

import static com.bugjc.ea.code.generator.core.db.converts.TypeConverts.contains;
import static com.bugjc.ea.code.generator.core.db.converts.TypeConverts.containsAny;

/**
 * KingbaseES 字段类型转换
 *
 * @author kingbase, hanchunlin
 * @since 2019-10-12
 */
public class OscarTypeConvert implements ITypeConvert {
    public static final OscarTypeConvert INSTANCE = new OscarTypeConvert();

    /**
     * @param globalConfig 全局配置
     * @param fieldType    字段类型
     * @return 返回对应的字段类型
     */
    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(containsAny("CHARACTER", "char", "varchar", "text", "character varying").then(DbColumnType.STRING))
            .test(containsAny("bigint", "int8").then(DbColumnType.LONG))
            .test(containsAny("int", "int1", "int2", "int3", "int4", "tinyint", "integer").then(DbColumnType.INTEGER))
            .test(containsAny("date", "time", "timestamp").then(p -> toDateType(globalConfig, p)))
            .test(containsAny("bit", "boolean").then(DbColumnType.BOOLEAN))
            .test(containsAny("decimal", "numeric", "number").then(DbColumnType.BIG_DECIMAL))
            .test(contains("clob").then(DbColumnType.CLOB))
            .test(contains("blob").then(DbColumnType.BYTE_ARRAY))
            .test(contains("float").then(DbColumnType.FLOAT))
            .test(containsAny("double", "real", "float4", "float8").then(DbColumnType.DOUBLE))
            .or(DbColumnType.STRING);
    }

    /**
     * 转换为日期类型
     *
     * @param config 配置信息
     * @param type   类型
     * @return 返回对应的列类型
     */
    private IColumnType toDateType(GlobalConfig config, String type) {
        DateType dateType = config.getDateType();
        if (dateType == DateType.SQL_PACK) {
            switch (type) {
                case "date":
                    return DbColumnType.DATE_SQL;
                case "time":
                    return DbColumnType.TIME;
                default:
                    return DbColumnType.TIMESTAMP;
            }
        } else if (dateType == DateType.TIME_PACK) {
            switch (type) {
                case "date":
                    return DbColumnType.LOCAL_DATE;
                case "time":
                    return DbColumnType.LOCAL_TIME;
                default:
                    return DbColumnType.LOCAL_DATE_TIME;
            }
        }
        return DbColumnType.DATE;
    }

}
