/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bugjc.ea.code.generator;

import com.bugjc.ea.code.generator.config.DataSourceConfig;
import com.bugjc.ea.code.generator.config.GlobalConfig;
import com.bugjc.ea.code.generator.config.TemplateConfig;
import com.bugjc.ea.code.generator.config.StrategyConfig;
import com.bugjc.ea.code.generator.config.builder.ConfigBuilder;
import com.bugjc.ea.code.generator.model.TableField;
import com.bugjc.ea.code.generator.model.TableInfo;
import com.bugjc.ea.code.generator.core.toolkit.StringUtils;
import com.bugjc.ea.code.generator.core.engine.AbstractTemplateEngine;
import com.bugjc.ea.code.generator.core.engine.FreemarkerTemplateEngine;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 生成文件
 *
 * @author YangHu, tangguo, hubin
 * @since 2016-08-30
 */
@Data
@Accessors(chain = true)
public class AutoGenerator {
    private static final Logger logger = LoggerFactory.getLogger(AutoGenerator.class);

    /**
     * 配置信息
     */
    protected ConfigBuilder config;
    /**
     * 数据源配置
     */
    private DataSourceConfig dataSource;
    /**
     * 数据库表配置
     */
    private StrategyConfig strategy;
    /**
     * 包 相关配置
     */
    private TemplateConfig templateConfig;

    /**
     * 全局 相关配置
     */
    private GlobalConfig globalConfig;
    /**
     * 模板引擎
     */
    private AbstractTemplateEngine templateEngine;

    /**
     * 生成代码
     */
    public void execute() {
        logger.debug("==========================准备生成文件...==========================");
        // 初始化配置
        config = new ConfigBuilder(templateConfig, dataSource, strategy, globalConfig);
        // 模板引擎初始化执行文件输出
        templateEngine = new FreemarkerTemplateEngine();
        templateEngine.init(this.pretreatmentConfigBuilder(config)).mkdirs().batchOutput().open();
        logger.debug("==========================文件生成完成！！！==========================");
    }

    /**
     * 开放表信息、预留子类重写
     *
     * @param config 配置信息
     * @return ignore
     */
    private List<TableInfo> getAllTableInfoList(ConfigBuilder config) {
        return config.getTableInfoList();
    }

    /**
     * 预处理配置
     *
     * @param config 总配置信息
     * @return 解析数据结果集
     */
    private ConfigBuilder pretreatmentConfigBuilder(ConfigBuilder config) {
        /*
         * 表信息列表
         */
        List<TableInfo> tableList = this.getAllTableInfoList(config);
        for (TableInfo tableInfo : tableList) {
            tableInfo.setImportPackages("com.baomidou.mybatisplus.annotation.*");
            tableInfo.setImportPackages(Serializable.class.getCanonicalName());
            //TODO 自定义添加导入包

            // Boolean类型is前缀处理
            if (config.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix()
                    && tableInfo.getFields() != null
                    && !tableInfo.getFields().isEmpty()) {
                List<TableField> tableFields = tableInfo.getFields().stream().filter(field -> "boolean".equalsIgnoreCase(field.getPropertyType()))
                        .filter(field -> field.getPropertyName().startsWith("is")).collect(Collectors.toList());
                tableFields.forEach(field -> {
                    field.setConvert(true);
                    field.setPropertyName(StringUtils.removePrefixAfterPrefixToLower(field.getPropertyName(), 2));
                });
            }
        }
        return config.setTableInfoList(tableList);
    }
}
