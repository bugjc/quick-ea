
package com.bugjc.ea.code.generator.core.db.converts;

import com.bugjc.ea.code.generator.config.GlobalConfig;
import com.bugjc.ea.code.generator.config.ITypeConvert;
import com.bugjc.ea.code.generator.core.db.rules.DateType;
import com.bugjc.ea.code.generator.core.db.rules.DbColumnType;
import com.bugjc.ea.code.generator.core.db.rules.IColumnType;

/**
 * KingbaseES 字段类型转换
 *
 * @author kingbase, hanchunlin
 * @since 2019-10-12
 */
public class KingbaseESTypeConvert implements ITypeConvert {
    public static final KingbaseESTypeConvert INSTANCE = new KingbaseESTypeConvert();

    /**
     * @param globalConfig 全局配置
     * @param fieldType    字段类型
     * @return 返回对应的字段类型
     */
    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("char", "text", "json", "enum").then(DbColumnType.STRING))
            .test(TypeConverts.contains("bigint").then(DbColumnType.LONG))
            .test(TypeConverts.contains("int").then(DbColumnType.INTEGER))
            .test(TypeConverts.containsAny("date", "time").then(p -> toDateType(globalConfig, p)))
            .test(TypeConverts.containsAny("bit", "boolean").then(DbColumnType.BOOLEAN))
            .test(TypeConverts.containsAny("decimal", "numeric").then(DbColumnType.BIG_DECIMAL))
            .test(TypeConverts.contains("clob").then(DbColumnType.CLOB))
            .test(TypeConverts.contains("blob").then(DbColumnType.BYTE_ARRAY))
            .test(TypeConverts.contains("float").then(DbColumnType.FLOAT))
            .test(TypeConverts.contains("double").then(DbColumnType.DOUBLE))
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
