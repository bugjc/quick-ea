package com.bugjc.ea.mybatisplus.code.generator.config;

import com.bugjc.ea.code.generator.config.TemplateConfig;
import com.bugjc.ea.code.generator.model.TemplateEntity;
import com.bugjc.flink.config.annotation.ConfigurationProperties;
import lombok.Data;

import java.util.List;

/**
 * 包命名配置
 *
 * @author aoki
 * @date 2020/8/19
 **/
@Data
@ConfigurationProperties(prefix = "mybatis.")
public class MyBatisTemplateConfig {

    /**
     * 包信息集合
     */
    private List<TemplateEntity> templates;

    public TemplateConfig getPackageConfig() {
        return new TemplateConfig()
                .setTemplates(templates);
    }
}
