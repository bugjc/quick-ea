package com.bugjc.ea.devtool.mybatis.code.generator.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.bugjc.flink.config.annotation.ConfigurationProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * 全局配置
 *
 * @author aoki
 * @date 2020/8/12
 **/
@Data
@ConfigurationProperties(prefix = "mybatis.global.")
public class MyBatisGlobalConfig implements Serializable {

    /**
     * 开发人员
     */
    private String author;

    /**
     * 生成文件的输出目录
     */
    private String outputDir;

    /**
     * 是否覆盖已有文件
     */
    private Boolean fileOverride;

    /**
     * 是否打开输出目录
     */
    private Boolean open;

    /**
     * 时间类型对应策略
     */
    private DateType dateType = DateType.TIME_PACK;

    /**
     * 开启 ActiveRecord 模式
     */
    private Boolean activeRecord;

    /**
     * 是否在xml中添加二级缓存配置
     */
    private Boolean enableCache;

    /**
     * 开启 BaseResultMap
     */
    private Boolean baseResultMap;

    /**
     * 开启 baseColumnList
     */
    private Boolean baseColumnList;

    /**
     * 开启 Kotlin 模式
     */
    private Boolean kotlin;

    /**
     * 各层文件名称方式，例如： %sAction 生成 UserAction
     * %s 为占位符
     */
    private String entityName;
    private String mapperName;
    private String xmlName;
    private String serviceName;
    private String serviceImplName;
    private String controllerName;
    
    public GlobalConfig getGlobalConfig() {
        return new GlobalConfig()
                .setAuthor(author)
                .setOutputDir(outputDir)
                .setFileOverride(fileOverride)
                .setOpen(open)
                .setDateType(dateType)
                .setActiveRecord(activeRecord)
                .setEnableCache(enableCache)
                .setBaseResultMap(baseResultMap)
                .setBaseColumnList(baseColumnList)
                .setKotlin(kotlin)
                .setEntityName(entityName)
                .setMapperName(mapperName)
                .setXmlName(xmlName)
                .setServiceName(serviceName)
                .setServiceImplName(serviceImplName)
                .setControllerName(controllerName)
                // 主键类型
                .setIdType(IdType.ASSIGN_ID);
    }
}
