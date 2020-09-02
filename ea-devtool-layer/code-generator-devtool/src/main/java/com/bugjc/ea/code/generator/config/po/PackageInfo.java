package com.bugjc.ea.code.generator.config.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 包配置信息
 * 数据示例：Module ->  Service -> com.bugjc.ea.devtool.mybatis.code.generator.test.business
 *
 * @author aoki
 * @date 2020/8/27
 **/
@Data
public class PackageInfo implements Serializable {

    /**
     * 父包的路徑（必须）
     * 示例：com.bugjc.ea.devtool.mybatis.code.generator.test
     */
    private String parent;

    /**
     * 包名 （必须）
     * 示例：api
     */
    private String packageName;

    /**
     * 包的路径
     * 例如：com.bugjc.ea.devtool.mybatis.code.generator.test.api
     */
    private String packagePath;

    /**
     * 包的绝对路径
     * 示例：E://xxx/mybatis-plus-generator-devtool/src/main/java/com/bugjc/ea/devtool/mybatis/code/generator/test/api
     */
    private String absolutePath;

    /**
     * 模板文件所在的相对路径 （必须）
     * 示例：/templates/api/Api.java.ftl
     */
    private String templatePath;

    /**
     * 文件所在的绝对路径
     * 示例：E://xxx/mybatis-plus-generator-devtool/src/main/java/com/bugjc/ea/devtool/mybatis/code/generator/test/api/ArticleApi
     */
    private String filePath;

    /**
     * 文件命名规约（必须）
     * 示例：%sEntity
     */
    private String fileNamingConvention;

    /**
     * 模板数据
     */
    private Map<String, Object> templateData;

    /**
     * 内部依赖包（选填），通过此配置来建立代码生成期内同一个上下文内部间的依赖关系。
     * 示例：当前要生成的表是 tb_article,{@link dependPackageNames} 配置的是 dao,model.entity,
     *      则会在生成的过程中自动建立 tb_article 表依赖类的目标数据 {@link dependClasses} 为模板提供关系相互引用。
     */
    private String[] dependPackageNames;

    /**
     * 外部依赖类（选填）
     * 示例：com.alibaba.fastjson.JSON,org.junit.jupiter.api.Test
     */
    private Map<String, DependClass> externalDependClasses;

    /**
     * 依赖 class
     */
    private Map<String, DependClass> dependClasses;

    /**
     * 类名
     * 示例：ArticleApi
     */
    private String className;

    /**
     * 引用路径
     * 示例：com.bugjc.ea.devtool.mybatis.code.generator.test.api.ArticleApi
     */
    private String referencePath;

}
