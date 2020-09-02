package com.bugjc.ea.mybatisplus.code.generator;

import com.bugjc.ea.code.generator.AutoGenerator;
import com.bugjc.ea.code.generator.engine.FreemarkerTemplateEngine;
import com.bugjc.ea.mybatisplus.code.generator.config.MyBatisDataSourceConfig;
import com.bugjc.ea.mybatisplus.code.generator.config.MyBatisGlobalConfig;
import com.bugjc.ea.mybatisplus.code.generator.config.MyBatisPackageConfig;
import com.bugjc.ea.mybatisplus.code.generator.config.MyBatisStrategyConfig;
import com.bugjc.flink.config.EnvironmentConfig;
import com.bugjc.flink.config.annotation.Application;

/**
 * 代码生成
 *
 * @author aoki
 * @date 2020/8/19
 **/
@Application
public class MyBatisPlusCodeGenerator {

    public static void main(String[] args) throws Exception {
        EnvironmentConfig environmentConfig = new EnvironmentConfig(args);
        MyBatisDataSourceConfig myBatisDataSourceConfig = environmentConfig.getComponent(MyBatisDataSourceConfig.class);
        MyBatisGlobalConfig myBatisGlobalConfig = environmentConfig.getComponent(MyBatisGlobalConfig.class);
        MyBatisPackageConfig myBatisPackageConfig = environmentConfig.getComponent(MyBatisPackageConfig.class);
        MyBatisStrategyConfig myBatisStrategyConfig = environmentConfig.getComponent(MyBatisStrategyConfig.class);


        //生成代码
        new AutoGenerator()
                .setGlobalConfig(myBatisGlobalConfig.getGlobalConfig())
                .setDataSource(myBatisDataSourceConfig.getDataSourceConfig())
                .setStrategy(myBatisStrategyConfig.getStrategyConfig())
                .setPackageInfo(myBatisPackageConfig.getPackageConfig())
                .setTemplateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
