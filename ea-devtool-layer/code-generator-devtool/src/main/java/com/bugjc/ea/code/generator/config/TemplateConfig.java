
package com.bugjc.ea.code.generator.config;

import com.bugjc.ea.code.generator.model.TemplateEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * 跟包相关的配置项
 *
 * @author aoki
 * @date 2020/8/27
 **/
@Data
@Accessors(chain = true)
public class TemplateConfig {
    /**
     * 包信息集合
     */
    private List<TemplateEntity> templates;

}
