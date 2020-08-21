package com.bugjc.ea.devtool.mybatis.code.generator.config;

import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
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
     * 是否大写命名
     */
    private Boolean isCapitalMode;

    /**
     * 是否跳过视图
     */
    private Boolean skipView;

    /**
     * 数据库表映射到实体的命名策略
     */
    private NamingStrategy naming = NamingStrategy.underline_to_camel;

    /**
     * 【实体】是否为lombok模型
     */
    private Boolean entityLombokModel;

    /**
     * 【实体】是否为链式模型
     */
    private Boolean chainModel;

    /**
     * 【实体】是否生成字段常量
     */
    private Boolean entityColumnConstant;

    /**
     * 是否生成实体时，生成字段注解
     */
    private Boolean entityTableFieldAnnotationEnable;

    /**
     * 逻辑删除属性名称
     */
    private String logicDeleteFieldName;

    /**
     * 乐观锁属性名称
     */
    private String versionFieldName;

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
                .setCapitalMode(isCapitalMode)
                .setSkipView(skipView)
                .setTablePrefix(tablePrefixes)
                .setFieldPrefix(fieldPrefixes)
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNames)
                .setEntityLombokModel(entityLombokModel)
                .setChainModel(chainModel)
                .setEntityColumnConstant(entityColumnConstant)
                .setLogicDeleteFieldName(logicDeleteFieldName)
                .setEntityTableFieldAnnotationEnable(entityTableFieldAnnotationEnable)
                .setVersionFieldName(versionFieldName)
                ;
    }

}
