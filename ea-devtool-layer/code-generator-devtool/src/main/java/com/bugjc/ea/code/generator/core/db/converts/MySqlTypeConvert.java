
package com.bugjc.ea.code.generator.core.db.converts;

import com.bugjc.ea.code.generator.config.GlobalConfig;
import com.bugjc.ea.code.generator.config.ITypeConvert;
import com.bugjc.ea.code.generator.core.db.rules.DbColumnType;
import com.bugjc.ea.code.generator.core.db.rules.IColumnType;

import static com.bugjc.ea.code.generator.core.db.converts.TypeConverts.contains;
import static com.bugjc.ea.code.generator.core.db.converts.TypeConverts.containsAny;

/**
 * MYSQL 数据库字段类型转换
 *
 * @author hubin, hanchunlin
 * @since 2017-01-20
 */
public class MySqlTypeConvert implements ITypeConvert {
    public static final MySqlTypeConvert INSTANCE = new MySqlTypeConvert();

    /**
     * @inheritDoc
     */
    @Override
    public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(containsAny("char", "text", "json", "enum").then(DbColumnType.STRING))
            .test(contains("bigint").then(DbColumnType.LONG))
            .test(containsAny("tinyint(1)", "bit").then(DbColumnType.BOOLEAN))
            .test(contains("int").then(DbColumnType.INTEGER))
            .test(contains("decimal").then(DbColumnType.BIG_DECIMAL))
            .test(contains("clob").then(DbColumnType.CLOB))
            .test(contains("blob").then(DbColumnType.BLOB))
            .test(contains("binary").then(DbColumnType.BYTE_ARRAY))
            .test(contains("float").then(DbColumnType.FLOAT))
            .test(contains("double").then(DbColumnType.DOUBLE))
            .test(containsAny("date", "time", "year").then(t -> toDateType(config, t)))
            .or(DbColumnType.STRING);
    }

    /**
     * 转换为日期类型
     *
     * @param config 配置信息
     * @param type   类型
     * @return 返回对应的列类型
     */
    public static IColumnType toDateType(GlobalConfig config, String type) {
        switch (config.getDateType()) {
            case ONLY_DATE:
                return DbColumnType.DATE;
            case SQL_PACK:
                switch (type) {
                    case "date":
                    case "year":
                        return DbColumnType.DATE_SQL;
                    case "time":
                        return DbColumnType.TIME;
                    default:
                        return DbColumnType.TIMESTAMP;
                }
            case TIME_PACK:
                switch (type) {
                    case "date":
                        return DbColumnType.LOCAL_DATE;
                    case "time":
                        return DbColumnType.LOCAL_TIME;
                    case "year":
                        return DbColumnType.YEAR;
                    default:
                        return DbColumnType.LOCAL_DATE_TIME;
                }
        }
        return DbColumnType.STRING;
    }

}
