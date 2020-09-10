package com.bugjc.ea.mybatisplus.code.generator.config;

import com.bugjc.ea.code.generator.config.DataSourceConfig;
import com.bugjc.ea.code.generator.core.db.querys.MySqlQuery;
import com.bugjc.ea.code.generator.core.annotation.DbType;
import com.bugjc.flink.config.annotation.ConfigurationProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * mybatis 数据源
 *
 * @author aoki
 * @date 2020/8/10
 **/
@Data
@ConfigurationProperties(prefix = "mybatis.datasource.")
public class MyBatisDataSourceConfig implements Serializable {

    /**
     * 驱动名称
     */
    private String driverClassName;

    /**
     * 驱动连接的URL
     */
    private String url;

    /**
     * 数据库连接用户名
     */
    private String username;

    /**
     * 数据库连接密码
     */
    private String password;

    /**
     * 数据库类型
     */
    private String dbType;

    public DataSourceConfig getDataSourceConfig() {
        return new DataSourceConfig()
                .setDbType(DbType.getDbType(dbType))
                .setUrl(url)
                .setUsername(username)
                .setPassword(password)
                .setDriverName(driverClassName)
                .setDbQuery(new MySqlQuery() {
                    @Override
                    public String[] fieldCustom() {
                        //解释NUll:是否必填（NO,YES）;PRIVILEGES:权限（SELECT...）
                        return new String[]{"NULL", "PRIVILEGES"};
                    }
                });
    }
}
