package com.bugjc.ea.devtool.mybatis.code.generator.config;

import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.bugjc.flink.config.annotation.ConfigurationProperties;
import lombok.Data;

/**
 * 包命名配置
 *
 * @author aoki
 * @date 2020/8/19
 **/
@Data
@ConfigurationProperties(prefix = "mybatis.package.")
public class MyBatisPackageConfig {
    /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    private String parent;

    /**
     * Controller包名
     */
    private String controller;

    /**
     * Service包名
     */
    private String service;

    /**
     * Service Impl包名
     */
    private String serviceImpl;

    /**
     * Entity包名
     */
    private String modelEntity;

    /**
     * Mapper包名
     */
    private String mapper;
    /**
     * Mapper XML包名
     */
    private String mapperXml;

    public PackageConfig getPackageConfig() {
        return new PackageConfig()
                .setParent(parent)
                .setController(controller)
                .setEntity(modelEntity)
                .setMapper(mapper)
                .setXml(mapperXml)
                .setService(service)
                .setServiceImpl(serviceImpl);
    }
}
