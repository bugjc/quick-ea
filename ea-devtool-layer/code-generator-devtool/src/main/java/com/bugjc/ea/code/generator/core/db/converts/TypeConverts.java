
package com.bugjc.ea.code.generator.core.db.converts;

import com.bugjc.ea.code.generator.config.ITypeConvert;
import com.bugjc.ea.code.generator.core.db.converts.select.BranchBuilder;
import com.bugjc.ea.code.generator.core.db.converts.select.Selector;
import com.bugjc.ea.code.generator.core.db.rules.IColumnType;
import com.bugjc.ea.code.generator.core.annotation.DbType;

/**
 * 该注册器负责注册并查询类型注册器
 *
 * @author nieqiuqiu, hanchunlin
 * @since 3.3.1
 */
public class TypeConverts {

    /**
     * 查询数据库类型对应的类型转换器
     *
     * @param dbType 数据库类型
     * @return 返回转换器
     */
    public static ITypeConvert getTypeConvert(DbType dbType) {
        /**
         * 数据库类型转换选择器
         */
        switch (dbType) {
            case ORACLE:
                return OracleTypeConvert.INSTANCE;
            case DB2:
                return DB2TypeConvert.INSTANCE;
            case DM:
            case GAUSS:
                return DmTypeConvert.INSTANCE;
            case KINGBASE_ES:
                return KingbaseESTypeConvert.INSTANCE;
            case OSCAR:
                return OscarTypeConvert.INSTANCE;
            case MYSQL:
            case MARIADB:
                return MySqlTypeConvert.INSTANCE;
            case POSTGRE_SQL:
                return PostgreSqlTypeConvert.INSTANCE;
            case SQLITE:
                return SqliteTypeConvert.INSTANCE;
            case SQL_SERVER:
                return SqlServerTypeConvert.INSTANCE;
            default:
                break;
        }
        return null;
    }

    /**
     * 使用指定参数构建一个选择器
     *
     * @param param 参数
     * @return 返回选择器
     */
    static Selector<String, IColumnType> use(String param) {
        return new Selector<>(param.toLowerCase());
    }

    /**
     * 这个分支构建器用于构建用于支持 {@link String#contains(CharSequence)} 的分支
     *
     * @param value 分支的值
     * @return 返回分支构建器
     * @see #containsAny(CharSequence...)
     */
    static BranchBuilder<String, IColumnType> contains(CharSequence value) {
        return BranchBuilder.of(s -> s.contains(value));
    }

    /**
     * @see #contains(CharSequence)
     */
    static BranchBuilder<String, IColumnType> containsAny(CharSequence... values) {
        return BranchBuilder.of(s -> {
            for (CharSequence value : values) {
                if (s.contains(value)) {
                    return true;
                }
            }
            return false;
        });
    }

}
