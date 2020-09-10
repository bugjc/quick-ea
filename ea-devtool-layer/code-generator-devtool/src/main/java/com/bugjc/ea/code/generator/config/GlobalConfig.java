
package com.bugjc.ea.code.generator.config;

import com.bugjc.ea.code.generator.core.db.rules.DateType;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 全局配置
 *
 * @author hubin
 * @since 2016-12-02
 */
@Data
@Accessors(chain = true)
public class GlobalConfig {

    /**
     * 生成文件的输出目录【默认 D 盘根目录】
     */
    private String outputDir = "D://";

    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride = false;

    /**
     * 是否打开输出目录
     */
    private boolean open = true;

    /**
     * 开发人员
     */
    private String author;

    /**
     * 时间类型对应策略
     */
    private DateType dateType = DateType.TIME_PACK;
}
