package com.bugjc.ea.mybatisplus.code.generator.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.bugjc.ea.code.generator.config.GlobalConfig;
import com.bugjc.ea.code.generator.config.rules.DateType;
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


    public GlobalConfig getGlobalConfig() {
        return new GlobalConfig()
                .setAuthor(author)
                .setOutputDir(outputDir)
                .setFileOverride(fileOverride)
                .setOpen(open)
                .setDateType(dateType)
                // 主键类型
                .setIdType(IdType.ASSIGN_ID);
    }
}
