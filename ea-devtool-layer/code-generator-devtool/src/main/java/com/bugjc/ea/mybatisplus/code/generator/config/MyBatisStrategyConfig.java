package com.bugjc.ea.mybatisplus.code.generator.config;

import com.bugjc.ea.code.generator.config.StrategyConfig;
import com.bugjc.ea.code.generator.core.db.rules.NamingStrategy;
import com.bugjc.flink.config.annotation.ConfigurationProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * mybatis 生成策略配置
 *
 * @author aoki
 * @date 2020/8/10
 **/
@Data
@ConfigurationProperties(prefix = "mybatis.strategy.")
public class MyBatisStrategyConfig implements Serializable {

    /**
     * 要生成的表前缀
     */
    private String[] tablePrefixes;

    /**
     * 要生成的表
     */
    private String[] tableNames;

    /**
     * 要生成的表字段前缀
     */
    private String[] fieldPrefixes;

    public StrategyConfig getStrategyConfig() {
        return new StrategyConfig()
                .setTablePrefix(tablePrefixes)
                .setFieldPrefix(fieldPrefixes)
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNames);
    }

}
