
package com.bugjc.ea.code.generator.config;


import com.bugjc.ea.code.generator.core.annotation.DbType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 表数据查询接口
 *
 * @author hubin
 * @since 2018-01-16
 */
public interface IDbQuery {

    /**
     * 数据库类型
     *
     * @deprecated 3.3.1 {@link DataSourceConfig#setDbType(DbType)}
     */
    @Deprecated
    default DbType dbType() {
        return null;
    }


    /**
     * 表信息查询 SQL
     */
    String tablesSql();


    /**
     * 表字段信息查询 SQL
     */
    String tableFieldsSql();


    /**
     * 表名称
     */
    String tableName();


    /**
     * 表注释
     */
    String tableComment();


    /**
     * 字段名称
     */
    String fieldName();


    /**
     * 字段类型
     */
    String fieldType();


    /**
     * 字段注释
     */
    String fieldComment();


    /**
     * 主键字段
     */
    String fieldKey();


    /**
     * 判断主键是否为identity
     *
     * @param results ResultSet
     * @return 主键是否为identity
     * @throws SQLException ignore
     */
    boolean isKeyIdentity(ResultSet results) throws SQLException;


    /**
     * 自定义字段名称
     */
    String[] fieldCustom();
}
