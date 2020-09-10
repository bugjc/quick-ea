
package com.bugjc.ea.code.generator.core.db.querys;

import com.bugjc.ea.code.generator.config.IDbQuery;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 表数据查询抽象类
 *
 * @author hubin
 * @since 2018-01-16
 */
public abstract class AbstractDbQuery implements IDbQuery {


    @Override
    public boolean isKeyIdentity(ResultSet results) throws SQLException {
        return false;
    }


    @Override
    public String[] fieldCustom() {
        return null;
    }
}
